package org.lokra.seaweedfs.core.contect;

/**
 * @author Chiho Sin
 */
public class PreAllocateVolumesResult {

    private int count;


    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "PreAllocateVolumesResult{" +
                "count=" + count +
                '}';
    }
}
