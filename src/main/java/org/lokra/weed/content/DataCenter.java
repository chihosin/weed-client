package org.lokra.weed.content;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class DataCenter {
    @JsonProperty("Free")
    private long free;
    @JsonProperty("Max")
    private long max;
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Racks")
    private Set<Rack> racks;

    public long getFree() {
        return free;
    }

    public void setFree(long free) {
        this.free = free;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<Rack> getRacks() {
        return racks;
    }

    public void setRacks(Set<Rack> racks) {
        this.racks = racks;
    }

    @Override
    public String toString() {
        return "DataCenter{" +
                "free=" + free +
                ", max=" + max +
                ", id='" + id + '\'' +
                ", racks=" + racks +
                '}';
    }
}
