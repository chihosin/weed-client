package org.lokra.seaweedfs.core;

import org.lokra.seaweedfs.core.topology.MasterStatus;

import java.util.List;
import java.util.Map;

public class SystemClusterStatus {

    private MasterStatus leader;
    private List<MasterStatus> peers;
    private int total;
    private int available;

    public SystemClusterStatus(MasterStatus leader, List<MasterStatus> peers) {
        this.leader = leader;
        this.peers = peers;
        this.total = peers.size() + 1;
        this.available = 1;
        for (MasterStatus item : peers) {
            if (item.isActive()) {
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

    public List<MasterStatus> getPeers() {
        return peers;
    }

    public int getTotal() {
        return total;
    }

    public int getAvailable() {
        return available;
    }
}
