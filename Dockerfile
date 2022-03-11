FROM openjdk:17-jdk-bullseye

RUN apt-get update
RUN apt-get -y install locales
RUN localedef -i de_DE -f UTF-8 de_DE.UTF-8

ARG version=1.6.0

COPY target/PocketTracker-${version}.jar /opt/PocketTracker/PocketTracker.jar

WORKDIR /opt/PocketTracker
CMD ["java", "-jar", "PocketTracker.jar"]
