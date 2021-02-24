FROM openjdk:14
FROM maven:3.5-jdk-8-alpine as builder

WORKDIR /app


COPY pom.xml ./
COPY src ./src/

# Build a release artifact.
RUN mvn package -DskipTests

# Copy the jar to the production image from the builder stage.
COPY --from=builder /app/target/SocketRedisCluterTest-*.jar /SocketRedisCluterTest.jar

CMD ["java","-Djava.security.egd=file:/dev/./urandom", "-jar", "/SocketRedisCluterTest.jar"]