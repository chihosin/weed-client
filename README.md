# SeaweedFS Client For Java

[![Build Status](https://travis-ci.org/lokra/seaweedfs-client.svg?branch=master)](https://travis-ci.org/lokra/seaweedfs-client)
[![codecov](https://codecov.io/gh/lokra/seaweedfs-client/branch/master/graph/badge.svg)](https://codecov.io/gh/lokra/seaweedfs-client)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/2a98cfb79ad04905940aef2fc5791390)](https://www.codacy.com/app/chihosin/seaweedfs-client?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=lokra-platform/seaweedfs-client&amp;utm_campaign=Badge_Grade)
[![Maven Central](http://img.shields.io/badge/maven_central-0.7.2.RELEASE-brightgreen.svg)](https://search.maven.org/#artifactdetails%7Corg.lokra.seaweedfs%7Cseaweedfs-client%7C0.7.2.RELEASE%7Cjar)
[![GitHub Release](http://img.shields.io/badge/Release-0.7.2.RELEASE-brightgreen.svg)](https://github.com/lokra-platform/seaweedfs-client/releases/tag/0.7.2.RELEASE)
[![MIT license](http://img.shields.io/badge/license-MIT-blue.svg)](http://opensource.org/licenses/MIT)

# Features
[SeaweedFS](https://github.com/chrislusf/seaweedfs) is a simple and highly scalable distributed file system and started by implementing [Facebook's Haystack design paper](http://www.usenix.org/event/osdi10/tech/full_papers/Beaver.pdf). SeaweedFS is currently growing, with more features on the way.

This Java client is encapsulates the functionality full of the SeaweedFS API and provides a simple interface.

For performance, we make some progress:
* Include [Ehcache](https://github.com/ehcache/ehcache3) and used it for cached lookup volume.
* Include [HttpClient](https://github.com/apache/httpclient) Connection Pool for handle HTTP request.
* Volume server location load balance.
* Heuristic cache is used for fetch file stream.
* Auto switch leader server (master) at failover.

# Quick Start

##### Maven

```xml
<dependency>
  <groupId>org.lokra.seaweedfs</groupId>
  <artifactId>seaweedfs-client</artifactId>
  <version>0.7.2.RELEASE</version>
</dependency>
```

##### Gradle
```
repositories {
    mavenCentral()
}

dependencies {
    compile('org.lokra.seaweedfs:seaweedfs-client:0.7.2.RELEASE')
}
```

##### Create a connection manager
```java
FileSource fileSource = new FileSource();
// SeaweedFS master server host
fileSource.setHost("localhost");
// SeaweedFS master server port
fileSource.setPort(9333);
// Startup manager and listens for the change
fileSource.startup();
```

##### Create a file operation template
```java
// Template used with connection manager
FileTemplate template = new FileTemplate(fileSource.getConnection());
template.saveFileByStream("filename.doc", someFile);
```

## License

The MIT License (MIT)

Copyright (c) 2016 Lokra Studio

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
