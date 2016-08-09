package org.lokra.seaweedfs.test;

import org.lokra.seaweedfs.FileSystemManager;

import java.io.IOException;

/**
 * @author Chiho Sin
 */
public class ConnectionManagerUtil {

    public static FileSystemManager connectionManager;

    static {
        connectionManager = new FileSystemManager();
        connectionManager.setHost("0.0.0.0");
        connectionManager.setPort(9333);
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
