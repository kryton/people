FROM        docker-registry-v2.digitalriver-external.com/playbase:latest

MAINTAINER  Ian Holsman <kryton@gmail.com>

# COMMIT PROJECT FILES

ADD         app /root/app
ADD         lib /root/lib
ADD         test /root/test
ADD         conf /root/conf
RUN         cd /root/conf  && cp application.conf.docker application.conf
RUN         cd /root/conf  && cp logback.xml.docker logback.xml
ADD         public /root/public
ADD         public/javascripts/main.js /root/public/javascripts/main.js
RUN         mkdir -p /root/public/stylesheets/
#ADD         public/stylesheets/main.css /root/public/stylesheets/main.css
ADD         build.sbt /root/
ADD         project/plugins.sbt /root/project/
ADD         project/build.properties /root/project/
#RUN         mkdir  -p /pic/cache  /pic/source && chown 777 /pic/cache /pic/source

# TEST AND BUILD THE PROJECT -- FAILURE WILL HALT IMAGE CREATION
#RUN         cd /root; /usr/local/activator/activator test stage
RUN         cd /root; /usr/local/activator/bin/activator package stage
RUN         cd  /root/conf; cp secret.conf.docker secret.conf
RUN         cd  /root/conf; cp logback.xml.docker logback.xml
RUN         rm /root/target/universal/stage/bin/*.bat

# TESTS PASSED -- CONFIGURE IMAGE
WORKDIR     /root
ENTRYPOINT  target/universal/stage/bin/$(ls target/universal/stage/bin)
VOLUME      ["/pic/cache","/pic/source","/pic/user-upload","/pic/tmp","/logs"]
EXPOSE      9000 9443
