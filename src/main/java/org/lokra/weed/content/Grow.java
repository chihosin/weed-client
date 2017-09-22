package org.lokra.weed.content;

public class Grow {
    private long count;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Grow{" +
                "count=" + count +
                '}';
    }
}
