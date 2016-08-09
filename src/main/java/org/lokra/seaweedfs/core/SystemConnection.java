package org.lokra.seaweedfs.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.lokra.seaweedfs.cache.HeaderCache;
import org.lokra.seaweedfs.cache.StreamCache;
import org.lokra.seaweedfs.core.topology.*;
import org.lokra.seaweedfs.exception.SeaweedfsException;
import org.lokra.seaweedfs.util.ConnectionUtil;
import org.lokra.seaweedfs.util.RequestPathStrategy;

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
    private SystemClusterStatus systemClusterStatus;
    private SystemTopologyStatus systemTopologyStatus;
    private PollClusterStatusThread pollClusterStatusThread;
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
    public SystemConnection(String leaderUrl, int timeout, int pollCycle, int maxConnection, int maxConnectionsPreRoute)
            throws IOException {
        this.leaderUrl = leaderUrl;
        this.pollCycle = pollCycle;
        this.pollClusterStatusThread = new PollClusterStatusThread();
        this.idleConnectionMonitorThread = new IdleConnectionMonitorThread();
        this.clientConnectionManager = new PoolingHttpClientConnectionManager();
        this.clientConnectionManager.setMaxTotal(maxConnection);
        this.clientConnectionManager.setDefaultMaxPerRoute(maxConnectionsPreRoute);
        final RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(timeout)
                .build();
        this.httpClient = HttpClients.custom()
                .setConnectionManager(this.clientConnectionManager)
                .setDefaultRequestConfig(requestConfig)
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
    @SuppressWarnings("unused")
    public SystemClusterStatus getSystemClusterStatus() {
        return systemClusterStatus;
    }

    /**
     * Get cluster topology status.
     *
     * @return core topology status.
     */
    @SuppressWarnings("unused")
    public SystemTopologyStatus getSystemTopologyStatus() {
        return systemTopologyStatus;
    }

    /**
     * Check volume server status
     *
     * @param volumeUrl
     * @return
     * @throws IOException
     */
    @SuppressWarnings({"unused", "unchecked"})
    public VolumeStatus getVolumeStatus(String volumeUrl) throws IOException {
        HttpGet request = new HttpGet(volumeUrl + RequestPathStrategy.checkVolumeStatus);
        JsonResponse jsonResponse = fetchJsonResultByRequest(request);
        VolumeStatus volumeStatus = objectMapper.readValue(
                jsonResponse.json.replace("{}", "null"), VolumeStatus.class);
        volumeStatus.setUrl(volumeUrl);
        return volumeStatus;
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
    String getLeaderUrl() {
        return this.leaderUrl;
    }

    /**
     * Fetch http API json result.
     *
     * @param request
     * @return
     * @throws IOException
     */
    JsonResponse fetchJsonResultByRequest(HttpRequestBase request) throws IOException {
        CloseableHttpResponse response = null;
        request.setHeader("Connection", "close");
        JsonResponse jsonResponse = null;

        try {
            response = httpClient.execute(request, HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            jsonResponse = new JsonResponse(EntityUtils.toString(entity), response.getStatusLine().getStatusCode());
            EntityUtils.consume(entity);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ignored) {
                }
            }
            request.releaseConnection();
        }

        if (jsonResponse.json.contains("\"error\":\"")) {
            Map map = objectMapper.readValue(jsonResponse.json, Map.class);
            final String errorMsg = (String) map.get("error");
            if (errorMsg != null)
                throw new SeaweedfsException(errorMsg);
        }

        return jsonResponse;
    }

    /**
     * Fetch http API status code.
     *
     * @param request Only http method head.
     * @return
     * @throws IOException
     */
    int fetchStatusCodeByRequest(HttpHead request) throws IOException {
        CloseableHttpResponse response = null;
        request.setHeader("Connection", "close");
        int statusCode;
        try {
            response = httpClient.execute(request, HttpClientContext.create());
            statusCode = response.getStatusLine().getStatusCode();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ignored) {
                }
            }
            request.releaseConnection();
        }
        return statusCode;
    }

    /**
     * Fetch http API input stream cache.
     *
     * @param request
     * @return
     * @throws IOException
     */
    StreamCache fetchStreamCacheByRequest(HttpRequestBase request) throws IOException {
        CloseableHttpResponse response = null;
        request.setHeader("Connection", "close");
        StreamCache cache;

        try {
            response = httpClient.execute(request, HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            cache = new StreamCache(entity.getContent(), response.getStatusLine().getStatusCode());
            EntityUtils.consume(entity);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ignored) {
                }
            }
            request.releaseConnection();
        }
        return cache;
    }

    /**
     * Fetch http API hearers with status code(in array).
     *
     * @param request
     * @return
     * @throws IOException
     */
    HeaderCache fetchHeaderByRequest(HttpHead request) throws IOException {
        CloseableHttpResponse response = null;
        request.setHeader("Connection", "close");
        HeaderCache headerCache;

        try {
            response = httpClient.execute(request, HttpClientContext.create());
            headerCache = new HeaderCache(response.getAllHeaders(), response.getStatusLine().getStatusCode());
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ignored) {
                }
            }
            request.releaseConnection();
        }
        return headerCache;
    }

    /**
     * Get closeable http client from pool.
     *
     * @return Closeable http client.
     */
    protected CloseableHttpClient getCloseableHttpClientFromPool() {
        return this.httpClient;
    }

    /**
     * Fetch core server by seaweedfs Http API.
     *
     * @param masterUrl core server url with scheme
     * @return Return the cluster status
     */
    @SuppressWarnings("unchecked")
    private SystemClusterStatus fetchSystemClusterStatus(String masterUrl) throws IOException {
        MasterStatus leader;
        ArrayList<MasterStatus> peers;
        final HttpGet request = new HttpGet(masterUrl + RequestPathStrategy.checkClusterStatus);
        final JsonResponse jsonResponse = fetchJsonResultByRequest(request);
        Map map = objectMapper.readValue(jsonResponse.json, Map.class);

        if (map.get("Leader") != null) {
            leader = new MasterStatus((String) map.get("Leader"));
        } else {
            throw new SeaweedfsException("not found seaweedfs core leader");
        }

        peers = new ArrayList<MasterStatus>();

        if (map.get("Peers") != null) {
            List<String> rawPeerList = (List<String>) map.get("Peers");
            for (String url : rawPeerList) {
                MasterStatus peer = new MasterStatus(url);
                peers.add(peer);
            }
        }

        if (map.get("IsLeader") == null || !((Boolean) map.get("IsLeader"))) {
            peers.add(new MasterStatus(masterUrl.replace("http://", "")));
            peers.remove(leader);
            leader.setActive(
                    ConnectionUtil.checkUriAlive(this.httpClient, leader.getUrl()));
            if (!leader.isActive())
                throw new SeaweedfsException("seaweedfs core leader is failover");
        } else {
            leader.setActive(true);
        }

        for (MasterStatus item : peers) {
            item.setActive(ConnectionUtil.checkUriAlive(this.httpClient, item.getUrl()));
        }

        return new SystemClusterStatus(leader, peers);

    }

    /**
     * Find leader core server from peer core server info.
     *
     * @param peers peers core server
     * @return If not found the leader, result is null.
     */
    private String findLeaderUriByPeers(List<MasterStatus> peers) throws IOException {
        if (peers == null || peers.size() == 0)
            return null;
        else {
            String result;
            for (MasterStatus item : peers) {
                final HttpGet request = new HttpGet(item.getUrl() + RequestPathStrategy.checkClusterStatus);
                Map responseMap;
                try {
                    final JsonResponse jsonResponse = fetchJsonResultByRequest(request);
                    responseMap = objectMapper.readValue(jsonResponse.json, Map.class);
                } catch (IOException e) {
                    continue;
                }
                if (responseMap.get("Leader") != null) {
                    result = ConnectionUtil.convertUrlWithScheme((String) responseMap.get("Leader"));

                    if (ConnectionUtil.checkUriAlive(this.httpClient, result))
                        return result;
                }
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
        final HttpGet request = new HttpGet(masterUrl + RequestPathStrategy.checkTopologyStatus);
        final JsonResponse jsonResponse = fetchJsonResultByRequest(request);
        Map map = objectMapper.readValue(jsonResponse.json, Map.class);

        // Fetch data center from json
        List<DataCenter> dataCenters = new ArrayList<DataCenter>();
        ArrayList<Map<String, Object>> rawDcs =
                ((ArrayList<Map<String, Object>>) ((Map) (map.get("Topology"))).get("DataCenters"));
        if (rawDcs != null)
            for (Map<String, Object> rawDc : rawDcs) {
                DataCenter dc = new DataCenter();
                dc.setFree((Integer) rawDc.get("Free"));
                dc.setId((String) rawDc.get("Id"));
                dc.setMax((Integer) rawDc.get("Max"));

                List<Rack> racks = new ArrayList<Rack>();
                ArrayList<Map<String, Object>> rawRks =
                        ((ArrayList<Map<String, Object>>) (rawDc.get("Racks")));
                if (rawRks != null)
                    for (Map<String, Object> rawRk : rawRks) {
                        Rack rk = new Rack();
                        rk.setMax((Integer) rawRk.get("Max"));
                        rk.setId((String) rawRk.get("Id"));
                        rk.setFree((Integer) rawRk.get("Free"));

                        List<DataNode> dataNodes = new ArrayList<DataNode>();
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
                                dataNodes.add(dn);
                            }
                        rk.setDataNodes(dataNodes);
                        racks.add(rk);
                    }
                dc.setRacks(racks);
                dataCenters.add(dc);
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
                        // Close free connection
                        clientConnectionManager.closeExpiredConnections();
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
