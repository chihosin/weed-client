FROM progrium/busybox

WORKDIR /opt/weed

RUN opkg-install curl
RUN echo tlsv1 >> ~/.curlrc

RUN \
  curl -Lks https://bintray.com$(curl -Lk http://bintray.com/chrislusf/seaweedfs/seaweedfs/_latestVersion | grep linux_amd64.tar.gz | sed -n "/href/ s/.*href=['\"]\([^'\"]*\)['\"].*/\1/gp") | gunzip | tar -xf - -C /opt/weed/ && \
  mkdir ./bin &&  mv ./*/* ./bin && \
  mkdir -p /opt/weed/v1 /opt/weed/v2 /opt/weed/v3 \
  chmod +x ./bin/weed \
  nohup /opt/weed/bin/weed server -master.port=9333 -ip=0.0.0.0 -dir=/opt/weed/v1 -volume.port=9443 >/dev/null 2>&1 & \
  nohup /opt/weed/bin/weed server -master.port=9334 -ip=0.0.0.0 -dir=/opt/weed/v2 -volume.port=9444 -master.peers=0.0.0.0:9333 \
  nohup /opt/weed/bin/weed server -master.port=9335 -ip=0.0.0.0 -dir=/opt/weed/v3 -volume.port=9445 -master.peers=0.0.0.0:9334

EXPOSE 9443
EXPOSE 9444
EXPOSE 9445
EXPOSE 9333
EXPOSE 9334
EXPOSE 9335

VOLUME /data

ENV WEED_HOME /opt/weed
ENV PATH ${PATH}:${WEED_HOME}/bin

ENTRYPOINT ["weed"]