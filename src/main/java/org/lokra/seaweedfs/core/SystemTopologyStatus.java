package org.lokra.seaweedfs.core;

import org.lokra.seaweedfs.core.topology.DataCenter;
import org.lokra.seaweedfs.core.topology.Layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author Chiho Sin
 */
public class SystemTopologyStatus {

    private int max;
    private int free;
    private String version;
    private Map<String, DataCenter> dataCenters;
    private List<Layout> layouts;

    public SystemTopologyStatus() {
    }

    public SystemTopologyStatus(int max, int free, Map<String, DataCenter> dataCenters, List<Layout> layouts) {
        this.max = max;
        this.free = free;
        this.dataCenters = dataCenters;
        this.layouts = layouts;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    public Map<String, DataCenter> getDataCenters() {
        return dataCenters;
    }

    public void setDataCenters(Map<String, DataCenter> dataCenters) {
        this.dataCenters = dataCenters;
    }

    public List<Layout> getLayouts() {
        return layouts;
    }

    public void setLayouts(List<Layout> layouts) {
        this.layouts = layouts;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "SystemTopologyStatus{" +
                "max=" + max +
                ", free=" + free +
                ", version='" + version + '\'' +
                ", dataCenters=" + dataCenters +
                ", layouts=" + layouts +
                '}';
    }
}
