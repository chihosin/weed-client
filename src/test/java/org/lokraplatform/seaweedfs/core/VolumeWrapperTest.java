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

package org.lokraplatform.seaweedfs.core;

import org.apache.http.entity.ContentType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lokraplatform.seaweedfs.FileSystemManager;
import org.lokraplatform.seaweedfs.FileSystemTest;
import org.lokraplatform.seaweedfs.core.contect.AssignFileKeyParams;
import org.lokraplatform.seaweedfs.core.contect.AssignFileKeyResult;

import java.io.ByteArrayInputStream;

/**
 * @author Chiho Sin
 */
public class VolumeWrapperTest {
    private static MasterWrapper masterWrapper;
    private static VolumeWrapper volumeWrapper;

    @BeforeClass
    public static void setBeforeClass() throws Exception {
        FileSystemTest.startup();
        Thread.sleep(1000);
        FileSystemManager manager = FileSystemTest.connectionManager;
        volumeWrapper = new VolumeWrapper(manager.getSystemConnection());
        masterWrapper = new MasterWrapper(manager.getSystemConnection());
    }

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

        StreamResponse cache = volumeWrapper.getFileStream(result.getUrl(), result.getFid());
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

}