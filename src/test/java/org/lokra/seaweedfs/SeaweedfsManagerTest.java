package org.lokra.seaweedfs;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

/**
 * Created by ChihoSin on 16/7/22.
 */
public class SeaweedfsManagerTest {

    private SeaweedfsManager seaweedfsManager;

    @Before
    public void setUp() throws Exception {
        this.seaweedfsManager = new SeaweedfsManager("http", "192.168.199.240", 9334);
        this.seaweedfsManager.setServerConnectTimeout(500);
    }

    @Test
    public void getClusterStatus() throws Exception {
        seaweedfsManager.start();
        System.out.println(seaweedfsManager.getClusterStatus());
    }

}