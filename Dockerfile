FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package && \
    (cp target/*-SNAPSHOT.jar app.jar || cp target/*.jar app.jar)



FROM eclipse-temurin:21-jre
WORKDIR /app

RUN addgroup --system app && adduser --system --ingroup app app
USER app

COPY --from=build /app/app.jar /app/app.jar
EXPOSE 8080

ENV SERVER_PORT=8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
