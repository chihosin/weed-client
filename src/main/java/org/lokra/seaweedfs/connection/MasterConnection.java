package org.lokra.seaweedfs.connection;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.lokra.seaweedfs.SeaweedfsException;
import org.lokra.seaweedfs.util.SeaweedfsHttpApiStrategy;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Master server connection control
 *
 * @author Chiho Sin
 */
public class MasterConnection {

    private static final Log log = LogFactory.getLog(MasterConnection.class);

    private String leaderUrl;
    private int pollCycle;
    private boolean connectionClose = true;
    private RequestConfig requestConfig;
    private MasterClusterStatus masterClusterStatus;
    private PollClusterStatusThread pollClusterStatusThread;
    private RequestConfig defaultRequestConfig = RequestConfig.DEFAULT;
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructor.
     *
     * @param leaderUrl initial leader uri
     * @param timeout   server connect timeout
     * @param pollCycle polls for server change cycle time
     */
    public MasterConnection(String leaderUrl, int timeout, int pollCycle)
            throws IOException {
        this.leaderUrl = leaderUrl;
        this.requestConfig = RequestConfig.custom()
                .setConnectTimeout(timeout)
                .build();
        this.pollCycle = pollCycle;
        this.pollClusterStatusThread = new PollClusterStatusThread();
    }

    /**
     * Start up polls for master leader.
     */
    void startup() {
        log.info("master connection is startup now");
        this.pollClusterStatusThread.start();
    }

    /**
     * Shutdown polls for master leader.
     */
    void stop() {
        log.info("master connection is shutdown now");
        this.pollClusterStatusThread.interrupt();
    }

    /**
     * Get master server cluster status.
     *
     * @return master cluster status.
     */
    public MasterClusterStatus getMasterClusterStatus() {
        return masterClusterStatus;
    }

    /**
     * Connection close flag.
     *
     * @return if result is false, that maybe master server is failover.
     */
    public boolean isConnectionClose() {
        return connectionClose;
    }

    /**
     * Get leader master server uri.
     *
     * @return master server uri
     */
//    public URI getConnectionUrl() {
//        return this.leaderUrl;
//    }

    /**
     * Fetch master server by seaweedfs Http API.
     *
     * @param masterUrl master server url with scheme
     * @return Return the cluster status
     */
    @SuppressWarnings("unchecked")
    private MasterClusterStatus fetchMasterClusterStatus(String masterUrl) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ServerStatus leader;
        ConcurrentHashMap<String, ServerStatus> peers;
        final HttpGet request = new HttpGet(masterUrl + SeaweedfsHttpApiStrategy.checkClusterStatus);
        request.setConfig(defaultRequestConfig);
        final ResponseHandler<String> responseHandler = new BasicResponseHandler();
        final String response = httpClient.execute(request, responseHandler);
        Map map = objectMapper.readValue(response, Map.class);

        if (map.get("Leader") != null) {
            leader = new ServerStatus(ConnectionUtil.convertUrlWithScheme((String) map.get("Leader")));
        } else {
            httpClient.close();
            throw new SeaweedfsException("not found seaweedfs master leader");
        }

        peers = new ConcurrentHashMap<String, ServerStatus>();

        if (map.get("Peers") != null) {
            List<String> rawPeerList = (List<String>) map.get("Peers");
            for (String url : rawPeerList) {
                ServerStatus peer = new ServerStatus(ConnectionUtil.convertUrlWithScheme(url));
                peers.put(peer.getUrl(), peer);
            }
        }

        if (map.get("IsLeader") == null || !((Boolean) map.get("IsLeader"))) {
            peers.put(masterUrl, new ServerStatus(masterUrl));
            peers.remove(leader.getUrl());
            leader.setActive(
                    ConnectionUtil.checkUriAlive(httpClient, leader.getUrl(), this.requestConfig));
            if (!leader.isActive())
                throw new SeaweedfsException("seaweedfs master leader is failover");
        } else {
            leader.setActive(true);
        }

        for (String url : peers.keySet()) {
            peers.get(url).setActive(ConnectionUtil.checkUriAlive(httpClient, url, this.requestConfig));
        }

        httpClient.close();
        return new MasterClusterStatus(leader, peers);

    }

    /**
     * Find leader master server from peer master server info.
     *
     * @param peers peers master server
     * @return If not found the leader, result is null.
     */
    private String findLeaderUriByPeers(ConcurrentHashMap<String, ServerStatus> peers) throws IOException {
        if (peers == null || peers.size() == 0)
            return null;
        else {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            try {
                String result;
                for (String uri : peers.keySet()) {
                    final HttpGet request = new HttpGet(uri + SeaweedfsHttpApiStrategy.checkClusterStatus);
                    request.setConfig(defaultRequestConfig);
                    final ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    String response;
                    Map responseMap;
                    try {
                        response = httpClient.execute(request, responseHandler);
                        responseMap = objectMapper.readValue(response, Map.class);
                    } catch (IOException e) {
                        continue;
                    }
                    if (responseMap.get("Leader") != null) {
                        result = ConnectionUtil.convertUrlWithScheme((String) responseMap.get("Leader"));

                        if (ConnectionUtil.checkUriAlive(httpClient, result, this.requestConfig))
                            return result;
                    }
                }
            } finally {
                httpClient.close();
            }
        }
        return null;
    }

    /**
     * Thread for cycle to check cluster status.
     */
    private class PollClusterStatusThread extends Thread {

        @Override
        public void run() {
            while (true) {

                try {
                    fetchLeaderUriByMasterClusterStatus(leaderUrl);
                    connectionClose = false;
                } catch (IOException e) {
                    connectionClose = true;
                    log.error("unable connect to the target seaweedfs master [" + leaderUrl + "]");
                }

                try {
                    if (connectionClose) {
                        log.info("lookup seaweedfs master leader by peers");
                        if (masterClusterStatus == null || masterClusterStatus.getPeers().size() == 0) {
                            log.error("cloud not found the seaweedfs master peers");
                        } else {
                            String url = findLeaderUriByPeers(masterClusterStatus.getPeers());
                            if (url != null) {
                                log.error("seaweedfs master cluster is failover");
                                fetchLeaderUriByMasterClusterStatus(url);
                                connectionClose = false;
                            } else {
                                log.error("seaweedfs master cluster is down");
                                masterClusterStatus.getLeader().setActive(false);
                                connectionClose = true;
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("unable connect to the seaweedfs master leader");
                }

                try {
                    Thread.sleep(pollCycle);
                } catch (InterruptedException e) {
                    connectionClose = true;
                    e.printStackTrace();
                    break;
                }
            }
        }

        private void fetchLeaderUriByMasterClusterStatus(String url) throws IOException {
            masterClusterStatus = fetchMasterClusterStatus(url);
            if (!leaderUrl.equals(masterClusterStatus.getLeader().getUrl())) {
                leaderUrl = (masterClusterStatus.getLeader().getUrl());
                log.info("seaweedfs master leader is change to [" + leaderUrl + "]");
            }
            log.info("seaweedfs master leader is found [" + leaderUrl + "]");
        }

    }
}
