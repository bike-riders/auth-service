# Use OpenJDK 17 slim image
FROM openjdk:17-jdk-slim

# Argument for jar file
ARG JAR_FILE=target/*.jar

# Copy the jar file into the container
COPY ${JAR_FILE} app.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app.jar"]
