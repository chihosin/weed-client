package org.lokra.seaweedfs;

/**
 * @author Chiho Sin
 */
public class VolumeLocationsParams {

    private String volumeId;
    private String collection;

    public VolumeLocationsParams() {
    }

    public VolumeLocationsParams(String volumeId) {
        this.volumeId = volumeId;
    }

    public VolumeLocationsParams(String volumeId, String collection) {
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

    public String toUrlParam() {
        String result = "?";
        if (volumeId != null) {
            result = result + "volumeId=" + volumeId + "&";
        }
        if (collection != null) {
            result = result + "collection=" + collection + "&";
        }
        return result;
    }

    @Override
    public String toString() {
        return "VolumeLocationsParams{" +
                "volumeId='" + volumeId + '\'' +
                ", collection='" + collection + '\'' +
                '}';
    }
}
