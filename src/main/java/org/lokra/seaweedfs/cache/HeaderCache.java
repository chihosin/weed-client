package org.lokra.seaweedfs.cache;

import org.apache.http.Header;

import java.util.Arrays;

/**
 * @author Chiho Sin
 */
public class HeaderCache {

    private Header[] headers;
    private int httpResponseStatusCode;

    public HeaderCache(Header[] headers, int httpResponseStatusCode) {
        this.httpResponseStatusCode = httpResponseStatusCode;
        if (headers == null)
            return;
        this.headers = headers;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public int getHttpResponseStatusCode() {
        return httpResponseStatusCode;
    }

    public Header getLastHeader(String name) {
        for (int index = headers.length - 1; index > -1; index--) {
            if (headers[index].getName().equals(name))
                return headers[index];
        }
        return null;
    }

    public Header getFirstHeader(String name) {
        for (int index = 0; index < headers.length; index++) {
            if (headers[index].getName().equals(name))
                return headers[index];
        }
        return null;
    }

    @Override
    public String toString() {
        return "HeaderCache{" +
                "headers=" + Arrays.toString(headers) +
                ", httpResponseStatusCode=" + httpResponseStatusCode +
                '}';
    }

}
