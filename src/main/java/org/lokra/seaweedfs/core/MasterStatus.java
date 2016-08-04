package org.lokra.seaweedfs.core;


/**
 * @author Chiho Sin
 */
public class MasterStatus {

    private String url;
    private boolean isActive;

    public MasterStatus(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "MasterStatus{" +
                "url=" + url +
                ", isActive=" + isActive +
                '}';
    }
}
