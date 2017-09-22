package org.lokra.weed.content;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class Cluster {
    @JsonProperty(value = "Leader")
    private String leader;
    @JsonProperty(value = "Peers")
    private Set<String> peers;

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public Set<String> getPeers() {
        return peers;
    }

    public void setPeers(Set<String> peers) {
        this.peers = peers;
    }

    @Override
    public String toString() {
        return "Cluster{" +
                "leader='" + leader + '\'' +
                ", peers=" + peers +
                '}';
    }
}
