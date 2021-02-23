FROM openjdk:11-jdk

ARG version=1.0.0

COPY target/PocketTracker-${version}.jar /opt/PocketTracker/PocketTracker.jar

RUN localedef -i de_DE -f UTF-8 de_DE.UTF-8

WORKDIR /opt/PocketTracker
CMD ["java", "-jar", "PocketTracker.jar"]
