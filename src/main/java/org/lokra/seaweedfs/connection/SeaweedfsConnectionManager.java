package org.lokra.seaweedfs.connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;

/**
 * Seaweedfs connection factory
 *
 * @author Chiho Sin
 */
public class SeaweedfsConnectionManager implements InitializingBean, DisposableBean {

    private static final Log log = LogFactory.getLog(SeaweedfsConnectionManager.class);

    private String host = "localhost";
    private int port = 9333;
    private int timeout = 10000;
    private int pollCycle = 30000;


    private MasterConnection masterConnection;
    private VolumeConnection volumeConnection;

    public SeaweedfsConnectionManager() {
    }

    public MasterConnection getMasterConnection() {
        return masterConnection;
    }

    public VolumeConnection getVolumeConnection() {
        return volumeConnection;
    }

    /**
     * Start up the connection to the Seaweedfs server
     */
    public void startup() throws IOException {
        log.info("start connect to the seaweedfs master server [" +
                ConnectionUtil.convertUrlWithScheme(host + ":" + port) + "]");
        if (this.masterConnection == null) {
            this.masterConnection = new MasterConnection(
                    ConnectionUtil.convertUrlWithScheme(host + ":" + port),
                    this.timeout,
                    this.pollCycle);
        }
        this.masterConnection.startup();

    }

    /**
     * Shutdown connect to the any Seaweedfs server
     */
    public void shutdown() {
        log.info("stop connect to the seaweedfs master server");
        if (this.masterConnection != null)
            this.masterConnection.stop();
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
}
