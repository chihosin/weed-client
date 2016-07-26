package org.lokra.seaweedfs.connection;

import com.sun.istack.internal.NotNull;

import java.net.URI;

/**
 * Created by ChihoSin on 16/7/22.
 */
public class ServerStatus {

    private String url;
    private boolean isActive;

    public ServerStatus(@NotNull String url) {
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
        return "ServerStatus{" +
                "url=" + url +
                ", isActive=" + isActive +
                '}';
    }
}
