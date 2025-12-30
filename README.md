# API de Coupons

API de cupons de desconto desenvolvida como desafio técnico.

## 🚀 Começando

Essas instruções permitirão que você obtenha uma cópia do projeto em operação na sua máquina local para fins de desenvolvimento, teste ou execução em ambiente containerizado com Docker.

### 📋 Pré-requisitos

Você precisará de:

- Java 17 LTS ou superior
- Maven 3.8+ (opcional, se for rodar localmente)
- Docker (opcional, se for rodar em container)

## ▶️ Como executar a aplicação

### ✅ Opção 1: Rodar localmente com Maven

1. Clone o repositório:

   ```bash
   git clone https://github.com/seu-usuario/seu-repositorio.git
   cd seu-repositorio

2. Compile o projeto
    
    ```bash
   ./mvnw clean install

3. Execute a aplicação
    
    ```bash
   ./mvnw spring-boot:run

OBS: No Windows, use mvnw.cmd no lugar de ./mvnw.

### 🐳 Opção 2: Rodar com Docker

1. Gere o arquivo .jar da aplicação:

   ```bash
   ./mvnw clean package -DskipTests

2. Construa a imagem Docker:

    ```bash
   docker build -t coupon-api .

3. Execute o container:

    ```bash
   docker run -p 8080:8080 coupon-api

OBS: A aplicação estará acessível em http://localhost:8080

### 📚 Observações
Swagger – Documentação da API

Interface Swagger UI: http://localhost:8080/swagger-ui/index.html

Documento OpenAPI (JSON): http://localhost:8080/v3/api-docs

### H2 Console – Banco de Dados em Memória
URL de acesso: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:coupondb

Usuário: sa

Senha: (deixe em branco)

### 🧪 Executando os testes

1. Para rodar os testes da aplicação:

   ```bash
   ./mvnw test 

### 🛠️ Construído com

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Security](https://spring.io/projects/spring-security)
- [H2 Database](https://www.h2database.com/html/main.html)
- [Swagger - springdoc-openapi](https://springdoc.org/)
- [Maven](https://maven.apache.org/)
- [Docker](https://www.docker.com/)
