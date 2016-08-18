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

package org.lokra.seaweedfs.core.http;

import java.io.*;

/**
 * @author Chiho Sin
 */
public class StreamResponse {

    private ByteArrayOutputStream byteArrayOutputStream;
    private int httpResponseStatusCode;
    private long length = 0;

    public StreamResponse(InputStream inputStream, int httpResponseStatusCode) throws IOException {
        this.httpResponseStatusCode = httpResponseStatusCode;
        if (inputStream == null)
            return;

        this.byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > -1) {
            byteArrayOutputStream.write(buffer, 0, length);
            this.length += length;
        }
        byteArrayOutputStream.flush();
    }

    public InputStream getInputStream() {
        if (byteArrayOutputStream == null)
            return null;

        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    public int getHttpResponseStatusCode() {
        return httpResponseStatusCode;
    }

    public OutputStream getOutputStream() {
        return byteArrayOutputStream;
    }

    public long getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "StreamResponse{" +
                "byteArrayOutputStream=" + byteArrayOutputStream +
                ", httpResponseStatusCode=" + httpResponseStatusCode +
                ", length=" + length +
                '}';
    }
}
