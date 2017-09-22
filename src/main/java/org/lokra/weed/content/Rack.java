package org.lokra.weed.content;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class Rack {
    @JsonProperty("Free")
    private long free;
    @JsonProperty("Max")
    private long max;
    @JsonProperty("Id")
    private String id;
    @JsonProperty("DataNodes")
    private Set<DataNode> dataNodes;

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

    public Set<DataNode> getDataNodes() {
        return dataNodes;
    }

    public void setDataNodes(Set<DataNode> dataNodes) {
        this.dataNodes = dataNodes;
    }

    @Override
    public String toString() {
        return "Rack{" +
                "free=" + free +
                ", max=" + max +
                ", id='" + id + '\'' +
                ", dataNodes=" + dataNodes +
                '}';
    }
}
