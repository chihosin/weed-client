package org.lokra.weed.content;

public class Location {
    private String url;
    private String publicUrl;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    public void setPublicUrl(String publicUrl) {
        this.publicUrl = publicUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (url != null ? !url.equals(location.url) : location.url != null) return false;
        return publicUrl != null ? publicUrl.equals(location.publicUrl) : location.publicUrl == null;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (publicUrl != null ? publicUrl.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Location{" +
                "url='" + url + '\'' +
                ", publicUrl='" + publicUrl + '\'' +
                '}';
    }
}
