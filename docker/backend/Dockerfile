FROM openjdk:21-ea-bookworm

WORKDIR /app

COPY ../../target/demo-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
