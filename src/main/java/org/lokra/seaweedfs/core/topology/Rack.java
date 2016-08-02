package org.lokra.seaweedfs.core.topology;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Chiho Sin
 */
public class Rack {

    private String id;
    private int free;
    private int max;
    private Map<String, DataNode> dataNodes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public Map<String, DataNode> getDataNodes() {
        return dataNodes;
    }

    public void setDataNodes(Map<String, DataNode> dataNodes) {
        this.dataNodes = dataNodes;
    }

    @Override
    public String toString() {
        return "Rack{" +
                "id='" + id + '\'' +
                ", free=" + free +
                ", max=" + max +
                ", dataNodes=" + dataNodes +
                '}';
    }
}
