package org.lokra.weed.content;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class VolumeStatus {
    @JsonProperty("Version")
    private String version;
    @JsonProperty("Volumes")
    private Set<Volume> volumes;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Set<Volume> getVolumes() {
        return volumes;
    }

    public void setVolumes(Set<Volume> volumes) {
        this.volumes = volumes;
    }

    @Override
    public String toString() {
        return "VolumeStatus{" +
                "version='" + version + '\'' +
                ", volumes=" + volumes +
                '}';
    }
}
