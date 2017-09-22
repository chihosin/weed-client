package org.lokra.weed.content;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReplicaPlacement {

    @JsonProperty("SameRackCount")
    private long sameRackCount;
    @JsonProperty("DiffRackCount")
    private long diffRackCount;
    @JsonProperty("DiffDataCenterCount")
    private long diffDataCenterCount;

    public long getSameRackCount() {
        return sameRackCount;
    }

    public void setSameRackCount(long sameRackCount) {
        this.sameRackCount = sameRackCount;
    }

    public long getDiffRackCount() {
        return diffRackCount;
    }

    public void setDiffRackCount(long diffRackCount) {
        this.diffRackCount = diffRackCount;
    }

    public long getDiffDataCenterCount() {
        return diffDataCenterCount;
    }

    public void setDiffDataCenterCount(long diffDataCenterCount) {
        this.diffDataCenterCount = diffDataCenterCount;
    }

    @Override
    public String toString() {
        return "ReplicaPlacement{" +
                "sameRackCount=" + sameRackCount +
                ", diffRackCount=" + diffRackCount +
                ", diffDataCenterCount=" + diffDataCenterCount +
                '}';
    }
}
