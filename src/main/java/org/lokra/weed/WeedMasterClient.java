package org.lokra.weed;

import feign.Param;
import feign.RequestLine;
import org.lokra.weed.content.*;

public interface WeedMasterClient {
    @RequestLine("OPTIONS /status")
    void checkAlive();

    @RequestLine("GET /dir/assign?replication={replication}&count={count}&dataCenter={dataCenter}&ttl={ttl}")
    Assign assign(@Param("replication") String replication, @Param("count") Integer count, @Param("dataCenter") String dataCenter, @Param("ttl") String ttl);

    @RequestLine("GET /dir/lookup?volumeId={volumeId}&collection={collection}")
    Locations lookup(@Param("volumeId") Integer volumeId, @Param("collection") String collection);

    @RequestLine("GET /vol/vacuum?garbageThreshold={garbageThreshold}")
    void vacuum(@Param("garbageThreshold") Double garbageThreshold);

    @RequestLine("GET /vol/grow?replication={replication}&count={count}&dataCenter={dataCenter}&ttl={ttl}")
    Grow grow(@Param("replication") String replication, @Param("count") Integer count, @Param("dataCenter") String dataCenter, @Param("ttl") String ttl);

    @RequestLine("GET /cluster/status")
    Cluster cluster();

    @RequestLine("GET /dir/status")
    MasterStatus status();
}
