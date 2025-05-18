FROM eclipse-temurin:17-jdk
ARG JAR_FILE=build/libs/auth-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]