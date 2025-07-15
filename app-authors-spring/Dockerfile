# Dockerfile para la aplicación de Autores con Spring Boot
FROM eclipse-temurin:21.0.7_6-jre-alpine-3.21

ENV LANGUAGE='en_US:en'

# Se crea un directorio para la aplicacion
RUN mkdir /app
WORKDIR /app

# Copiar el jar de la aplicación
COPY build/libs/app-authors-spring-1.0.0.jar app.jar

# Exponer el puerto
EXPOSE 8080

# Ejecutar la aplicación
CMD ["java", "-jar", "app.jar"]

# Comandos para construir la imagen Docker:
# 1. Navegar al directorio donde se encuentra este Dockerfile.
# 2. Ejecutar el comando:
#    docker build -t app-authors-spring .
#
# Para subir a Docker Hub:
#    docker build -t jcestrella/app-authors-spring .
#    docker build -t jcestrella/app-authors-spring:spring-version .
