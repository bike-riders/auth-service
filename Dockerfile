FROM openjdk:17-jdk-slim

ARG WAR_FILE=target/*.war
COPY ${WAR_FILE} app.war

ENTRYPOINT ["java", "-jar", "/app.war"]

