FROM node:20-bookworm AS build

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build --prod

FROM nginx:alpine

COPY --from=build /app/dist/new_test /var/www/html/public/

EXPOSE 80

CMD ["chmod", "-R", "755", "/var/www/html/public/"]
CMD ["nginx", "-g", "daemon off;"]
