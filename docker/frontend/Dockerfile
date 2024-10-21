FROM node:20-bookworm AS build

# Set the working directory to /app
WORKDIR /app

#### copy both 'package.json' and 'package-lock.json' (if available) from the root (content folder)
COPY ../../Frontend/package*.json ./

RUN npm install -g @angular/cli
RUN npm install

#### go one folder back and copy the entire code from the root
COPY ../ .

RUN npm run build --prod

FROM nginxinc/nginx-unprivileged

#### go one folder back and copy the entire code from the root
COPY ../../Frontend/nginx.conf /etc/nginx/conf.d/default.conf

#### copy artifact build from the 'build environment'
COPY --from=build /app/dist /usr/share/nginx/html

CMD ["nginx", "-g", "daemon off;"]
