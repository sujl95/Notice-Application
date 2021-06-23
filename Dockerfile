FROM openjdk:11.0.11-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /app/notice-application.jar
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "/app/notice-application.jar"]