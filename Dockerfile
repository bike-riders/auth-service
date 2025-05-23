FROM openjdk:17-jdk-slim
RUN pwd
RUN ls -lrth
# Build context ke root me 'target' folder ko check karne ke liye
RUN ls -l target || echo "target folder not found"

ARG WAR_FILE=target/*.war
COPY ${WAR_FILE} app.war

ENTRYPOINT ["java", "-jar", "/app.war"]
