# Usa una imagen de Maven para construir y ejecutar el proyecto
FROM maven:3.8.6-openjdk-17

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia los archivos de Maven y el proyecto al contenedor
COPY . .

# Exponer el puerto que la app utiliza
EXPOSE 8080

# Ejecuta la aplicación usando Maven con Spring Boot
CMD ["mvn", "spring-boot:run"]
