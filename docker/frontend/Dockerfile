FROM node:20-bookworm AS build

WORKDIR /app

COPY package*.json ./
COPY nginx.conf /etc/nginx/conf.d/default.conf
RUN npm install

COPY . .
RUN npm run build --prod
RUN chmod -R 777 /usr/share/nginx/html

FROM nginx:alpine

COPY --from=build /app/dist/new_test /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
