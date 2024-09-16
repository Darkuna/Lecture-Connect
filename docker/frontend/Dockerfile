FROM node:20-bookworm AS build

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build --prod
RUN chmod -R 755 /usr/share/nginx/html

FROM nginx:alpine

COPY --from=build /app/dist/new_test /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
