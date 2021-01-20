FROM openjdk:11-jdk

ARG version=1.0.0

COPY target/PocketTracker-${version}.jar /opt/PocketTracker/PocketTracker.jar

WORKDIR /opt/PocketTracker
CMD ["java", "-jar", "PocketTracker.jar"]
