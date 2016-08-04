package org.lokra.seaweedfs.core.master;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lokra.seaweedfs.SeaweedfsConnectionManager;
import org.lokra.seaweedfs.core.contect.AssignFileKeyParams;
import org.lokra.seaweedfs.core.contect.AssignFileKeyResult;
import org.lokra.seaweedfs.test.TestUtil;

import static org.junit.Assert.*;

/**
 * @author Chiho Sin
 */
public class MasterWrapperTest {

    private SeaweedfsConnectionManager manager;

    @Before
    public void setUp() throws Exception {
        TestUtil.startup();
        Thread.sleep(1000);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void assignFileKey() throws Exception {
        SeaweedfsConnectionManager manager = TestUtil.connectionManager;
        MasterWrapper masterWrapper = new MasterWrapper(manager.getSystemConnection());
        AssignFileKeyParams params = new AssignFileKeyParams();
        params.setReplication("000");
        params.setCount(2);
        AssignFileKeyResult result = masterWrapper.assignFileKey(params);
        Assert.assertEquals(Long.parseLong(String.valueOf(params.getCount())),
                Long.parseLong(String.valueOf(result.getCount())));
    }

}