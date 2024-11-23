FROM openjdk:17
ARG JAR_FILE=/build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar", "/app.jar"]

FROM mysql:latest
HEALTHCHECK --interval=10s CMD mysqladmin ping -h database-2.cjqwug44qqcy.ap-northeast-2.rds.amazonaws.com || exit 1