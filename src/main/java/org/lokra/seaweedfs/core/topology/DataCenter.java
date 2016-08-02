package org.lokra.seaweedfs.core.topology;

import java.util.Map;

/**
 * @author Chiho Sin
 */
public class DataCenter {

    private String id;
    private int free;
    private int max;
    private Map<String, Rack> racks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public Map<String, Rack> getRacks() {
        return racks;
    }

    public void setRacks(Map<String, Rack> racks) {
        this.racks = racks;
    }

    @Override
    public String toString() {
        return "DataCenter{" +
                "id='" + id + '\'' +
                ", free=" + free +
                ", max=" + max +
                ", racks=" + racks +
                '}';
    }
}
