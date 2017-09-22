package org.lokra.weed.content;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataNode {
    @JsonProperty("Free")
    private long free;
    @JsonProperty("Max")
    private long max;
    @JsonProperty("Volumes")
    private long volumes;
    @JsonProperty("PublicUrl")
    private String publicUrl;
    @JsonProperty("Url")
    private String url;

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

    public long getVolumes() {
        return volumes;
    }

    public void setVolumes(long volumes) {
        this.volumes = volumes;
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    public void setPublicUrl(String publicUrl) {
        this.publicUrl = publicUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "DataNode{" +
                "free=" + free +
                ", max=" + max +
                ", volumes=" + volumes +
                ", publicUrl='" + publicUrl + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
