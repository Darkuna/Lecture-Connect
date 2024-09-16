# Stage 1: Build the Angular project
FROM node:20-bookworm AS build

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build --prod

# Stage 2: Nginx setup
FROM nginx:alpine

COPY nginx.conf /etc/nginx/nginx.conf

# Copy the Angular build output
COPY --from=build /app/dist/new_test /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
