FROM openjdk:17-jdk-alpine
WORKDIR /app

COPY build/libs/my-security.jar /app/my-security.jar
EXPOSE 8081

ENTRYPOINT [ "java", "-jar","/app/my-security.jar" ]