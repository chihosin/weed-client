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

package org.lokraplatform.seaweedfs.core.contect;

/**
 * @author Chiho Sin
 */
public class LookupVolumeParams {

    private long volumeId;
    private String collection;

    public LookupVolumeParams(long volumeId) {
        this.volumeId = volumeId;
    }

    public LookupVolumeParams(String volumeId) {
        this.volumeId = convertVolumeId(volumeId);
    }

    public LookupVolumeParams(long volumeId, String collection) {
        this.volumeId = volumeId;
        this.collection = collection;
    }

    public LookupVolumeParams(String volumeId, String collection) {
        this.volumeId = convertVolumeId(volumeId);
        this.collection = collection;
    }

    public long getVolumeId() {
        return volumeId;
    }

    public String getCollection() {
        return collection;
    }

    private long convertVolumeId(String rawVolumeId) {
        final int index = rawVolumeId.indexOf(",");
        if (index < 1)
            return Long.parseLong(rawVolumeId);
        else
            return Long.parseLong(rawVolumeId.substring(0, rawVolumeId.indexOf(",")));
    }

    public String toUrlParams() {
        String result = "?";
        result = result + "volumeId=" + String.valueOf(volumeId) + "&";
        if (collection != null) {
            result = result + "collection=" + collection;
        }
        return result;
    }

    @Override
    public String toString() {
        return "LookupVolumeParams{" +
                "volumeId='" + volumeId + '\'' +
                ", collection='" + collection + '\'' +
                '}';
    }
}
