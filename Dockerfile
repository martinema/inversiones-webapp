FROM eclipse-temurin:11-jre-slim
WORKDIR /app

# Copia el código fuente
COPY pom.xml .
COPY src ./src

# Compila la aplicación
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:11-jre-slim
WORKDIR /app

# Copia el JAR compilado
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
