package org.lokra.seaweedfs.core;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lokra.seaweedfs.SeaweedfsConnectionManager;
import org.lokra.seaweedfs.core.contect.AssignFileKeyParams;
import org.lokra.seaweedfs.core.contect.AssignFileKeyResult;
import org.lokra.seaweedfs.test.ConnectionManagerUtil;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.*;

/**
 * @author Chiho Sin
 */
public class VolumeWrapperTest {

    @Test
    public void deleteFile() throws Exception {
        File file =
                new File(getClass().getClassLoader().getResource("test_upload_file.txt").toURI());
        AssignFileKeyParams params = new AssignFileKeyParams();
        AssignFileKeyResult result = masterWrapper.assignFileKey(params);

        volumeWrapper.uploadFile(
                result.getUrl(),
                result.getFid(),
                file.getName(),
                new FileInputStream(file));

        Assert.assertTrue(volumeWrapper.deleteFile(result.getUrl(), result.getFid()) > 0L);
    }

    @Test
    public void uploadFile() throws Exception {
        File file =
                new File(getClass().getClassLoader().getResource("test_upload_file.txt").toURI());

        AssignFileKeyParams params = new AssignFileKeyParams();
        AssignFileKeyResult result = masterWrapper.assignFileKey(params);

        Assert.assertTrue(
                volumeWrapper.uploadFile(
                        result.getUrl(), result.getFid(), file.getName(), new FileInputStream(file)) > 0L);
    }

    private static MasterWrapper masterWrapper;
    private static VolumeWrapper volumeWrapper;

    @BeforeClass
    public static void setBeforeClass() throws Exception {
        ConnectionManagerUtil.startup();
        Thread.sleep(1000);
        SeaweedfsConnectionManager manager = ConnectionManagerUtil.connectionManager;
        volumeWrapper = new VolumeWrapper(manager.getSystemConnection());
        masterWrapper = new MasterWrapper(manager.getSystemConnection());
    }

}