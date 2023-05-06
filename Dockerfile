FROM maven:3.8.3-openjdk-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

#
# Package stage
#
FROM eclipse-temurin:17-jdk-alpine
COPY --from=build /home/app/target/mood-diary-0.0.1-SNAPSHOT.jar /usr/local/lib/mood-diary.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/mood-diary.jar"]
