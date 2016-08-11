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
