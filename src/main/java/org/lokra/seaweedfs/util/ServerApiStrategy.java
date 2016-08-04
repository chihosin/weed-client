package org.lokra.seaweedfs.util;

/**
 * @author Chiho Sin
 */
public class ServerApiStrategy {

    public static final String assignFileKey = "/dir/assign";
    public static final String checkClusterStatus = "/cluster/status";
    public static final String checkTopologyStatus = "/dir/status";
    public static final String lookupVolume = "/dir/lookup";
    public static final String forceGarbageCollection = "/vol/vacuum";
    public static final String preAllocateVolumes = "/vol/grow";

}
