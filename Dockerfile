# =========================
# Stage 1: build (Maven)
# =========================
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# cache de dependências
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# build do projeto
COPY src ./src
RUN mvn -q -DskipTests clean package

# =========================
# Stage 2: runtime (JRE)
# =========================
FROM eclipse-temurin:17-jre
WORKDIR /app

# segurança básica
RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

# copia o jar gerado
COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
