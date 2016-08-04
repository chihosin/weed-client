package org.lokra.seaweedfs.test;

import org.lokra.seaweedfs.SeaweedfsConnectionManager;

import java.io.IOException;

/**
 * @author Chiho Sin
 */
public class TestUtil {

    public static SeaweedfsConnectionManager connectionManager;

    static {
        connectionManager = new SeaweedfsConnectionManager();
        connectionManager.setHost("0.0.0.0");
        connectionManager.setPort(9335);
        connectionManager.setPollCycle(5000);
        connectionManager.setTimeout(800);
    }

    public static void startup() throws IOException {
        connectionManager.startup();
    }

    public static void shutdown() {
        connectionManager.shutdown();
    }

}
