package org.lokra.seaweedfs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.internal.NotNull;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.lokra.seaweedfs.util.SeaweedfsHttpApiStrategy;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;


/**
 * Created by ChihoSin on 16/7/22.
 */
public class SeaweedfsManager {

    private URI leaderUri;
    private ClusterStatus clusterStatus;
    private String uriScheme;
    private int serverConnectTimeout;
    private RequestConfig defaultRequestConfig;
    private ObjectMapper objectMapper = new ObjectMapper();

    public SeaweedfsManager(@NotNull String scheme, @NotNull String host, int port)
            throws URISyntaxException, IOException {
        this.defaultRequestConfig = RequestConfig.DEFAULT;
        this.uriScheme = scheme;
        this.leaderUri = new URIBuilder()
                .setScheme(scheme)
                .setHost(host)
                .setPort(port)
                .build();
    }

    public void start() throws IOException, URISyntaxException {
        this.clusterStatus = fetchClusterStatus(leaderUri);
    }

    private ClusterStatus fetchClusterStatus(URI masterUri) throws URISyntaxException, IOException {

        MasterServer leader;
        ConcurrentHashMap<URI, MasterServer> peers;

        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            final HttpGet request = new HttpGet(new URIBuilder(masterUri)
                    .setPath(SeaweedfsHttpApiStrategy.ClusterStatus)
                    .build());
            request.setConfig(defaultRequestConfig);
            final ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpClient.execute(request, responseHandler);
            Map map = objectMapper.readValue(response, Map.class);

            if (map.get("Leader") != null) {
                String urlString[] = ((String) map.get("Leader")).split(":");
                leader = new MasterServer(new URIBuilder()
                        .setScheme(this.uriScheme)
                        .setHost(urlString[0])
                        .setPort(Integer.parseInt(urlString[1]))
                        .build());
            } else {
                throw new SeaweedfsException("not found master server leader");
            }

            peers = new ConcurrentHashMap<URI, MasterServer>();

            if (map.get("Peers") != null) {
                List<String> rawPeerList = (List<String>) map.get("Peers");
                for (String url : rawPeerList) {
                    String urlString[] = url.split(":");
                    MasterServer peer = new MasterServer(new URIBuilder()
                            .setScheme(this.uriScheme)
                            .setHost(urlString[0])
                            .setPort(Integer.parseInt(urlString[1]))
                            .build());
                    peers.put(peer.getUri(), peer);
                }
            }

            if (map.get("IsLeader") == null || ((Boolean) map.get("IsLeader")) == false) {
                peers.put(masterUri, new MasterServer(masterUri));
                peers.remove(leader.getUri());
                leader.setActive(checkUriLive(httpClient, leader.getUri()));
                if (!leader.isActive())
                    throw new SeaweedfsException("master server leader is down");
            } else {
                leader.setActive(true);
            }

            for (URI uri : peers.keySet()) {
                peers.get(uri).setActive(checkUriLive(httpClient, uri));
            }

            return new ClusterStatus(leader, peers);

        } finally {
            httpClient.close();
        }

    }

    private boolean checkUriLive(HttpClient httpClient, URI uri)
            throws URISyntaxException {
        try {
            final HttpGet peerRequest = new HttpGet(uri);
            peerRequest.setConfig(this.defaultRequestConfig);
            final HttpResponse response = httpClient.execute(peerRequest);
            return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
        } catch (IOException e) {
            return false;
        }
    }


    public int getServerConnectTimeout() {
        return serverConnectTimeout;
    }

    public void setServerConnectTimeout(int serverConnectTimeout) {
        this.serverConnectTimeout = serverConnectTimeout;
        this.defaultRequestConfig = RequestConfig.custom()
                .setConnectTimeout(this.serverConnectTimeout)
                .build();
    }

    public ClusterStatus getClusterStatus() {
        return clusterStatus;
    }

}
