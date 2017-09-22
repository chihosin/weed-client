package org.lokra.weed.content;

import java.util.Set;

public class Layout {
    private String collection;
    private String replication;
    private Set<Integer> writables;

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getReplication() {
        return replication;
    }

    public void setReplication(String replication) {
        this.replication = replication;
    }

    public Set<Integer> getWritables() {
        return writables;
    }

    public void setWritables(Set<Integer> writables) {
        this.writables = writables;
    }

    @Override
    public String toString() {
        return "Layout{" +
                "collection='" + collection + '\'' +
                ", replication='" + replication + '\'' +
                ", writables=" + writables +
                '}';
    }
}
