FROM openjdk:11 AS builder
ARG JAR_FILE="./target/book-shop-0.0.1-SNAPSHOT.jar"
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM openjdk:11

RUN mkdir /app

ARG CONF_FILE="./docker/application.properties"
COPY ${CONF_FILE} /app/app.properties

EXPOSE 8085

COPY --from=builder dependencies/ /app/
COPY --from=builder snapshot-dependencies/ /app/
COPY --from=builder spring-boot-loader/ /app/
COPY --from=builder application/ /app/

WORKDIR /app

ENTRYPOINT ["java", "-Dspring.config.location=/app/app.properties", "org.springframework.boot.loader.JarLauncher"]