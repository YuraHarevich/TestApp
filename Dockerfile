FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/TestApp-0.0.1-SNAPSHOT.jar project.jar
ENTRYPOINT ["java", "-jar", "project.jar"]