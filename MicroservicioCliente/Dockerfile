# Usa una imagen de Maven con OpenJDK 17
FROM openjdk:17-jdk-alpine

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia los archivos de Maven y el proyecto al contenedor
COPY target/MicroservicioCliente-0.0.1-SNAPSHOT.jar /app/MicroservicioCliente.jar

# Exponer el puerto que la app utiliza
EXPOSE 8080

# Ejecuta el ejecutable .jar
ENTRYPOINT ["java", "-jar", "/app/MicroservicioCliente.jar"]


