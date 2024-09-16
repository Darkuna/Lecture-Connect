# Stage 1: Build Angular project
FROM node:20-bookworm AS build

WORKDIR /app

# Copy the package.json and install dependencies
COPY package*.json ./
RUN npm install

# Copy all source files and build the Angular app
COPY . .
RUN npm run build --prod

# Stage 2: Set up Nginx to serve the Angular app
FROM nginx:alpine

# Copy the Nginx configuration file
COPY nginx.conf /etc/nginx/nginx.conf

# Copy the built Angular app from the build stage
COPY --from=build /app/dist/new_test /usr/share/nginx/html

# Expose port 80 for the web server
EXPOSE 80

# Start Nginx in the foreground
CMD ["nginx", "-g", "daemon off;"]
