package org.lokra.seaweedfs.core;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lokra.seaweedfs.FileSystemManager;
import org.lokra.seaweedfs.core.contect.*;
import org.lokra.seaweedfs.test.ConnectionManagerUtil;

/**
 * @author Chiho Sin
 */
public class MasterWrapperTest {

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

    private static MasterWrapper wrapper;

    @BeforeClass
    public static void setBeforeClass() throws Exception {
        ConnectionManagerUtil.startup();
        Thread.sleep(1000);
        FileSystemManager manager = ConnectionManagerUtil.connectionManager;
        wrapper = new MasterWrapper(manager.getSystemConnection());
    }

}