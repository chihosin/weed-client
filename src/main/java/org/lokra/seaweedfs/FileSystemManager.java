/*
 * Copyright (c) 2016 Lokra Platform
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

package org.lokra.seaweedfs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.cache.HttpCacheStorage;
import org.lokra.seaweedfs.core.SystemConnection;
import org.lokra.seaweedfs.util.ConnectionUtil;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;

/**
 * Seaweedfs connection factory
 *
 * @author Chiho Sin
 */
public class FileSystemManager implements InitializingBean, DisposableBean {

    private static final Log log = LogFactory.getLog(FileSystemManager.class);

    private String host = "localhost";
    private int port = 9333;
    private int connectionTimeout = 10000;
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
    private boolean startup = false;

    private SystemConnection systemConnection;

    public SystemConnection getSystemConnection() {
        return systemConnection;
    }

    /**
     * Start up the connection to the Seaweedfs server
     */
    public void startup() throws IOException {
        if (this.startup) {
            log.info("connect is already startup");
        } else {
            log.info("start connect to the seaweedfs core server [" +
                    ConnectionUtil.convertUrlWithScheme(host + ":" + port) + "]");
            if (this.systemConnection == null) {
                this.systemConnection = new SystemConnection(
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
            this.systemConnection.startup();
            this.startup = true;
        }
    }

    /**
     * Shutdown connect to the any Seaweedfs server
     */
    public void shutdown() {
        log.info("stop connect to the seaweedfs core server");
        if (this.systemConnection != null)
            this.systemConnection.stop();
    }

    /**
     * Working with Spring framework startup
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        startup();
    }

    /**
     * Using when the Spring framework is destroy
     *
     * @throws Exception
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
