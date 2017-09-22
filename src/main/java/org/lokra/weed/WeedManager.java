package org.lokra.weed;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.okhttp.OkHttpClient;
import org.lokra.weed.content.Cluster;
import org.lokra.weed.content.Locations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class WeedManager {
    private final Logger log = LoggerFactory.getLogger(WeedManager.class);
    private ConcurrentMap<String, WeedVolumeClient> volumeClients = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Set<String>> volumeCollectionIds = new ConcurrentHashMap<>();
    private ConcurrentMap<String, WeedMasterClient> peerMasters = new ConcurrentHashMap<>();
    private final MasterHealthCheckThread masterHealthCheckThread = new MasterHealthCheckThread();
    private final VolumeHealthCheckThread volumeHealthCheckThread = new VolumeHealthCheckThread();
    private String leaderMasterUrl;
    private WeedMasterClient leaderMaster;
    private String host;
    private int port;

    public WeedManager() {
    }

    /**
     * Construction.
     *
     * @param host Master server host.
     * @param port Master server port.
     */
    public WeedManager(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Startup weed manager.
     */
    public void start() {
        leaderMasterUrl = assembleUrl(host, port);
        leaderMaster = Feign.builder()
                .client(new OkHttpClient())
                .decoder(new JacksonDecoder())
                .target(WeedMasterClient.class, String.format("http://%s", leaderMasterUrl));

        fetchMasters(leaderMaster.cluster());

        masterHealthCheckThread.start();
        volumeHealthCheckThread.start();
    }

    /**
     * Shutdown weed manager.
     */
    public void shutdown() {
        masterHealthCheckThread.interrupt();
        volumeHealthCheckThread.interrupt();
    }

    /**
     * Get master server client.
     *
     * @return client.
     */
    public WeedMasterClient getMasterClient() {
        return this.leaderMaster;
    }

    /**
     * Get volume server client.
     *
     * @return client.
     */
    public WeedVolumeClient getVolumeClient(int volumeId, String collection) {

        Set<String> volumeIds =
                volumeCollectionIds.get(String.format("%d#%s", volumeId, collection != null ? collection : ""));

        if (volumeIds == null || volumeIds.size() == 0)
            volumeIds = fetchVolume(volumeId, collection);

        final long millis = System.currentTimeMillis();
        final int index = (int) (millis % volumeIds.size());

        final String[] ids = new String[volumeIds.size()];
        return volumeClients.get(volumeIds.toArray(ids)[index]);
    }

    /**
     * Fetch volume server.
     *
     * @param volumeId   Volume id.
     * @param collection Collection.
     * @return ids.
     */
    private Set<String> fetchVolume(int volumeId, String collection) {
        Locations locations = getMasterClient().lookup(volumeId, collection);
        Set<String> ids = new HashSet<>();
        locations.getLocations().forEach(location -> {
            final String id = String.format("%s#%s", location.getUrl(), location.getUrl());
            ids.add(id);
            volumeClients.putIfAbsent(id, Feign.builder()
                    .client(new OkHttpClient())
                    .decoder(new JacksonDecoder())
                    .target(WeedVolumeClient.class, String.format("http://%s", location.getPublicUrl())));
        });
        return ids;
    }

    /**
     * Master server host.
     *
     * @return host.
     */
    public String getHost() {
        return host;
    }

    /**
     * Master server port.
     *
     * @return port.
     */
    public int getPort() {
        return port;
    }

    /**
     * Assemble URL.
     *
     * @param host Host.
     * @param port Port.
     * @return URL.
     */
    private String assembleUrl(String host, int port) {
        return String.format("%s:%d", host, port);
    }

    /**
     * Fetch master cluster information.
     *
     * @param cluster Master cluster information.
     */
    private void fetchMasters(Cluster cluster) {
        if (!leaderMasterUrl.equals(cluster.getLeader())) {
            leaderMasterUrl = cluster.getLeader();
            log.info("Weed leader master server is change to [{}]", leaderMasterUrl);
            leaderMaster = Feign.builder()
                    .client(new OkHttpClient())
                    .decoder(new JacksonDecoder())
                    .target(WeedMasterClient.class, String.format("http://%s", leaderMaster));
        }

        // Cleanup peer master
        Set<String> removeSet = peerMasters.keySet().stream().filter(
                key -> !cluster.getPeers().contains(key) && !cluster.getLeader().equals(key))
                .collect(Collectors.toSet());

        peerMasters.remove(leaderMasterUrl);
        removeSet.forEach(key -> peerMasters.remove(key));

        cluster.getPeers().forEach(url -> {
            if (!peerMasters.containsKey(url)) {
                peerMasters.put(url,
                        Feign.builder()
                                .client(new OkHttpClient())
                                .decoder(new JacksonDecoder())
                                .target(WeedMasterClient.class, String.format("http://%s", url)));
            }
        });
    }

    /**
     * Master server health check thread
     */
    class MasterHealthCheckThread extends Thread {

        @Override
        public void run() {
            log.info("Started weed leader master server health check");
            //noinspection InfiniteLoopStatement
            while (true) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    log.info("Stop weed leader master server health check");
                    return;
                }
                try {
                    fetchMasters(leaderMaster.cluster());
                } catch (Exception e) {
                    log.warn("Weed leader master server [{}] is down", leaderMasterUrl);
                    for (String url : peerMasters.keySet()) {
                        try {
                            fetchMasters(peerMasters.get(url).cluster());
                        } catch (Exception ignored) {
                            log.warn("Weed peer master server [{}] is down", url);
                            continue;
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * Volume server health check thread
     */
    class VolumeHealthCheckThread extends Thread {
        @Override
        public void run() {
            log.info("Started weed leader master server health check");
            //noinspection InfiniteLoopStatement
            while (true) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    log.info("Stop weed leader master server health check");
                    return;
                }
                try {
                    fetchMasters(leaderMaster.cluster());
                } catch (Exception e) {
                    log.warn("Weed leader master server [{}] is down", leaderMasterUrl);
                    for (String url : peerMasters.keySet()) {
                        try {
                            fetchMasters(peerMasters.get(url).cluster());
                        } catch (Exception ignored) {
                            log.warn("Weed peer master server [{}] is down", url);
                            continue;
                        }
                        break;
                    }
                }
            }
        }
    }
}
