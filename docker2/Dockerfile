# Build phase for Angular (Frontend)
FROM node:20-bookworm AS frontend-build

WORKDIR /app

# Install Angular dependencies
COPY ./Frontend/package*.json ./
RUN npm install

COPY ./Frontend ./
RUN npm run build --prod

# Build phase for Spring Boot (Backend)
FROM openjdk:21-ea-bookworm AS backend-build

WORKDIR /app

# Copy the Spring Boot JAR file
COPY ../../target/demo-0.0.1-SNAPSHOT.jar /app/app.jar

# Production image with Nginx and Spring Boot
FROM nginx:alpine

# Copy the built frontend files to Nginx's serving directory
COPY --from=frontend-build /app/dist/new_test/browser /usr/share/nginx/html

# Copy the built Spring Boot JAR file
COPY --from=backend-build /app/app.jar /app/app.jar

# Expose the necessary ports
EXPOSE 80 8080

# Start Spring Boot in the background and then run Nginx
CMD java -jar /app/app.jar & nginx -g 'daemon off;'
