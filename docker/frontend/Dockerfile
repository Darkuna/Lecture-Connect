FROM node:20-bookworm AS build

WORKDIR /app/src/app

#### copy both 'package.json' and 'package-lock.json' (if available)
COPY package*.json ./
RUN npm RUN npm install -g @angular/cli
RUN npm install

COPY . .
RUN npm run build --prod

FROM nginxinc/nginx-unprivileged

COPY ./nginx.conf /etc/nginx/conf.d/default.conf

#### copy artifact build from the 'build environment'
COPY --from=build /usr/src/app/dist /usr/share/nginx/html

CMD ["nginx", "-g", "daemon off;"]
