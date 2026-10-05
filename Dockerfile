# Etapa 1: Maven + Java 21
FROM maven:3-eclipse-temurin AS build
WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

# Etapa 2: Runtime con Java 21
FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
