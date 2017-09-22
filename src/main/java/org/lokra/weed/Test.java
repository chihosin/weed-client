package org.lokra.weed;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
        WeedManager manager = new WeedManager("localhost", 9001);
        manager.start();
        try {
            for (int i = 0; i < 100; i++) {
                System.out.println(manager.getVolumeClient(3, null));
            }
        } finally {
            manager.shutdown();
        }
    }
}

