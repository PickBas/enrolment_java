FROM openjdk:18-jdk-alpine as build

ARG JAR_FILE=/src/main/docker/*.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]