package org.lokra.seaweedfs.core.contect;

import java.util.List;

/**
 * @author Chiho Sin
 */
public class LookupVolumeResult {

    private String volumeId;
    private List<LocationResult> locations;

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }

    public List<LocationResult> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationResult> locations) {
        this.locations = locations;
    }

    public LocationResult getRandomLocation() {
        if (locations != null) {
            return locations.get(((int) (Math.random() * locations.size())));
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "LookupVolumeResult{" +
                "volumeId='" + volumeId + '\'' +
                ", locations=" + locations +
                '}';
    }
}
