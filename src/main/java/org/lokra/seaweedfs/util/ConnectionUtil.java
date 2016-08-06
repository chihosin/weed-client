package org.lokra.seaweedfs.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

/**
 * Sample connection tools
 *
 * @author Chiho Sin
 */
public class ConnectionUtil {

    /**
     * Check uri link is alive, the basis for judging response status code.
     *
     * @param client httpClient
     * @param url    check url
     * @return When the response status code is 200, the result is true.
     */
    public static boolean checkUriAlive(CloseableHttpClient client, String url) {
        boolean result = false;
        CloseableHttpResponse response = null;
        HttpGet request = new HttpGet(url);
        try {
            response = client.execute(request, HttpClientContext.create());
            result = response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
        } catch (IOException e) {
            return false;
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ignored) {
                }
            }
            request.releaseConnection();
        }
        return result;
    }

    /**
     * Convert url with scheme match seaweedfs server
     *
     * @param serverUrl url without scheme
     */
    public static String convertUrlWithScheme(String serverUrl) {
        return "http://" + serverUrl;
    }

}
