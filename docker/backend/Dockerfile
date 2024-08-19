# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-ea-bookworm

# Set the working directory in the container
WORKDIR /app

# Copy the jar file into the container at /app
COPY ../../target/demo-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
