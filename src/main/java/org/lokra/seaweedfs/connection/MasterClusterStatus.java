package org.lokra.seaweedfs.connection;

import com.sun.istack.internal.NotNull;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ChihoSin on 16/7/22.
 */
public class MasterClusterStatus {

    private ServerStatus leader;
    private ConcurrentHashMap<String, ServerStatus> peers;

    public MasterClusterStatus(@NotNull ServerStatus leader,
                               @NotNull ConcurrentHashMap<String, ServerStatus> peers) {
        this.leader = leader;
        this.peers = peers;
    }

    @Override
    public String toString() {
        return "MasterClusterStatus{" +
                "leader=" + leader +
                ", peers=" + peers +
                '}';
    }

    public ServerStatus getLeader() {
        return leader;
    }

    public ConcurrentHashMap<String, ServerStatus> getPeers() {
        return peers;
    }
}
