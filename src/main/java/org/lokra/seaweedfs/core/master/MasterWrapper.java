package org.lokra.seaweedfs.core.master;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.util.EntityUtils;
import org.lokra.seaweedfs.core.SystemConnection;
import org.lokra.seaweedfs.core.contect.AssignFileKeyParams;
import org.lokra.seaweedfs.core.contect.AssignFileKeyResult;
import org.lokra.seaweedfs.core.contect.ForceGarbageCollectionParams;
import org.lokra.seaweedfs.exception.SeaweedfsException;
import org.lokra.seaweedfs.util.HttpApiStrategy;

import java.io.IOException;

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
        checkConnection();
        CloseableHttpResponse response = null;
        final String url = connection.getLeaderUrl() + HttpApiStrategy.assignFileKey + params.toUrlParams();
        HttpGet request = new HttpGet(url);
        request.setHeader("Connection", "close");
        try {
            response = connection.getClientFromPool().execute(request, HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            final String json = EntityUtils.toString(entity);
            AssignFileKeyResult result = objectMapper.readValue(json, AssignFileKeyResult.class);
            EntityUtils.consume(entity);
            return result;
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ignored) {
                }
            }
            request.releaseConnection();
        }
    }

    /**
     * Force garbage collection.
     *
     * @param params
     * @throws IOException
     */
    public void forceGarbageCollection(ForceGarbageCollectionParams params) throws IOException {
        checkConnection();
        CloseableHttpResponse response = null;
        final String url = connection.getLeaderUrl() + HttpApiStrategy.forceGarbageCollection + params.toUrlParams();
        HttpGet request = new HttpGet(url);
        request.setHeader("Connection", "close");
        try {
            response = connection.getClientFromPool().execute(request, HttpClientContext.create());
            EntityUtils.consume(response.getEntity());
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ignored) {
                }
            }
            request.releaseConnection();
        }
    }

    private void checkConnection() throws SeaweedfsException {
        if (this.connection.isConnectionClose())
            throw new SeaweedfsException("connection is closed");
    }

}
