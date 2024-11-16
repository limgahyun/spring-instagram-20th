FROM openjdk:17
ARG JAR_FILE=/build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar", "/app.jar"]

FROM mysql:latest
HEALTHCHECK --interval=10s CMD mysqladmin ping -h localhost || exit 1

COPY .env ./