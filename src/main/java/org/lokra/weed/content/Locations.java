package org.lokra.weed.content;

import java.io.Serializable;
import java.util.Set;

public class Locations implements Serializable {
    private String volumeId;
    private Set<Location> locations;


    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Locations locations1 = (Locations) o;

        if (volumeId != null ? !volumeId.equals(locations1.volumeId) : locations1.volumeId != null) return false;
        return locations != null ? locations.equals(locations1.locations) : locations1.locations == null;
    }

    @Override
    public int hashCode() {
        int result = volumeId != null ? volumeId.hashCode() : 0;
        result = 31 * result + (locations != null ? locations.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Locations{" +
                "volumeId='" + volumeId + '\'' +
                ", locations=" + locations +
                '}';
    }
}
