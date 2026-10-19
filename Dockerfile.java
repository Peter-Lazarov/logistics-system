FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace
COPY . .

ARG SERVICE_MODULE
RUN mvn -B -f api-contracts/pom.xml -DskipTests install \
    && mvn -B -f shared-security/pom.xml -DskipTests install \
    && mvn -B -f "${SERVICE_MODULE}/pom.xml" -DskipTests clean package

FROM eclipse-temurin:21-jre
WORKDIR /app
ARG SERVICE_MODULE
COPY --from=build "/workspace/${SERVICE_MODULE}/target/*.jar" /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]