package org.lokra.seaweedfs.core;

import org.apache.http.entity.ContentType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lokra.seaweedfs.FileSystemManager;
import org.lokra.seaweedfs.cache.StreamCache;
import org.lokra.seaweedfs.core.contect.AssignFileKeyParams;
import org.lokra.seaweedfs.core.contect.AssignFileKeyResult;
import org.lokra.seaweedfs.test.ConnectionManagerUtil;

import java.io.ByteArrayInputStream;

/**
 * @author Chiho Sin
 */
public class VolumeWrapperTest {
    @Test
    public void checkFileExist() throws Exception {
        AssignFileKeyParams params = new AssignFileKeyParams();
        AssignFileKeyResult result = masterWrapper.assignFileKey(params);
        volumeWrapper.uploadFile(
                result.getUrl(),
                result.getFid(),
                "test.txt",
                new ByteArrayInputStream("@checkFileExist".getBytes()),
                null,
                ContentType.DEFAULT_BINARY);
        Assert.assertTrue(volumeWrapper.checkFileExist(result.getUrl(), result.getFid()));
    }

    @Test
    public void getFileStream() throws Exception {
        AssignFileKeyParams params = new AssignFileKeyParams();
        AssignFileKeyResult result = masterWrapper.assignFileKey(params);
        volumeWrapper.uploadFile(
                result.getUrl(),
                result.getFid(),
                "test.txt",
                new ByteArrayInputStream("@getFileContent".getBytes()),
                null,
                ContentType.DEFAULT_BINARY);

        StreamCache cache = volumeWrapper.getFileStream(result.getUrl(), result.getFid());
        Assert.assertTrue(cache.getOutputStream().toString().equals("@getFileContent"));
    }

    @Test
    public void getFileStatus() throws Exception {
        AssignFileKeyParams params = new AssignFileKeyParams();
        AssignFileKeyResult result = masterWrapper.assignFileKey(params);

        volumeWrapper.uploadFile(
                result.getUrl(),
                result.getFid(),
                "test.txt",
                new ByteArrayInputStream("@getFileStatus".getBytes()),
                null,
                ContentType.DEFAULT_BINARY);


        Assert.assertTrue(
                volumeWrapper.getFileStatus(result.getUrl(),
                        result.getFid()).getLastHeader("Content-Length").getValue().equals("38"));
    }

    @Test
    public void deleteFile() throws Exception {
        AssignFileKeyParams params = new AssignFileKeyParams();
        AssignFileKeyResult result = masterWrapper.assignFileKey(params);

        volumeWrapper.uploadFile(
                result.getUrl(),
                result.getFid(),
                "test.txt",
                new ByteArrayInputStream("@deleteFile".getBytes()),
                null,
                ContentType.DEFAULT_BINARY);

        volumeWrapper.deleteFile(result.getUrl(), result.getFid());
    }

    @Test
    public void uploadFile() throws Exception {
        AssignFileKeyParams params = new AssignFileKeyParams();
        AssignFileKeyResult result = masterWrapper.assignFileKey(params);

        Assert.assertTrue(
                volumeWrapper.uploadFile(
                        result.getUrl(),
                        result.getFid(),
                        "test.txt",
                        new ByteArrayInputStream("@uploadFile".getBytes()),
                        null,
                        ContentType.DEFAULT_BINARY) > 0L);
    }

    private static MasterWrapper masterWrapper;
    private static VolumeWrapper volumeWrapper;

    @BeforeClass
    public static void setBeforeClass() throws Exception {
        ConnectionManagerUtil.startup();
        Thread.sleep(1000);
        FileSystemManager manager = ConnectionManagerUtil.connectionManager;
        volumeWrapper = new VolumeWrapper(manager.getSystemConnection());
        masterWrapper = new MasterWrapper(manager.getSystemConnection());
    }

}