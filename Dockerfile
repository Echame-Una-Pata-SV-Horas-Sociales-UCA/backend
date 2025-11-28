# 🔥 Etapa 1: Compila el proyecto usando Gradle
FROM gradle:8.7-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle build -x test --no-daemon

# 🔥 Etapa 2: Imagen final limpia y ligera
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
