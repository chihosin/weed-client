package org.lokra.seaweedfs;

import com.sun.istack.internal.NotNull;

/**
 * @author Chiho Sin
 */
public class AssignFileKeyParams {

    private String replication;
    private int count;
    private String dataCenter;

    public AssignFileKeyParams() {
    }

    public AssignFileKeyParams(String dataCenter) {
        this.dataCenter = dataCenter;
    }

    public AssignFileKeyParams(int count) {
        this.count = count;
    }

    public AssignFileKeyParams(@NotNull String replication, int count, @NotNull String dataCenter) {
        this.replication = replication;
        this.count = count;
        this.dataCenter = dataCenter;
    }

    public String getReplication() {
        return replication;
    }

    public void setReplication(@NotNull String replication) {
        this.replication = replication;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDataCenter() {
        return dataCenter;
    }

    public void setDataCenter(@NotNull String dataCenter) {
        this.dataCenter = dataCenter;
    }

    public String toUrlParam() {
        String result = "?";
        if (replication != null) {
            result = result + "replication=" + replication + "&";
        }
        if (dataCenter != null) {
            result = result + "dataCenter=" + dataCenter + "&";
        }
        if (count > 0) {
            result = result + "dataCenter=" + Integer.toString(count) + "&";
        }
        return result;
    }

    @Override
    public String toString() {
        return "AssignFileKeyParams{" +
                "replication=" + replication +
                ", count=" + count +
                ", dataCenter='" + dataCenter + '\'' +
                '}';
    }
}
