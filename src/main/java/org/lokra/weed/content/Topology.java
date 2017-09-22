package org.lokra.weed.content;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class Topology {
    @JsonProperty("Free")
    private long free;
    @JsonProperty("Max")
    private long max;
    @JsonProperty("DataCenters")
    private Set<DataCenter> dataCenters;
    @JsonProperty("layouts")
    private Set<Layout> layouts;

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

    public Set<Layout> getLayouts() {
        return layouts;
    }

    public void setLayouts(Set<Layout> layouts) {
        this.layouts = layouts;
    }

    @Override
    public String toString() {
        return "Topology{" +
                "free=" + free +
                ", max=" + max +
                ", layouts=" + layouts +
                '}';
    }
}
