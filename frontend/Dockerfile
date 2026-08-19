# Etapa 1: Construir la app
FROM node:20-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Etapa 2: Servir la app con Nginx
FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html

# LÍNEA AGREGADA: Configuración para SPA
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]