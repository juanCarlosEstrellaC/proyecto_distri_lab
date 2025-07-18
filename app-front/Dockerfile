FROM node:20-alpine AS builder

WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM nginx:alpine

COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY entrypoint.sh /entrypoint.sh

RUN chmod +x /entrypoint.sh

EXPOSE 80

# Comando para iniciar nginx con configuración dinámica
CMD ["/entrypoint.sh"]

############################ Comandos para construir la imagen Docker ##############################
#
#       docker build -t jcestrella/app-front .
#       docker push jcestrella/app-front
