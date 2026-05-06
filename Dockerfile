FROM maven:3.8-openjdk-11 AS build
WORKDIR /app

# Paso 1: Copiar pom.xml (se cachea esta capa)
COPY pom.xml .

# Paso 2: Descargar dependencias en caché (solo se ejecuta si pom.xml cambia)
RUN mvn dependency:resolve dependency:resolve-plugins

# Paso 3: Copiar código fuente
COPY src ./src

# Paso 4: Compilar (usa caché de dependencias del paso 2)
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM arm64v8/openjdk:11
WORKDIR /app

# Copia el JAR compilado
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
