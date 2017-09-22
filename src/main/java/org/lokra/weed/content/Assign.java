package org.lokra.weed.content;

public class Assign {
    String fid;
    String url;
    String publicUrl;
    long count;

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

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Assign{" +
                "fid='" + fid + '\'' +
                ", url='" + url + '\'' +
                ", publicUrl='" + publicUrl + '\'' +
                ", count=" + count +
                '}';
    }
}
