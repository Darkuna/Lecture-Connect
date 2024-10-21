FROM node:20-bookworm AS build

# Set the working directory to /app
WORKDIR /app


#### copy both 'package.json' and 'package-lock.json' (if available) from the root (content folder)
COPY ./package*.json ./

RUN npm install -g @angular/cli
RUN npm install

COPY . .

RUN npm run build --prod

FROM nginx

COPY --from=build-stage ./nginx.conf /etc/nginx/conf.d/default.conf

#### copy artifact build from the 'build environment'
COPY --from=build-stage /app/dist /usr/share/nginx/html

CMD ["nginx", "-g", "daemon off;"]
