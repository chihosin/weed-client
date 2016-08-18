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
import org.lokra.seaweedfs.FileSource;
import org.lokra.seaweedfs.FileSystemTest;
import org.lokra.seaweedfs.core.contect.*;

/**
 * @author Chiho Sin
 */
public class MasterWrapperTest {

    private static MasterWrapper wrapper;

    @BeforeClass
    public static void setBeforeClass() throws Exception {
        FileSystemTest.startup();
        Thread.sleep(1000);
        FileSource manager = FileSystemTest.fileSource;
        wrapper = new MasterWrapper(manager.getConnection());
    }

    @Test
    public void lookupVolume() throws Exception {
        LookupVolumeParams params = new LookupVolumeParams("1");
        LookupVolumeResult result = wrapper.lookupVolume(params);
        Assert.assertEquals(params.getVolumeId(), result.getVolumeId());
    }

    @Test
    public void forceGarbageCollection() throws Exception {
        ForceGarbageCollectionParams params = new ForceGarbageCollectionParams(0.4f);
        wrapper.forceGarbageCollection(params);
    }

    @Test
    public void preAllocateVolumes() throws Exception {
        PreAllocateVolumesParams params = new PreAllocateVolumesParams();
        params.setCount(1);
        PreAllocateVolumesResult result = wrapper.preAllocateVolumes(params);
        Assert.assertEquals(Long.parseLong(String.valueOf(params.getCount())),
                Long.parseLong(String.valueOf(result.getCount())));
    }

    @Test
    public void assignFileKey() throws Exception {
        AssignFileKeyParams params = new AssignFileKeyParams();
        params.setReplication("001");
        params.setCollection("test");
        params.setCount(1);
        AssignFileKeyResult result = wrapper.assignFileKey(params);
        Assert.assertEquals(Long.parseLong(String.valueOf(params.getCount())),
                Long.parseLong(String.valueOf(result.getCount())));
    }

}