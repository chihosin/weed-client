/*
 * Copyright (c) 2016 Lokra Studio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.lokra.seaweedfs.core;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lokra.seaweedfs.FileSystemTest;

/**
 * @author Chiho Sin
 */
public class connectionTest {

    private static Connection Connection;

    @BeforeClass
    public static void setBeforeClass() throws Exception {
        FileSystemTest.startup();
        Thread.sleep(1000);
        Connection = FileSystemTest.fileSource.getConnection();
    }

    @Test
    public void getSystemClusterStatus() throws Exception {
        Assert.assertTrue(Connection.getSystemClusterStatus().getLeader().isActive());
    }

    @Test
    public void getSystemTopologyStatus() throws Exception {
        Assert.assertTrue(Connection.getSystemTopologyStatus().getMax() > 0);
    }

    @Test
    public void isConnectionClose() throws Exception {
        Assert.assertFalse(Connection.isConnectionClose());
    }

    @Test
    public void getLeaderUrl() throws Exception {
        Assert.assertNotNull(Connection.getLeaderUrl());
    }

    @Test
    public void getVolumeStatus() throws Exception {
        String dataNodeUrl =
                Connection.getSystemTopologyStatus()
                        .getDataCenters().get(0).getRacks().get(0).getDataNodes().get(0).getUrl();
        Assert.assertEquals(dataNodeUrl,
                Connection.getVolumeStatus(dataNodeUrl).getUrl());
    }

}