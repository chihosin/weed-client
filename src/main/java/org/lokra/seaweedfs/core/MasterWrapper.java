package org.lokra.seaweedfs.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.HttpGet;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.lokra.seaweedfs.core.contect.*;
import org.lokra.seaweedfs.exception.SeaweedfsException;
import org.lokra.seaweedfs.util.RequestPathStrategy;

import java.io.IOException;

/**
 * @author Chiho Sin
 */
public class MasterWrapper {

    private static final Log log = LogFactory.getLog(MasterWrapper.class);

    private SystemConnection connection;
    private Cache<Long, LookupVolumeResult> lookupVolumeCache;
    private ObjectMapper objectMapper = new ObjectMapper();

    public MasterWrapper(SystemConnection connection) {
        this.connection = connection;
        final CacheManager cacheManager = connection.getCacheManager();
        if (cacheManager != null)
            this.lookupVolumeCache =
                    cacheManager.getCache(SystemConnection.LOOKUP_VOLUME_CACHE_ALIAS,
                            Long.class, LookupVolumeResult.class);
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
        final String url = connection.getLeaderUrl() + RequestPathStrategy.assignFileKey + params.toUrlParams();
        HttpGet request = new HttpGet(url);
        JsonResponse jsonResponse = connection.fetchJsonResultByRequest(request);
        return objectMapper.readValue(jsonResponse.json, AssignFileKeyResult.class);
    }

    /**
     * Force garbage collection.
     *
     * @param params
     * @throws IOException
     */
    public void forceGarbageCollection(ForceGarbageCollectionParams params) throws IOException {
        checkConnection();
        final String url = connection.getLeaderUrl() + RequestPathStrategy.forceGarbageCollection + params.toUrlParams();
        HttpGet request = new HttpGet(url);
        connection.fetchJsonResultByRequest(request);
    }

    /**
     * Pre-Allocate volumes.
     *
     * @param params
     * @return
     * @throws IOException
     */
    public PreAllocateVolumesResult preAllocateVolumes(PreAllocateVolumesParams params) throws IOException {
        checkConnection();
        final String url = connection.getLeaderUrl() + RequestPathStrategy.preAllocateVolumes + params.toUrlParams();
        HttpGet request = new HttpGet(url);
        JsonResponse jsonResponse = connection.fetchJsonResultByRequest(request);
        return objectMapper.readValue(jsonResponse.json, PreAllocateVolumesResult.class);
    }

    /**
     * Lookup volume.
     *
     * @param params
     * @return
     * @throws IOException
     */
    public LookupVolumeResult lookupVolume(LookupVolumeParams params) throws IOException {
        checkConnection();
        LookupVolumeResult result;
        if (this.lookupVolumeCache != null) {
            result = this.lookupVolumeCache.get(params.getVolumeId());
            if (result != null) {
                return result;
            } else {
                result = fetchLookupVolumeResult(params);
                this.lookupVolumeCache.put(params.getVolumeId(), result);
                return result;
            }
        } else {
            return fetchLookupVolumeResult(params);
        }
    }

    /**
     * Fetch lookup volume result.
     *
     * @param params
     * @return
     * @throws IOException
     */
    private LookupVolumeResult fetchLookupVolumeResult(LookupVolumeParams params) throws IOException {
        checkConnection();
        final String url = connection.getLeaderUrl() + RequestPathStrategy.lookupVolume + params.toUrlParams();
        HttpGet request = new HttpGet(url);
        JsonResponse jsonResponse = connection.fetchJsonResultByRequest(request);
        return objectMapper.readValue(jsonResponse.json, LookupVolumeResult.class);
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
