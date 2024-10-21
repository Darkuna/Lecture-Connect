FROM node:20-bookworm AS build

# Set the working directory to /app
WORKDIR /app

#### copy both 'package.json' and 'package-lock.json' (if available) from Frontend root
COPY package*.json ./

RUN npm install -g @angular/cli
RUN npm install

#### Copy the entire Frontend folder (not just src)
COPY . .

RUN npm run build --prod

FROM nginxinc/nginx-unprivileged

COPY ./nginx.conf /etc/nginx/conf.d/default.conf

#### copy artifact build from the 'build environment'
COPY --from=build /app/dist /usr/share/nginx/html

CMD ["nginx", "-g", "daemon off;"]
