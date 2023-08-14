FROM openjdk:17-alpine
MAINTAINER Zhurenkov Pavel
COPY target/pastebox-0.0.1-SNAPSHOT.jar pastebox.jar
ENTRYPOINT ["java", "-jar", "/pastebox.jar"]
