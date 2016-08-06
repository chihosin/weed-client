package org.lokra.seaweedfs.core;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lokra.seaweedfs.SeaweedfsConnectionManager;
import org.lokra.seaweedfs.core.topology.DataCenter;
import org.lokra.seaweedfs.test.ConnectionManagerUtil;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Chiho Sin
 */
public class SystemConnectionTest {

    @Test
    public void getSystemClusterStatus() throws Exception {
        Assert.assertTrue(connection.getSystemClusterStatus().getLeader().isActive());
    }

    @Test
    public void getSystemTopologyStatus() throws Exception {
        Assert.assertTrue(connection.getSystemTopologyStatus().getMax() > 0);
    }

    @Test
    public void isConnectionClose() throws Exception {
        Assert.assertFalse(connection.isConnectionClose());
    }

    @Test
    public void getLeaderUrl() throws Exception {
        Assert.assertNotNull(connection.getLeaderUrl());
    }

    @Test
    public void getVolumeStatus() throws Exception {
        String dataNodeUrl =
                connection.getSystemTopologyStatus()
                        .getDataCenters().get(0).getRacks().get(0).getDataNodes().get(0).getUrl();
        Assert.assertEquals(dataNodeUrl,
                connection.getVolumeStatus(dataNodeUrl).getUrl());
    }

    private static SystemConnection connection;

    @BeforeClass
    public static void setBeforeClass() throws Exception {
        ConnectionManagerUtil.startup();
        Thread.sleep(1000);
        connection = ConnectionManagerUtil.connectionManager.getSystemConnection();
    }

}