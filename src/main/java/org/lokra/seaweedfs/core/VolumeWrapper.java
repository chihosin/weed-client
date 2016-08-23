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

package org.lokra.seaweedfs.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.lokra.seaweedfs.core.http.HeaderResponse;
import org.lokra.seaweedfs.core.http.JsonResponse;
import org.lokra.seaweedfs.core.http.StreamResponse;
import org.lokra.seaweedfs.exception.SeaweedfsException;
import org.lokra.seaweedfs.exception.SeaweedfsFileNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Volume server operation wrapper.
 *
 * @author Chiho Sin
 */
class VolumeWrapper {

    private Connection connection;
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructor.
     *
     * @param Connection Connection from file source.
     */
    VolumeWrapper(Connection Connection) {
        this.connection = Connection;
    }

    /**
     * Upload file.
     *
     * @param url         url
     * @param fid         fid
     * @param fileName    fileName
     * @param stream      stream
     * @param ttl         ttl
     * @param contentType contentType
     * @return The size returned is the size stored on SeaweedFS.
     * @throws IOException Http connection is fail or server response within some error message.
     */
    long uploadFile(String url, String fid, String fileName, InputStream stream, String ttl,
                    ContentType contentType)
            throws IOException {
        HttpPost request;
        if (ttl != null)
            request = new HttpPost(url + "/" + fid + "?ttl=" + ttl);
        else
            request = new HttpPost(url + "/" + fid);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody("upload", stream, contentType, fileName);
        HttpEntity entity = builder.build();
        request.setEntity(entity);
        JsonResponse jsonResponse = connection.fetchJsonResultByRequest(request);
        convertResponseStatusToException(jsonResponse.statusCode, url, fid, false, false, false, false);
        return (Integer) objectMapper.readValue(jsonResponse.json, Map.class).get("size");
    }

    /**
     * Delete file.
     *
     * @param url Server url.
     * @param fid File id.
     * @throws IOException Http connection is fail or server response within some error message.
     */
    void deleteFile(String url, String fid) throws IOException {
        HttpDelete request = new HttpDelete(url + "/" + fid);
        convertResponseStatusToException(connection.fetchJsonResultByRequest(request).statusCode,
                url, fid, false, false, false, false);
    }

    /**
     * Check file is exist.
     *
     * @param url Server url.
     * @param fid File id.
     * @return If file is exist that result is true.
     * @throws IOException Http connection is fail or server response within some error message.
     */
    boolean checkFileExist(String url, String fid) throws IOException {
        HttpHead request = new HttpHead(url + "/" + fid);
        final int statusCode = connection.fetchStatusCodeByRequest(request);
        try {
            convertResponseStatusToException(statusCode, url, fid, false, true, false, false);
            return true;
        } catch (SeaweedfsFileNotFoundException e) {
            return false;
        }
    }

    /**
     * Get file stream.
     *
     * @param url Server url.
     * @param fid File id.
     * @return File input stream cache at system memory.
     * @throws IOException Http connection is fail or server response within some error message.
     */
    StreamResponse getFileStream(String url, String fid) throws IOException {
        HttpGet request = new HttpGet(url + "/" + fid);
        StreamResponse cache = connection.fetchStreamCacheByRequest(request);
        convertResponseStatusToException(cache.getHttpResponseStatusCode(), url, fid, false, false, false, false);
        return cache;
    }

    /**
     * Get file status.
     *
     * @param url Server url.
     * @param fid File id.
     * @return File status header.
     * @throws IOException Http connection is fail or server response within some error message.
     */
    HeaderResponse getFileStatusHeader(String url, String fid) throws IOException {
        HttpHead request = new HttpHead(url + "/" + fid);
        HeaderResponse cache = connection.fetchHeaderByRequest(request);
        convertResponseStatusToException(cache.getHttpResponseStatusCode(), url, fid, false, false, false, false);
        return cache;
    }

    /**
     * @param statusCode         Server response code.
     * @param url                Server url.
     * @param fid                File id.
     * @param ignoreNotFound     Ignore http response not found status.
     * @param ignoreRedirect     Ignore http response redirect status.
     * @param ignoreRequestError Ignore http response request error status.
     * @param ignoreServerError  Ignore http response server error status.
     * @throws SeaweedfsException Http connection is fail or server response within some error message.
     */
    private void convertResponseStatusToException(int statusCode, String url, String fid,
                                                  boolean ignoreNotFound,
                                                  boolean ignoreRedirect,
                                                  boolean ignoreRequestError,
                                                  boolean ignoreServerError) throws SeaweedfsException {

        switch (statusCode / 100) {
            case 1:
                return;
            case 2:
                return;
            case 3:
                if (ignoreRedirect)
                    return;
                throw new SeaweedfsException(
                        "fetch file from [" + url + "/" + fid + "] is redirect, " +
                                "response stats code is [" + statusCode + "]");
            case 4:
                if (statusCode == 404 && ignoreNotFound)
                    return;
                else if (statusCode == 404)
                    throw new SeaweedfsFileNotFoundException(
                            "fetch file from [" + url + "/" + fid + "] is not found, " +
                                    "response stats code is [" + statusCode + "]");
                if (ignoreRequestError)
                    return;
                throw new SeaweedfsException(
                        "fetch file from [" + url + "/" + fid + "] is request error, " +
                                "response stats code is [" + statusCode + "]");
            case 5:
                if (ignoreServerError)
                    return;
                throw new SeaweedfsException(
                        "fetch file from [" + url + "/" + fid + "] is request error, " +
                                "response stats code is [" + statusCode + "]");
            default:
                throw new SeaweedfsException(
                        "fetch file from [" + url + "/" + fid + "] is error, " +
                                "response stats code is [" + statusCode + "]");
        }
    }

}
