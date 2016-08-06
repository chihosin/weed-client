package org.lokra.seaweedfs.core.contect;

import org.lokra.seaweedfs.util.ConnectionUtil;

/**
 * @author Chiho Sin
 */
public class LocationResult {
    private String url;
    private String publicUrl;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = ConnectionUtil.convertUrlWithScheme(url);
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    public void setPublicUrl(String publicUrl) {
        this.publicUrl = ConnectionUtil.convertUrlWithScheme(publicUrl);
    }

    @Override
    public String toString() {
        return "LocationResult{" +
                "url='" + url + '\'' +
                ", publicUrl='" + publicUrl + '\'' +
                '}';
    }
}
