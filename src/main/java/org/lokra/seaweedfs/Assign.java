package org.lokra.seaweedfs;

/**
 * Created by ChihoSin on 16/7/22.
 */
public class Assign {

    private Integer count;
    private String fid;
    private String url;
    private String publicUrl;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    public void setPublicUrl(String publicUrl) {
        this.publicUrl = publicUrl;
    }

    @Override
    public String toString() {
        return "Assign{" +
                "count=" + count +
                ", fid='" + fid + '\'' +
                ", url='" + url + '\'' +
                ", publicUrl='" + publicUrl + '\'' +
                '}';
    }
}
