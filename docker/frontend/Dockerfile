# Use an official Node.js runtime as a parent image
FROM node:20-bookworm AS build

# Set the working directory
WORKDIR /app

# Copy package.json and install dependencies
COPY package*.json ./
RUN npm install

# Copy the rest of the application files and build the Angular app
COPY . .
RUN npm run build --prod

# Use an Nginx image to serve the Angular app
FROM nginx:alpine

# Copy the Angular build output to the Nginx HTML directory
COPY --from=build /app/dist/your-angular-app /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
