package org.lokra.seaweedfs.connection;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;

/**
 * Sample connection tools
 *
 * @author Chiho Sin
 */
class ConnectionUtil {

    /**
     * Check uri link is alive, the basis for judging response status code.
     *
     * @param client httpClient
     * @param url    check url
     * @return When the response status code is 200, the result is true.
     */
    static boolean checkUriAlive(HttpClient client, String url, RequestConfig requestConfig) {
        try {
            final HttpGet peerRequest = new HttpGet(url);

            if (requestConfig != null)
                peerRequest.setConfig(requestConfig);

            final HttpResponse response = client.execute(peerRequest);
            return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Convert url with scheme match seaweedfs server
     *
     * @param serverUrl url without scheme
     */
    static String convertUrlWithScheme(String serverUrl) {
        return "http://" + serverUrl;
    }

}
