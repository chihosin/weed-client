package org.lokra.seaweedfs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    private int timeout = 10000;
    private int statusExpiry = 30;
    private int maxConnection = 100;
    private int idleConnectionExpiry = 30;
    private int maxConnectionsPreRoute = 1000;
    private boolean enableLookupVolumeCache = false;
    private int lookupVolumeCacheExpiry = 120;
    private int lookupVolumeCacheCount = 100;
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
                        this.timeout,
                        this.statusExpiry,
                        this.idleConnectionExpiry,
                        this.maxConnection,
                        this.maxConnectionsPreRoute,
                        this.enableLookupVolumeCache,
                        this.lookupVolumeCacheExpiry,
                        this.lookupVolumeCacheCount);
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

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
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

    public int getLookupVolumeCacheCount() {
        return lookupVolumeCacheCount;
    }

    public void setLookupVolumeCacheCount(int lookupVolumeCacheCount) {
        this.lookupVolumeCacheCount = lookupVolumeCacheCount;
    }
}
