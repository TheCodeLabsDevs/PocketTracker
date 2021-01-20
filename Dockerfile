FROM openjdk:11-jdk

COPY target/PocketTracker-1.0.0.jar /opt/PocketTracker/PocketTracker.jar

WORKDIR /opt/PocketTracker
CMD ["java", "-jar", "PocketTracker.jar"]
