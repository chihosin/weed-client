package org.lokra.weed;

import com.fasterxml.jackson.annotation.JsonProperty;
import feign.Feign;
import feign.Param;
import feign.RequestLine;
import feign.jackson.JacksonDecoder;
import feign.okhttp.OkHttpClient;
import org.lokra.seaweedfs.util.ConnectionUtil;

import java.io.Serializable;
import java.util.Set;

public class Test {

    public static void main(String[] args) {
        WeedClient weedClient = Feign.builder()
                .client(new OkHttpClient())
                .decoder(new JacksonDecoder())
                .target(WeedClient.class, "http://localhost:9333");


        System.out.println(weedClient.assign(null, null, null));
        System.out.println(weedClient.lookup(7, null));
        weedClient.vacuum(0.4d);
        System.out.println(weedClient.grow(null, 0, null, null));
        System.out.println(weedClient.cluster());
        System.out.println(weedClient.dir());
    }
}

interface WeedClient {
    @RequestLine("GET /dir/assign?replication={replication}&count={count}&dataCenter={dataCenter}")
    Assign assign(@Param("replication") String replication, @Param("count") Integer count, @Param("dataCenter") String dataCenter);

    @RequestLine("GET /dir/lookup?volumeId={volumeId}&collection={collection}")
    Locations lookup(@Param("volumeId") Integer volumeId, @Param("collection") Integer collection);

    @RequestLine("GET /vol/vacuum?garbageThreshold={garbageThreshold}")
    void vacuum(@Param("garbageThreshold") Double garbageThreshold);

    @RequestLine("GET /vol/grow?replication={replication}&count={count}&dataCenter={dataCenter}&ttl={ttl}")
    Grow grow(@Param("replication") String replication, @Param("count") Integer count, @Param("dataCenter") String dataCenter, @Param("ttl") String ttl);

    @RequestLine("GET /cluster/status")
    Cluster cluster();

    @RequestLine("GET /dir/status")
    Dir dir();
}

class Cluster {
    @JsonProperty(value = "Leader")
    private String leader;
    @JsonProperty(value = "Peers")
    private Set<String> peers;

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public Set<String> getPeers() {
        return peers;
    }

    public void setPeers(Set<String> peers) {
        this.peers = peers;
    }

    @Override
    public String toString() {
        return "Cluster{" +
                "leader='" + leader + '\'' +
                ", peers=" + peers +
                '}';
    }
}

class Assign {
    String fid;
    String url;
    String publicUrl;
    int count;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    public void setPublicUrl(String publicUrl) {
        this.publicUrl = publicUrl;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Assign{" +
                "fid='" + fid + '\'' +
                ", url='" + url + '\'' +
                ", publicUrl='" + publicUrl + '\'' +
                ", count=" + count +
                '}';
    }
}

class Location {
    private String url;
    private String publicUrl;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = ConnectionUtil.convertUrlWithScheme(url);
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    public void setPublicUrl(String publicUrl) {
        this.publicUrl = ConnectionUtil.convertUrlWithScheme(publicUrl);
    }

    @Override
    public String toString() {
        return "Locations{" +
                "url='" + url + '\'' +
                ", publicUrl='" + publicUrl + '\'' +
                '}';
    }
}

class Locations implements Serializable {
    private String volumeId;
    private Set<Location> locations;


    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    @Override
    public String toString() {
        return "Locations{" +
                "volumeId='" + volumeId + '\'' +
                ", locations=" + locations +
                '}';
    }
}

class Grow {
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Grow{" +
                "count=" + count +
                '}';
    }
}

class Dir {
    @JsonProperty("Topology")
    private Topology topology;
    @JsonProperty("Version")
    private String version;

    public Topology getTopology() {
        return topology;
    }

    public void setTopology(Topology topology) {
        this.topology = topology;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Dir{" +
                "topology=" + topology +
                ", version='" + version + '\'' +
                '}';
    }
}

class Topology {
    @JsonProperty("Free")
    private int free;
    @JsonProperty("Max")
    private int max;
    @JsonProperty("DataCenters")
    private Set<DataCenter> dataCenters;
    @JsonProperty("layouts")
    private Set<Layout> layouts;

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public Set<Layout> getLayouts() {
        return layouts;
    }

    public void setLayouts(Set<Layout> layouts) {
        this.layouts = layouts;
    }

    @Override
    public String toString() {
        return "Topology{" +
                "free=" + free +
                ", max=" + max +
                ", layouts=" + layouts +
                '}';
    }
}

class Layout {
    private String collection;
    private String replication;
    private Set<Integer> writables;

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getReplication() {
        return replication;
    }

    public void setReplication(String replication) {
        this.replication = replication;
    }

    public Set<Integer> getWritables() {
        return writables;
    }

    public void setWritables(Set<Integer> writables) {
        this.writables = writables;
    }

    @Override
    public String toString() {
        return "Layout{" +
                "collection='" + collection + '\'' +
                ", replication='" + replication + '\'' +
                ", writables=" + writables +
                '}';
    }
}

class DataCenter {
    @JsonProperty("Free")
    private int free;
    @JsonProperty("Max")
    private int max;
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Racks")
    private Set<Rack> racks;

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<Rack> getRacks() {
        return racks;
    }

    public void setRacks(Set<Rack> racks) {
        this.racks = racks;
    }

    @Override
    public String toString() {
        return "DataCenter{" +
                "free=" + free +
                ", max=" + max +
                ", id='" + id + '\'' +
                ", racks=" + racks +
                '}';
    }
}

class Rack {
    @JsonProperty("Free")
    private int free;
    @JsonProperty("Max")
    private int max;
    @JsonProperty("Id")
    private String id;
    @JsonProperty("DataNodes")
    private Set<DataNode> dataNodes;

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<DataNode> getDataNodes() {
        return dataNodes;
    }

    public void setDataNodes(Set<DataNode> dataNodes) {
        this.dataNodes = dataNodes;
    }

    @Override
    public String toString() {
        return "Rack{" +
                "free=" + free +
                ", max=" + max +
                ", id='" + id + '\'' +
                ", dataNodes=" + dataNodes +
                '}';
    }
}

class DataNode {
    @JsonProperty("Free")
    private int free;
    @JsonProperty("Max")
    private int max;
    @JsonProperty("Volumes")
    private int volumes;
    @JsonProperty("PublicUrl")
    private String publicUrl;
    @JsonProperty("Url")
    private String url;

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getVolumes() {
        return volumes;
    }

    public void setVolumes(int volumes) {
        this.volumes = volumes;
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    public void setPublicUrl(String publicUrl) {
        this.publicUrl = publicUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "DataNode{" +
                "free=" + free +
                ", max=" + max +
                ", volumes=" + volumes +
                ", publicUrl='" + publicUrl + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}