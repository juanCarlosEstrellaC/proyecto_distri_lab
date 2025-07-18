############################ Dockerfile para la aplicacion Frontend ##############################

FROM node:20-alpine AS build-stage

WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM nginx:1.24-alpine AS production-stage
COPY --from=build-stage /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]

############################ Comandos para construir la imagen Docker ##############################
#  1. Navegar al directorio donde se encuentra este Dockerfile.
#  2. Ejecutar el comando:
#       docker build -t app-front . (el punto indica que se debe buscar el Dockerfile en el directorio actual)
#
#     O para subir a Docker Hub, usar:
#       docker build -t jcestrella/app-front . (colocar el punto al final para indicar el contexto de construcción)
#       docker build -t jcestrella/app-front:latest .
#
#  3. Para subir la imagen a Docker Hub, usar:
#       docker push jcestrella/app-front
#       docker push jcestrella/app-front:latest
#

