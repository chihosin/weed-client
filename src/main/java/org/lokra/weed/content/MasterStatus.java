package org.lokra.weed.content;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MasterStatus {
    @JsonProperty("Topology")
    private Topology topology;
    @JsonProperty("Version")
    private String version;

    public Topology getTopology() {
        return topology;
    }

    public void setTopology(Topology topology) {
        this.topology = topology;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Dir{" +
                "topology=" + topology +
                ", version='" + version + '\'' +
                '}';
    }
}
