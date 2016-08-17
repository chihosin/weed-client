# SeaweedFS Client For Java

[![Build Status](https://travis-ci.org/lokra-platform/seaweedfs-client.svg?branch=master)](https://travis-ci.org/lokra-platform/seaweedfs-client)
[![codecov](https://codecov.io/gh/lokra-platform/seaweedfs-client/branch/master/graph/badge.svg)](https://codecov.io/gh/lokra-platform/seaweedfs-client)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/2a98cfb79ad04905940aef2fc5791390)](https://www.codacy.com/app/chihosin/seaweedfs-client?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=lokra-platform/seaweedfs-client&amp;utm_campaign=Badge_Grade)
[![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg)](http://opensource.org/licenses/MIT)

# Features
[SeaweedFS](https://github.com/chrislusf/seaweedfs) is a simple and highly scalable distributed file system and started by implementing [Facebook's Haystack design paper](http://www.usenix.org/event/osdi10/tech/full_papers/Beaver.pdf). SeaweedFS is currently growing, with more features on the way.

This Java client is encapsulates the functionality full of the SeaweedFS API and provides a simple interface.

For performance, the library used Http Client cached some fetch file result stream and you can custom cache storage implement. Ehcached is used for lookup cached volume location, location url load balance can disperse user's requests and reduce the local load of server and local traffic of networks.

# Setup

##### Maven Repository

```xml
<dependency>
  <groupId>org.lokra.seaweedfs</groupId>
  <artifactId>seaweedfs-client</artifactId>
  <version>0.7.0.RELEASE</version>
</dependency>
```
or

##### Gradle Dependency
```
repositories {
    mavenCentral()
}

dependencies {
    compile('org.lokra.seaweedfs:seaweedfs-client:0.7.0.RELEASE')
}
```

# Usage

##### Create a connection manager
```java
FileSystemManager connectionManager = new FileSystemManager();
// SeaweedFS master server host
connectionManager.setHost("localhost");
// SeaweedFS master server port
connectionManager.setPort(9333);
// Startup manager and listens for the change
connectionManager.startup();
```

##### Create a file operation template
```java
// Template used with connection manager
FileTemplate template = new FileTemplate(connectionManager.getSystemConnection());
template.saveFileByStream("filename.doc", someFile);
```