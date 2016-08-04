package org.lokra.seaweedfs.core.master;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.util.EntityUtils;
import org.lokra.seaweedfs.core.SystemConnection;
import org.lokra.seaweedfs.core.contect.*;
import org.lokra.seaweedfs.exception.SeaweedfsException;
import org.lokra.seaweedfs.util.ServerApiStrategy;

import java.io.IOException;
import java.util.Map;

/**
 * @author Chiho Sin
 */
public class MasterWrapper {

    private static final Log log = LogFactory.getLog(MasterWrapper.class);

    private SystemConnection connection;
    private ObjectMapper objectMapper = new ObjectMapper();

    public MasterWrapper(SystemConnection connection) {
        this.connection = connection;
    }

    /**
     * Assign a file key.
     *
     * @param params
     * @return
     * @throws IOException
     */
    public AssignFileKeyResult assignFileKey(AssignFileKeyParams params) throws IOException {
        final String url = connection.getLeaderUrl() + ServerApiStrategy.assignFileKey + params.toUrlParams();
        HttpGet request = new HttpGet(url);
        String json = fetchJsonResultByRequest(request);
        return objectMapper.readValue(json, AssignFileKeyResult.class);
    }

    /**
     * Force garbage collection.
     *
     * @param params
     * @throws IOException
     */
    public void forceGarbageCollection(ForceGarbageCollectionParams params) throws IOException {
        final String url = connection.getLeaderUrl() + ServerApiStrategy.forceGarbageCollection + params.toUrlParams();
        HttpGet request = new HttpGet(url);
        fetchJsonResultByRequest(request);
    }

    /**
     * Pre-Allocate volumes.
     *
     * @param params
     * @return
     * @throws IOException
     */
    public PreAllocateVolumesResult preAllocateVolumes(PreAllocateVolumesParams params) throws IOException {
        final String url = connection.getLeaderUrl() + ServerApiStrategy.preAllocateVolumes + params.toUrlParams();
        HttpGet request = new HttpGet(url);
        String json = fetchJsonResultByRequest(request);
        return objectMapper.readValue(json, PreAllocateVolumesResult.class);
    }

    /**
     * Lookup volume.
     *
     * @param params
     * @return
     * @throws IOException
     */
    public LookupVolumeResult lookupVolume(LookupVolumeParams params) throws IOException {
        final String url = connection.getLeaderUrl() + ServerApiStrategy.lookupVolume + params.toUrlParams();
        HttpGet request = new HttpGet(url);
        String json = fetchJsonResultByRequest(request);
        return objectMapper.readValue(json, LookupVolumeResult.class);
    }

    /**
     * Fetch http API json result.
     *
     * @param request
     * @return
     * @throws IOException
     */
    private String fetchJsonResultByRequest(HttpRequestBase request) throws IOException {
        checkConnection();
        CloseableHttpResponse response = null;
        request.setHeader("Connection", "close");
        String json = null;

        try {
            response = connection.getClientFromPool().execute(request, HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            json = EntityUtils.toString(entity);
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

        if (json.contains("\"error\":\"")) {
            Map map = objectMapper.readValue(json, Map.class);
            final String errorMsg = (String) map.get("error");
            if (errorMsg != null)
                throw new SeaweedfsException("seaweedfs error: " + errorMsg);
        }

        return json;
    }

    /**
     * Check connection is alive.
     *
     * @throws SeaweedfsException
     */
    private void checkConnection() throws SeaweedfsException {
        if (this.connection.isConnectionClose())
            throw new SeaweedfsException("connection is closed");
    }

}
