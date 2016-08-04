package org.lokra.seaweedfs.connection;

import org.junit.Before;
import org.junit.Test;
import org.lokra.seaweedfs.SeaweedfsConnectionManager;
import org.lokra.seaweedfs.test.TestUtil;

import java.io.IOException;

/**
 * @author Chiho Sin
 */
public class SeaweedfsConnectionManagerTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void startup() throws Exception {
        TestUtil.startup();
        Thread.sleep(16000);
    }

    @Test
    public void shutdown() throws Exception {
        TestUtil.shutdown();
        Thread.sleep(6000);
    }

}