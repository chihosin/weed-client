package org.lokra.seaweedfs;

import com.sun.istack.internal.NotNull;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ChihoSin on 16/7/22.
 */
public class ClusterStatus {

    private MasterServer leader;
    private ConcurrentHashMap<URI, MasterServer> peers;

    public ClusterStatus(@NotNull MasterServer leader,
                         @NotNull ConcurrentHashMap<URI, MasterServer> peers) {
        this.leader = leader;
        this.peers = peers;
    }

    @Override
    public String toString() {
        return "ClusterStatus{" +
                "leader=" + leader +
                ", peers=" + peers +
                '}';
    }
}
