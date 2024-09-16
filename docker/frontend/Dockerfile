# Stage 1: Build the Angular project
FROM node:20-bookworm AS build

WORKDIR /app

# Install dependencies
COPY package*.json ./
RUN npm install

# Build the Angular project
COPY . .
RUN npm run build --prod

# Stage 2: Nginx setup
FROM nginx:alpine

# Copy the Nginx configuration file
COPY nginx.conf /etc/nginx/nginx.conf

# Copy the Angular build output to the Nginx HTML directory
COPY --from=build /app/dist/new_test /usr/share/nginx/html

# Set correct permissions for the Angular build files
RUN chmod -R 755 /usr/share/nginx/html

# (Optional) If using a non-root user:
# Create a non-root user
RUN addgroup -S nginxgroup && adduser -S nginxuser -G nginxgroup

# Adjust ownership to the non-root user
RUN chown -R nginxuser:nginxgroup /usr/share/nginx/html

# Switch to non-root user (optional step for security)
USER nginxuser

# Expose port 80
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
