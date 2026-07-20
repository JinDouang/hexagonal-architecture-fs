FROM node:24-alpine AS build
WORKDIR /workspace
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build -- --configuration production

FROM nginx:1.29-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /workspace/dist/training-catalogue-front/browser /usr/share/nginx/html
EXPOSE 80