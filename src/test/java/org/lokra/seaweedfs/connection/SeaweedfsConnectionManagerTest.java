package org.lokra.seaweedfs.connection;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Chiho Sin
 */
public class SeaweedfsConnectionManagerTest {

    private SeaweedfsConnectionManager manager;

    @Before
    public void setUp() throws Exception {
        this.manager = new SeaweedfsConnectionManager();
        this.manager.setHost("192.168.7.250");
        this.manager.setPort(9333);
        this.manager.setPollCycle(6000);
        this.manager.setTimeout(800);
    }

    @Test
    public void startup() throws Exception {
        manager.startup();
        Thread.sleep(1000000000);
    }

    @Test
    public void shutdown() throws Exception {
        manager.shutdown();

    }

}