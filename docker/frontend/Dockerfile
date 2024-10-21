FROM node:20-bookworm AS build

# Set the working directory to /app
WORKDIR /app

#### copy both 'package.json' and 'package-lock.json' (if available) from the root (content folder)
COPY ./package*.json ./

RUN npm install -g @angular/cli
RUN npm install

COPY . .

RUN npm run build --prod --configuration=production

FROM nginx:alpine

COPY ./nginx.conf /etc/nginx/conf.d/default.conf

#### copy artifact build from the 'build environment'
COPY --from=build /app/dist/le_co_prod /usr/share/nginx/html

CMD ["nginx", "-g", "daemon off;"]
