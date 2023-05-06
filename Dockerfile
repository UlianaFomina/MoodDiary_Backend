FROM eclipse-temurin:17-jdk-alpine
COPY target/*.jar mood-diary.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","mood-diary.jar"]
