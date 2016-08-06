package org.lokra.seaweedfs.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author Chiho Sin
 */
public class VolumeWrapper {

    private static final Log log = LogFactory.getLog(VolumeWrapper.class);

    private SystemConnection connection;
    private ObjectMapper objectMapper = new ObjectMapper();

    public VolumeWrapper(SystemConnection connection) {
        this.connection = connection;
    }

    /**
     * Upload file.
     *
     * @param url
     * @param fid
     * @param fileName
     * @param stream
     * @return The size returned is the size stored on SeaweedFS.
     * @throws IOException
     */
    public long uploadFile(String url, String fid, String fileName, InputStream stream) throws IOException {
        HttpPost request = new HttpPost(url + "/" + fid);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody("upload", stream, ContentType.DEFAULT_BINARY, fileName);
        HttpEntity entity = builder.build();
        request.setEntity(entity);
        String json = connection.fetchJsonResultByRequest(request);
        return (Integer) objectMapper.readValue(json, Map.class).get("size");
    }

    /**
     * Delete file.
     *
     * @param url
     * @param fid
     * @return The size returned is the trash size stored on SeaweedFS.
     * @throws IOException
     */
    public long deleteFile(String url, String fid) throws IOException {
        HttpDelete request = new HttpDelete(url + "/" + fid);
        String json = connection.fetchJsonResultByRequest(request);
        return (Integer) objectMapper.readValue(json, Map.class).get("size");
    }

}
