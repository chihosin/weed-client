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

package org.lokra.seaweedfs.core.contect;

/**
 * @author Chiho Sin
 */
public class PreAllocateVolumesParams {

    private String replication;
    private int count;
    private String dataCenter;
    private String ttl;
    private String collection;

    public PreAllocateVolumesParams() {
    }

    public PreAllocateVolumesParams(String dataCenter) {
        this.dataCenter = dataCenter;
    }

    public PreAllocateVolumesParams(int count) {
        this.count = count;
    }

    public PreAllocateVolumesParams(String replication, int count, String dataCenter, String ttl) {
        this.replication = replication;
        this.count = count;
        this.dataCenter = dataCenter;
        this.ttl = ttl;
    }

    public String getReplication() {
        return replication;
    }

    public void setReplication(String replication) {
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

    public void setDataCenter(String dataCenter) {
        this.dataCenter = dataCenter;
    }

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String toUrlParams() {
        String result = "?";
        if (replication != null) {
            result = result + "replication=" + replication + "&";
        }
        if (dataCenter != null) {
            result = result + "dataCenter=" + dataCenter + "&";
        }
        if (count > 0) {
            result = result + "count=" + Integer.toString(count) + "&";
        }
        if (collection != null) {
            result = result + "collection=" + collection + "&";
        }
        if (ttl != null) {
            result = result + "ttl=" + ttl;
        }
        return result;
    }

    @Override
    public String toString() {
        return "PreAllocateVolumesParams{" +
                "replication='" + replication + '\'' +
                ", count=" + count +
                ", dataCenter='" + dataCenter + '\'' +
                ", ttl='" + ttl + '\'' +
                ", collection='" + collection + '\'' +
                '}';
    }
}
