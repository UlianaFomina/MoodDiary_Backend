FROM maven:3.8.3-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
COPY --from=build /target/mood-diary-0.0.1-SNAPSHOT.jar mood-diary.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","mood-diary.jar"]