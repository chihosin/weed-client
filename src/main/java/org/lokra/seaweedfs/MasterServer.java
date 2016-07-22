package org.lokra.seaweedfs;

import com.sun.istack.internal.NotNull;

import java.net.URI;

/**
 * Created by ChihoSin on 16/7/22.
 */
public class MasterServer {

    private URI uri;
    private boolean isActive;

    public MasterServer(@NotNull URI uri) {
        this.uri = uri;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "MasterServer{" +
                "uri=" + uri +
                ", isActive=" + isActive +
                '}';
    }
}
