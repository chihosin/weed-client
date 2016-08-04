package org.lokra.seaweedfs.core;

import java.util.Map;

public class SystemClusterStatus {

    private MasterStatus leader;
    private Map<String, MasterStatus> peers;
    private int total;
    private int available;

    public SystemClusterStatus(MasterStatus leader, Map<String, MasterStatus> peers) {
        this.leader = leader;
        this.peers = peers;
        this.total = peers.size() + 1;
        this.available = 1;
        for (String key : peers.keySet()) {
            if (peers.get(key).isActive()) {
                this.available++;
            }
        }
    }

    @Override
    public String toString() {
        return "SystemClusterStatus{" +
                "leader=" + leader +
                ", peers=" + peers +
                ", total=" + total +
                ", available=" + available +
                '}';
    }

    public MasterStatus getLeader() {
        return leader;
    }

    public Map<String, MasterStatus> getPeers() {
        return peers;
    }

    public int getTotal() {
        return total;
    }

    public int getAvailable() {
        return available;
    }
}
