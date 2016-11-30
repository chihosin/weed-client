/*
 * Copyright (c) 2016 Lokra Studio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.lokra.seaweedfs.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.cache.HttpCacheStorage;
import org.lokra.seaweedfs.core.topology.SystemClusterStatus;
import org.lokra.seaweedfs.core.topology.SystemTopologyStatus;
import org.lokra.seaweedfs.core.topology.VolumeStatus;
import org.lokra.seaweedfs.exception.SeaweedfsException;
import org.lokra.seaweedfs.util.ConnectionUtil;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;

/**
 * Seaweed file system connection source.
 *
 * @author Chiho Sin
 */
public class FileSource implements InitializingBean, DisposableBean {

    private static final Log log = LogFactory.getLog(FileSource.class);

    private String host = "localhost";
    private int port = 9333;
    private int connectionTimeout = 10;
    private int statusExpiry = 30;
    private int maxConnection = 100;
    private int idleConnectionExpiry = 30;
    private int maxConnectionsPreRoute = 1000;
    private boolean enableLookupVolumeCache = true;
    private int lookupVolumeCacheExpiry = 120;
    private int lookupVolumeCacheEntries = 100;
    private boolean enableFileStreamCache = true;
    private int fileStreamCacheEntries = 1000;
    private long fileStreamCacheSize = 8192;
    private HttpCacheStorage fileStreamCacheStorage = null;
    volatile private boolean startup = false;

    private Connection connection;

    /**
     * Get wrapper connection.
     *
     * @return Wrapper connection.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Start up the connection to the Seaweedfs server
     *
     * @throws IOException Http connection is fail or server response within some error message.
     */
    public void startup() throws IOException {
        if (this.startup) {
            log.info("connect is already startup");
        } else {
            log.info("start connect to the seaweedfs master server [" +
                    ConnectionUtil.convertUrlWithScheme(host + ":" + port) + "]");
            if (this.connection == null) {
                this.connection = new Connection(
                        ConnectionUtil.convertUrlWithScheme(host + ":" + port),
                        this.connectionTimeout,
                        this.statusExpiry,
                        this.idleConnectionExpiry,
                        this.maxConnection,
                        this.maxConnectionsPreRoute,
                        this.enableLookupVolumeCache,
                        this.lookupVolumeCacheExpiry,
                        this.lookupVolumeCacheEntries,
                        this.enableFileStreamCache,
                        this.fileStreamCacheEntries,
                        this.fileStreamCacheSize,
                        this.fileStreamCacheStorage);
            }
            this.connection.startup();
            this.startup = true;
        }
    }

    /**
     * Shutdown connect to the any Seaweedfs server
     */
    public void shutdown() {
        log.info("stop connect to the seaweedfs master server");
        if (this.connection != null)
            this.connection.stop();
    }

    /**
     * Force garbage collection.
     *
     * @throws IOException Http connection is fail or server response within some error message.
     */
    public void forceGarbageCollection() throws IOException {
        connection.forceGarbageCollection();
    }

    /**
     * Force garbage collection.
     *
     * @param garbageThreshold Garbage threshold.
     * @throws IOException Http connection is fail or server response within some error message.
     */
    public void forceGarbageCollection(float garbageThreshold) throws IOException {
        connection.forceGarbageCollection(garbageThreshold);
    }

    /**
     * Pre-allocate volumes.
     *
     * @param sameRackCount       Same rack count.
     * @param diffRackCount       Different rack count.
     * @param diffDataCenterCount Different data center count.
     * @param count               Count.
     * @param dataCenter          Data center.
     * @param ttl                 Time to live.
     * @throws IOException IOException Http connection is fail or server response within some error message.
     */
    public void preAllocateVolumes(int sameRackCount, int diffRackCount, int diffDataCenterCount, int count, String dataCenter,
                                   String ttl) throws IOException {
        connection.preAllocateVolumes(sameRackCount, diffRackCount, diffDataCenterCount, count, dataCenter, ttl);
    }

    /**
     * Get master server  cluster status.
     *
     * @return Core cluster status.
     * @throws SeaweedfsException Connection is shutdown.
     */
    public SystemClusterStatus getSystemClusterStatus() throws SeaweedfsException {
        if (startup)
            return connection.getSystemClusterStatus();
        else
            throw new SeaweedfsException("Could not fetch server cluster status at connection is shutdown");
    }

    /**
     * Get cluster topology status.
     *
     * @return Core topology status.
     * @throws SeaweedfsException Connection is shutdown.
     */
    public SystemTopologyStatus getSystemTopologyStatus() throws SeaweedfsException {
        if (startup)
            return connection.getSystemTopologyStatus();
        else
            throw new SeaweedfsException("Could not fetch server cluster status at connection is shutdown");
    }

    /**
     * Check volume server status.
     *
     * @param volumeUrl Volume server url.
     * @return Volume server status.
     * @throws IOException Connection is shutdown or
     *                     Http connection is fail or server response within some error message.
     */
    public VolumeStatus getVolumeStatus(String volumeUrl) throws IOException {
        if (startup)
            return connection.getVolumeStatus(volumeUrl);
        else
            throw new SeaweedfsException("Could not fetch server cluster status at connection is shutdown");
    }

    /**
     * Working with Spring framework startup
     *
     * @throws IOException Http connection is fail or server response within some error message.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        startup();
    }

    /**
     * Using when the Spring framework is destroy
     *
     * @throws IOException Http connection is fail or server response within some error message.
     */
    @Override
    public void destroy() throws Exception {
        shutdown();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getStatusExpiry() {
        return statusExpiry;
    }

    public void setStatusExpiry(int statusExpiry) {
        this.statusExpiry = statusExpiry;
    }

    public int getMaxConnection() {
        return maxConnection;
    }

    public void setMaxConnection(int maxConnection) {
        this.maxConnection = maxConnection;
    }

    public int getMaxConnectionsPreRoute() {
        return maxConnectionsPreRoute;
    }

    public void setMaxConnectionsPreRoute(int maxConnectionsPreRoute) {
        this.maxConnectionsPreRoute = maxConnectionsPreRoute;
    }

    public boolean isEnableLookupVolumeCache() {
        return enableLookupVolumeCache;
    }

    public void setEnableLookupVolumeCache(boolean enableLookupVolumeCache) {
        this.enableLookupVolumeCache = enableLookupVolumeCache;
    }

    public int getLookupVolumeCacheExpiry() {
        return lookupVolumeCacheExpiry;
    }

    public void setLookupVolumeCacheExpiry(int lookupVolumeCacheExpiry) {
        this.lookupVolumeCacheExpiry = lookupVolumeCacheExpiry;
    }

    public int getIdleConnectionExpiry() {
        return idleConnectionExpiry;
    }

    public void setIdleConnectionExpiry(int idleConnectionExpiry) {
        this.idleConnectionExpiry = idleConnectionExpiry;
    }

    public int getLookupVolumeCacheEntries() {
        return lookupVolumeCacheEntries;
    }

    public void setLookupVolumeCacheEntries(int lookupVolumeCacheEntries) {
        this.lookupVolumeCacheEntries = lookupVolumeCacheEntries;
    }

    public boolean isEnableFileStreamCache() {
        return enableFileStreamCache;
    }

    public void setEnableFileStreamCache(boolean enableFileStreamCache) {
        this.enableFileStreamCache = enableFileStreamCache;
    }

    public int getFileStreamCacheEntries() {
        return fileStreamCacheEntries;
    }

    public void setFileStreamCacheEntries(int fileStreamCacheEntries) {
        this.fileStreamCacheEntries = fileStreamCacheEntries;
    }

    public long getFileStreamCacheSize() {
        return fileStreamCacheSize;
    }

    public void setFileStreamCacheSize(long fileStreamCacheSize) {
        this.fileStreamCacheSize = fileStreamCacheSize;
    }

    public HttpCacheStorage getFileStreamCacheStorage() {
        return fileStreamCacheStorage;
    }

    public void setFileStreamCacheStorage(HttpCacheStorage fileStreamCacheStorage) {
        this.fileStreamCacheStorage = fileStreamCacheStorage;
    }

}
