############################ Dockerfile para la aplicacion de Books ##############################
# Descarga una distribución de Linux ligera (Noble) que ya trae el JRE de OpenJDK (Eclipse Temurin),
# Luego copia los archivos build generados de en la aplicación al contenedor y la ejecuta con
# java -jar app.jar.

FROM eclipse-temurin:21.0.7_6-jre-noble
ENV LANGUAGE='en_US:en'

# Se crea un directorio para la aplicacion
RUN mkdir /app
WORKDIR /app

RUN apt update
RUN apt install -y iputils-ping

# Copiar los archivos de la aplicacion al contenedor
# Se pone primero la capa de dependencias o lib para que se pueda reutilizar.
COPY build/quarkus-app/lib/ ./lib
COPY build/quarkus-app/quarkus ./quarkus
COPY build/quarkus-app/app/ ./app
COPY build/quarkus-app/quarkus-run.jar app.jar
#El nombre del jar lo cambiamos a app.jar

CMD ["java", "-jar", "app.jar"]

############################ Comandos para construir la imagen Docker ##############################

#       docker build -t jcestrella/app-books .
#       docker push jcestrella/app-books
#       docker build -t jcestrella/app-books:version_health . && docker push jcestrella/app-books

