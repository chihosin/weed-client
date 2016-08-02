package org.lokra.seaweedfs.core.topology;

/**
 * @author Chiho Sin
 */
public class DataNode {

    private String url;
    private String pubilcUrl;
    private int volumes;
    private int free;
    private int max;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPubilcUrl() {
        return pubilcUrl;
    }

    public void setPubilcUrl(String pubilcUrl) {
        this.pubilcUrl = pubilcUrl;
    }

    public int getVolumes() {
        return volumes;
    }

    public void setVolumes(int volumes) {
        this.volumes = volumes;
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

    @Override
    public String toString() {
        return "DataNode{" +
                "url='" + url + '\'' +
                ", pubilcUrl='" + pubilcUrl + '\'' +
                ", volumes=" + volumes +
                ", free=" + free +
                ", max=" + max +
                '}';
    }
}
