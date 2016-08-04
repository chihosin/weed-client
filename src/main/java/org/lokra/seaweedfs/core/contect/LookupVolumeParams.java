package org.lokra.seaweedfs.core.contect;

/**
 * @author Chiho Sin
 */
public class LookupVolumeParams {

    private String volumeId;
    private String collection;

    public LookupVolumeParams(String volumeId) {
        this.volumeId = volumeId;
    }

    public LookupVolumeParams(String volumeId, String collection) {
        this.volumeId = volumeId;
        this.collection = collection;
    }

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String toUrlParams() {
        String result = "?";
        if (volumeId != null) {
            result = result + "volumeId=" + volumeId + "&";
        }
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
