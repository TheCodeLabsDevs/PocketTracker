FROM eclipse-temurin:17-jre-alpine

RUN apk update && apk upgrade && \
    rm -rf /var/cache/apk

ARG version=1.7.1

COPY target/PocketTracker-${version}.jar /opt/PocketTracker/PocketTracker.jar

WORKDIR /opt/PocketTracker
CMD ["java", "-jar", "PocketTracker.jar"]
