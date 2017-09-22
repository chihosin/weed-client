package org.lokra.weed;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.lokra.weed.content.VolumeStatus;
import org.lokra.weed.content.WeedFile;

import java.io.File;

public interface WeedVolumeClient {
    @RequestLine("OPTIONS /status")
    void checkAlive();

    @RequestLine("POST /{fid}")
    @Headers("Content-Type: multipart/form-data")
    WeedFile upload(@Param("fid") String fid, @Param("file") File file);

    @RequestLine("DELETE /{fid}")
    WeedFile delete(@Param("fid") String fid);

    @RequestLine("GET /admin/assign_volume")
    void assignVolume(@Param("replication") String replication, @Param("volume") Integer volume);

    @RequestLine("GET /admin/volume/delete")
    void deleteVolume(@Param("volumeId") Integer volumeId);

    @RequestLine("GET /admin/volume/unmount")
    void unmountVolume(@Param("volumeId") Integer volumeId);

    @RequestLine("GET /admin/volume/mount")
    void mountVolume(@Param("volumeId") Integer volumeId);

    @RequestLine("GET /admin/delete_collection")
    void deleteCollection(@Param("collection") String collection);

    @RequestLine("GET /status")
    VolumeStatus status();
}
