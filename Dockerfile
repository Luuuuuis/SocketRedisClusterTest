FROM openjdk:14
COPY target/ .
WORKDIR .
CMD java -jar SocketRedisClusterTest-1.0-SNAPSHOT-jar-with-dependencies.jar