FROM progrium/busybox

WORKDIR /opt/weed

RUN opkg-install curl
RUN echo tlsv1 >> ~/.curlrc

RUN \
  curl -Lks https://bintray.com$(curl -Lk http://bintray.com/chrislusf/seaweedfs/seaweedfs/_latestVersion | grep linux_amd64.tar.gz | sed -n "/href/ s/.*href=['\"]\([^'\"]*\)['\"].*/\1/gp") | gunzip | tar -xf - -C /opt/weed/ && \
  mkdir ./bin &&  mv ./*/* ./bin && \
  mkdir /opt/weed/v1 /opt/weed/v2 /opt/weed/v3
  chmod +x ./bin/weed
  ./bin/weed server -master.port=9333 -ip=0.0.0.0 -dir=/opt/weed/v1 -volume.port=9443

EXPOSE 9443
EXPOSE 9333

VOLUME /data

ENV WEED_HOME /opt/weed
ENV PATH ${PATH}:${WEED_HOME}/bin

ENTRYPOINT ["weed"]