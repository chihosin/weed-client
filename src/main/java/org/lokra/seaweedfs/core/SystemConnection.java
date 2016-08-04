package org.lokra.seaweedfs.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.lokra.seaweedfs.core.master.MasterStatus;
import org.lokra.seaweedfs.core.topology.DataCenter;
import org.lokra.seaweedfs.core.topology.DataNode;
import org.lokra.seaweedfs.core.topology.Layout;
import org.lokra.seaweedfs.core.topology.Rack;
import org.lokra.seaweedfs.exception.SeaweedfsException;
import org.lokra.seaweedfs.util.ConnectionUtil;
import org.lokra.seaweedfs.util.ServerApiStrategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Master server connection control
 *
 * @author Chiho Sin
 */
public class SystemConnection {

    private static final Log log = LogFactory.getLog(SystemConnection.class);

    private String leaderUrl;
    private int pollCycle;
    private boolean connectionClose = true;
    private RequestConfig requestConfig;
    private SystemClusterStatus systemClusterStatus;
    private SystemTopologyStatus systemTopologyStatus;
    private PollClusterStatusThread pollClusterStatusThread;
    private RequestConfig defaultRequestConfig = RequestConfig.DEFAULT;
    private ObjectMapper objectMapper = new ObjectMapper();
    private PoolingHttpClientConnectionManager clientConnectionManager;
    private IdleConnectionMonitorThread idleConnectionMonitorThread;
    private CloseableHttpClient httpClient;

    /**
     * Constructor.
     *
     * @param leaderUrl initial leader uri
     * @param timeout   server connect timeout
     * @param pollCycle polls for server change cycle time
     */
    public SystemConnection(String leaderUrl,
                            int timeout,
                            int pollCycle,
                            int maxConnection,
                            int maxConnectionsPreRoute)
            throws IOException {
        this.leaderUrl = leaderUrl;
        this.requestConfig = RequestConfig.custom()
                .setConnectTimeout(timeout)
                .build();
        this.pollCycle = pollCycle;
        this.pollClusterStatusThread = new PollClusterStatusThread();
        this.idleConnectionMonitorThread = new IdleConnectionMonitorThread();
        this.clientConnectionManager = new PoolingHttpClientConnectionManager();
        this.clientConnectionManager.setMaxTotal(maxConnection);
        this.clientConnectionManager.setDefaultMaxPerRoute(maxConnectionsPreRoute);
        this.httpClient = HttpClients.custom()
                .setConnectionManager(this.clientConnectionManager)
                .setDefaultRequestConfig(this.defaultRequestConfig)
                .build();
    }

    /**
     * Start up polls for core leader.
     */
    public void startup() {
        log.info("core connection is startup now");
        this.pollClusterStatusThread.start();
        this.idleConnectionMonitorThread.start();
    }

    /**
     * Shutdown polls for core leader.
     */
    public void stop() {
        log.info("core connection is shutdown now");
        this.pollClusterStatusThread.shutdown();
        this.idleConnectionMonitorThread.shutdown();
    }

    /**
     * Get core server cluster status.
     *
     * @return core cluster status.
     */
    public SystemClusterStatus getSystemClusterStatus() {
        return systemClusterStatus;
    }

    /**
     * Get cluster topology status.
     *
     * @return core topology status.
     */
    public SystemTopologyStatus getSystemTopologyStatus() {
        return systemTopologyStatus;
    }

    /**
     * Connection close flag.
     *
     * @return if result is false, that maybe core server is failover.
     */
    public boolean isConnectionClose() {
        return connectionClose;
    }

    /**
     * Get leader core server uri.
     *
     * @return core server uri
     */
    public String getLeaderUrl() {
        return this.leaderUrl;
    }

    /**
     * Get http client create with pool.
     *
     * @return Http client.
     */
    public CloseableHttpClient getClientFromPool() {
        return httpClient;
    }

    /**
     * Fetch core server by seaweedfs Http API.
     *
     * @param masterUrl core server url with scheme
     * @return Return the cluster status
     */
    @SuppressWarnings("unchecked")
    private SystemClusterStatus fetchSystemClusterStatus(String masterUrl) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        MasterStatus leader;
        HashMap<String, MasterStatus> peers;
        final HttpGet request = new HttpGet(masterUrl + ServerApiStrategy.checkClusterStatus);
        request.setConfig(defaultRequestConfig);
        final ResponseHandler<String> responseHandler = new BasicResponseHandler();
        final String response = httpClient.execute(request, responseHandler);
        Map map = objectMapper.readValue(response, Map.class);

        if (map.get("Leader") != null) {
            leader = new MasterStatus(ConnectionUtil.convertUrlWithScheme((String) map.get("Leader")));
        } else {
            httpClient.close();
            throw new SeaweedfsException("not found seaweedfs core leader");
        }

        peers = new HashMap<String, MasterStatus>();

        if (map.get("Peers") != null) {
            List<String> rawPeerList = (List<String>) map.get("Peers");
            for (String url : rawPeerList) {
                MasterStatus peer = new MasterStatus(ConnectionUtil.convertUrlWithScheme(url));
                peers.put(peer.getUrl(), peer);
            }
        }

        if (map.get("IsLeader") == null || !((Boolean) map.get("IsLeader"))) {
            peers.put(masterUrl, new MasterStatus(masterUrl));
            peers.remove(leader.getUrl());
            leader.setActive(
                    ConnectionUtil.checkUriAlive(httpClient, leader.getUrl(), this.requestConfig));
            if (!leader.isActive())
                throw new SeaweedfsException("seaweedfs core leader is failover");
        } else {
            leader.setActive(true);
        }

        for (String url : peers.keySet()) {
            peers.get(url).setActive(ConnectionUtil.checkUriAlive(httpClient, url, this.requestConfig));
        }

        httpClient.close();
        return new SystemClusterStatus(leader, peers);

    }

    /**
     * Find leader core server from peer core server info.
     *
     * @param peers peers core server
     * @return If not found the leader, result is null.
     */
    private String findLeaderUriByPeers(Map<String, MasterStatus> peers) throws IOException {
        if (peers == null || peers.size() == 0)
            return null;
        else {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            try {
                String result;
                for (String uri : peers.keySet()) {
                    final HttpGet request = new HttpGet(uri + ServerApiStrategy.checkClusterStatus);
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
     * Fetch topology by seaweedfs Http Api.
     *
     * @param masterUrl core server url with scheme
     * @return Return the topology status
     */
    @SuppressWarnings("unchecked")
    private SystemTopologyStatus fetchSystemTopologyStatus(String masterUrl) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        final HttpGet request = new HttpGet(masterUrl + ServerApiStrategy.checkTopologyStatus);
        request.setConfig(defaultRequestConfig);
        final ResponseHandler<String> responseHandler = new BasicResponseHandler();
        final String response = httpClient.execute(request, responseHandler);
        Map map = objectMapper.readValue(response, Map.class);

        // Fetch data center from json
        Map<String, DataCenter> dataCenters = new HashMap<String, DataCenter>();
        ArrayList<Map<String, Object>> rawDcs =
                ((ArrayList<Map<String, Object>>) ((Map) (map.get("Topology"))).get("DataCenters"));
        if (rawDcs != null)
            for (Map<String, Object> rawDc : rawDcs) {
                DataCenter dc = new DataCenter();
                dc.setFree((Integer) rawDc.get("Free"));
                dc.setId((String) rawDc.get("Id"));
                dc.setMax((Integer) rawDc.get("Max"));

                Map<String, Rack> racks = new HashMap<String, Rack>();
                ArrayList<Map<String, Object>> rawRks =
                        ((ArrayList<Map<String, Object>>) (rawDc.get("Racks")));
                if (rawRks != null)
                    for (Map<String, Object> rawRk : rawRks) {
                        Rack rk = new Rack();
                        rk.setMax((Integer) rawRk.get("Max"));
                        rk.setId((String) rawRk.get("Id"));
                        rk.setFree((Integer) rawRk.get("Free"));

                        Map<String, DataNode> dataNodes = new HashMap<String, DataNode>();
                        ArrayList<Map<String, Object>> rawDns =
                                ((ArrayList<Map<String, Object>>) (rawRk.get("DataNodes")));

                        if (rawDns != null)
                            for (Map<String, Object> rawDn : rawDns) {
                                DataNode dn = new DataNode();
                                dn.setFree((Integer) rawDn.get("Free"));
                                dn.setMax((Integer) rawDn.get("Max"));
                                dn.setVolumes((Integer) rawDn.get("Volumes"));
                                dn.setUrl((String) rawDn.get("Url"));
                                dn.setPubilcUrl((String) rawDn.get("PublicUrl"));
                                dataNodes.put(dn.getUrl(), dn);
                            }
                        rk.setDataNodes(dataNodes);
                        racks.put(rk.getId(), rk);
                    }
                dc.setRacks(racks);
                dataCenters.put(dc.getId(), dc);
            }

        // Fetch data layout
        ArrayList<Layout> layouts = new ArrayList<Layout>();
        ArrayList<Map<String, Object>> rawLos =
                ((ArrayList<Map<String, Object>>) ((Map) (map.get("Topology"))).get("layouts"));
        if (rawLos != null)
            for (Map<String, Object> rawLo : rawLos) {
                Layout layout = new Layout();
                if (rawLo.get("collection") != null || !((String) rawLo.get("collection")).isEmpty())
                    layout.setCollection((String) rawLo.get("collection"));
                if (rawLo.get("replication") != null || !((String) rawLo.get("replication")).isEmpty())
                    layout.setReplication((String) rawLo.get("replication"));
                if (rawLo.get("ttl") != null || !((String) rawLo.get("ttl")).isEmpty())
                    layout.setTtl((String) rawLo.get("ttl"));
                if (rawLo.get("writables") != null)
                    layout.setWritables(((ArrayList<Integer>) rawLo.get("writables")));

                layouts.add(layout);
            }

        SystemTopologyStatus systemTopologyStatus = new SystemTopologyStatus();
        systemTopologyStatus.setDataCenters(dataCenters);
        systemTopologyStatus.setLayouts(layouts);
        systemTopologyStatus.setFree((Integer) ((Map) (map.get("Topology"))).get("Free"));
        systemTopologyStatus.setMax((Integer) ((Map) (map.get("Topology"))).get("Max"));
        systemTopologyStatus.setVersion((String) map.get("Version"));

        return systemTopologyStatus;
    }

    /**
     * Thread for cycle to check cluster status.
     */
    private class PollClusterStatusThread extends Thread {

        private volatile boolean shutdown;

        @Override
        public void run() {
            while (!shutdown) {

                try {
                    fetchSystemStatus(leaderUrl);
                    connectionClose = false;
                } catch (IOException e) {
                    connectionClose = true;
                    log.error("unable connect to the target seaweedfs core [" + leaderUrl + "]");
                }

                try {
                    if (connectionClose) {
                        log.info("lookup seaweedfs core leader by peers");
                        if (systemClusterStatus == null || systemClusterStatus.getPeers().size() == 0) {
                            log.error("cloud not found the seaweedfs core peers");
                        } else {
                            String url = findLeaderUriByPeers(systemClusterStatus.getPeers());
                            if (url != null) {
                                log.error("seaweedfs core cluster is failover");
                                fetchSystemStatus(url);
                                connectionClose = false;
                            } else {
                                log.error("seaweedfs core cluster is down");
                                systemClusterStatus.getLeader().setActive(false);
                                connectionClose = true;
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("unable connect to the seaweedfs core leader");
                }

                try {
                    Thread.sleep(pollCycle);
                } catch (InterruptedException ignored) {
                }
            }
        }

        private void fetchSystemStatus(String url) throws IOException {
            systemClusterStatus = fetchSystemClusterStatus(url);
            systemTopologyStatus = fetchSystemTopologyStatus(url);
            if (!leaderUrl.equals(systemClusterStatus.getLeader().getUrl())) {
                leaderUrl = (systemClusterStatus.getLeader().getUrl());
                log.info("seaweedfs core leader is change to [" + leaderUrl + "]");
            }
            log.debug("seaweedfs core leader is found [" + leaderUrl + "]");
        }

        void shutdown() {
            shutdown = true;
            synchronized (this) {
                notifyAll();
            }
        }

    }

    /**
     * Thread for close expired connections.
     */
    private class IdleConnectionMonitorThread extends Thread {

        private volatile boolean shutdown;

        @Override
        public void run() {
            try {
                while (!shutdown) {
                    synchronized (this) {
                        wait(pollCycle);
                        // 关闭无效连接
                        clientConnectionManager.closeExpiredConnections();
                        // 关闭空闲超过30秒的
                        clientConnectionManager.closeIdleConnections(30, TimeUnit.SECONDS);
                        log.debug("http client pool state [" + clientConnectionManager.getTotalStats().toString() + "]");
                    }
                }
            } catch (InterruptedException ignored) {
            }
        }

        void shutdown() {
            shutdown = true;
            synchronized (this) {
                notifyAll();
            }
        }
    }
}
