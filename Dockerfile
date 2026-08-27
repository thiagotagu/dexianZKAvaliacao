FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /workspace

COPY pom.xml ./
COPY src ./src
RUN mvn clean package -DskipTests -B

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

RUN addgroup -S educzk && adduser -S educzk -G educzk

COPY --from=build /workspace/target/educzk-0.0.1-SNAPSHOT.jar app.jar

USER educzk
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
