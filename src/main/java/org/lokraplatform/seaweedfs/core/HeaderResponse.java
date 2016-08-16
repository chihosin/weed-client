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

package org.lokraplatform.seaweedfs.core;

import org.apache.http.Header;

import java.util.Arrays;

/**
 * @author Chiho Sin
 */
public class HeaderResponse {

    private Header[] headers;
    private int httpResponseStatusCode;

    public HeaderResponse(Header[] headers, int httpResponseStatusCode) {
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
        return "HeaderResponse{" +
                "headers=" + Arrays.toString(headers) +
                ", httpResponseStatusCode=" + httpResponseStatusCode +
                '}';
    }

}
