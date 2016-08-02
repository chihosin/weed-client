package org.lokra.seaweedfs.connection;

import org.junit.Before;
import org.junit.Test;
import org.lokra.seaweedfs.SeaweedfsConnectionManager;

import java.io.IOException;

/**
 * @author Chiho Sin
 */
public class SeaweedfsConnectionManagerTest {

    private SeaweedfsConnectionManager manager;

    @Before
    public void setUp() throws Exception {
        this.manager = new SeaweedfsConnectionManager();
        this.manager.setHost("0.0.0.0");
        this.manager.setPort(9335);
        this.manager.setPollCycle(5000);
        this.manager.setTimeout(800);
    }

    @Test
    public void startup() throws Exception {
        manager.startup();
        Thread.sleep(16000);
    }

    @Test
    public void shutdown() throws Exception {
        manager.shutdown();
        Thread.sleep(6000);
    }

}