FROM maven:3.6-jdk-14 as builder

WORKDIR /app

COPY pom.xml ./
COPY src ./src/

# Build a release artifact.
RUN mvn package -DskipTests

FROM openjdk:14

# Copy the jar to the production image from the builder stage.
COPY --from=builder /app/target/SocketRedisClusterTest-1.0-SNAPSHOT-jar-with-dependencies.jar /SocketRedisClusterTest.jar


CMD ["java","-Djava.security.egd=file:/dev/./urandom", "-jar", "/SocketRedisClusterTest.jar"]