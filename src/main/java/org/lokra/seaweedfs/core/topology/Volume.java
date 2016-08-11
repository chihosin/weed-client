/*
 * Copyright (c) 2016 Lokra Studio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.lokra.seaweedfs.core.topology;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author Chiho Sin
 */
public class Volume {

    @JsonProperty("Id")
    private long id;
    @JsonProperty("Size")
    private long size;
    @JsonProperty("ReplicaPlacement")
    private ReplicaPlacement replicaPlacement;
    @JsonProperty("Ttl")
    private List<String> ttl;
    @JsonProperty("Collection")
    private String collection;
    @JsonProperty("Version")
    private long version;
    @JsonProperty("FileCount")
    private long fileCount;
    @JsonProperty("DeleteCount")
    private long deleteCount;
    @JsonProperty("DeletedByteCount")
    private long deletedByteCount;
    @JsonProperty("ReadOnly")
    private boolean readOnly;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public ReplicaPlacement getReplicaPlacement() {
        return replicaPlacement;
    }

    public void setReplicaPlacement(ReplicaPlacement replicaPlacement) {
        this.replicaPlacement = replicaPlacement;
    }

    public List<String> getTtl() {
        return ttl;
    }

    public void setTtl(List<String> ttl) {
        this.ttl = ttl;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public long getFileCount() {
        return fileCount;
    }

    public void setFileCount(long fileCount) {
        this.fileCount = fileCount;
    }

    public long getDeleteCount() {
        return deleteCount;
    }

    public void setDeleteCount(long deleteCount) {
        this.deleteCount = deleteCount;
    }

    public long getDeletedByteCount() {
        return deletedByteCount;
    }

    public void setDeletedByteCount(long deletedByteCount) {
        this.deletedByteCount = deletedByteCount;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override
    public String toString() {
        return "Volume{" +
                "id=" + id +
                ", size=" + size +
                ", replicaPlacement=" + replicaPlacement +
                ", ttl=" + ttl +
                ", collection='" + collection + '\'' +
                ", version=" + version +
                ", fileCount=" + fileCount +
                ", deleteCount=" + deleteCount +
                ", deletedByteCount=" + deletedByteCount +
                ", readOnly=" + readOnly +
                '}';
    }
}
