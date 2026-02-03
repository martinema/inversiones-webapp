FROM maven:3.8-openjdk-11 AS build
WORKDIR /app

# Copia el código fuente
COPY pom.xml .
COPY src ./src

# Compila la aplicación
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM arm64v8/openjdk:11
WORKDIR /app

# Copia el JAR compilado
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
