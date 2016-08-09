package org.lokra.seaweedfs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lokra.seaweedfs.core.MasterWrapper;
import org.lokra.seaweedfs.core.SystemConnection;
import org.lokra.seaweedfs.core.VolumeWrapper;
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
    private int pollCycle = 30000;
    private int maxConnection = 300;
    private int maxConnectionsPreRoute = 3000;
    private boolean startup = false;

    private SystemConnection systemConnection;
    private MasterWrapper masterWrapper;
    private VolumeWrapper volumeWrapper;

    public FileSystemManager() {
    }

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
                        this.pollCycle,
                        this.maxConnection,
                        this.maxConnectionsPreRoute);
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

    public int getPollCycle() {
        return pollCycle;
    }

    public void setPollCycle(int pollCycle) {
        this.pollCycle = pollCycle;
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
}
