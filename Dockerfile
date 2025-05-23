FROM openjdk:17-jdk-slim AS build

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

FROM openjdk:19-jdk
VOLUME /tmp

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 8080
