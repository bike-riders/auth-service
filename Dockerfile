FROM openjdk:17-jdk-slim

RUN ls -lrth
WORKDIR /app

RUN ls -lrth

COPY pom.xml .

RUN ls -lrth
COPY src src


RUN ls -lrth
# Copy Maven wrapper
COPY mvnw .
COPY .mvn .mvn


RUN ls -lrth
# Set execution permission for the Maven wrapper
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

RUN ls -lrth

ARG WAR_FILE=target/*.war
COPY ${WAR_FILE} app.war

ENTRYPOINT ["java", "-jar", "/app.war"]
