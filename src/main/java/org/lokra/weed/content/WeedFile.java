package org.lokra.weed.content;

public class WeedFile {
    private String name;
    private long size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "WeedFile{" +
                "name='" + name + '\'' +
                ", size=" + size +
                '}';
    }
}
