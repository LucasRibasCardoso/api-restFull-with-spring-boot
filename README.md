# API RestFull

Essa é uma API desenvolvida com o objetivo de implementar todos os princípios rest, tornando-a uma API Restfull. Foi utilizado Spring Framework e também foi implementada uma pipeline de CI para buidar a imagem da aplicação e enviar para o DockerHub.

## **🚀 Funcionalidades**

**Core Features**

- ✅ CRUD de Pessoas: Endpoints para realizar operações CRUD de pessoas.
- ✅ Importação e Exportação: É possivel exportar e importar os dados de pessoas para arquivos .xlsx e .csv.
- ✅ CRUD de Livros: Endpoints para realizar operações CRUD de livros.
- ✅ Gerenciamento de Arquivos: Endpoints para realizar upload e download de arquivos.
    - [ ]  Importar multiplos arquivos.
    - [ ]  Importar um unico arquivo.
    - [ ]  Download.
- ✅ Envio de e-mail: Endpoints para realizar o envio de e-mails simples e com anexos via gmail.
- ✅ Autenticação: Endpoints para realizar o SignIn, SignUp e obter o refresh token.

**Recursos Técnicos**

- 🔒 **Segurança**: Spring Security e JWT para autenticar usuários.
- 📊 **Monitoramento**: Spring Boot Actuator para métricas e health checks.
- 🗄️ **Persistência**: Migrations com Flyway e JPA/Hibernate com suporte a postgreSQL.
- 📊 **Documentação**: Swagger para documentar todos os endpoints.

## **🏗️ Arquitetura**

Estrutura do Projeto

```markdown
src/main/java/com/LucaRibasCardoso/api_rest_with_spring_boot
├── config/         # Configurações swagger, email, files, security...
├── controller/     # Controllers REST
├── docs/           # Interfaces de documentação para os controllers
├── exception/      # Exceptions e HandlerException Global
├── file/           # Implementação exporter e importer
├── mail/           # Implementação para enviar e-mails
├── dto/            # Data Transfer Objects
├── entity/         # Entidades JPA
├── mapper/         # MapStruct mappers
├── repository/     # Repositórios JPA
├── service/        # Lógica de negócio
└── ApiRestWithSpringBootApplication.java
```

## **🛠️ Tecnologias**

- **Java 21** - Linguagem principal
- **Spring Boot 3.4.0** - Framework base
- **Spring Hateoas** - Hateoas
- **Spring Data JPA** - Persistência
- **Spring Security** - Segurança
- **Spring Actuator**: Monitoramento
- **Json Web Token:** Segurança
- **Spring Mail -** Emails
- **MapStruct** - Mapeamento de objetos
- **PostgreSQL** - Banco de dados
- **FlyWay** - Migrations
- **Docker**: Containers
- **GitHub Actions**: Continuos Integrations
- **Maven** - Gerenciamento de dependências

## **🚦 Endpoints**

**Pessoas**

```bash
# Buscar todas as pessoas
GET /api/v1/person

# Cadastrar uma pessoa
POST /api/v1/person
Content-Type: JSON, XML e YML

# Cadastrar pessoas atráves de um arquivo .xlsx ou .csv
POST /api/v1/person/massCreation
Content-Type: file

# Buscar por uma pessoas específica
GET /api/v1/person/{id}

# Deletar uma pessoa
DELETE /api/v1/person/{id}

# Atualizar dados de uma pessoa
PATCH /api/v1/person/{id}
Content-Type: JSON, XML e YML

# Habilitar o cadastro de uma pessoa
PATCH /api/v1/person/enable/{id}

# Desabilitar o cadastro de uma pessoa
/api/v1/person/disable/{id}

# Buscar todas as pessoas filtrando pelo nome
GET /api/v1/person/findByName/{firstName}

# Exportar cadastros de pessoas para um arquivo .xlsx ou .csv
GET /api/v1/person/exportPage
```

**Livros**

```bash
# Buscar todos os livros
GET /api/v1/books

# Cadastrar um livro
POST /api/v1/books
Content-Type: JSON, XML e YML

# Buscar por livro especifico
GET /api/v1/books{id}

# Deletar uma livro
DELETE /api/v1/books{id}

# Atualizar atributos de um livro
PATCH /api/v1/books/{id}
Content-Type: JSON, XML e YML
```

**Email**

```bash
# Enviar um email
POST /api/v1/email
Content-Type: JSON

# Enviar um email com anexos
POST /api/v1/email/withAttachment
Content-Type: JSON, File
```

**Files**

```bash
# Upload de multiplos arquivos
POST /api/v1/files/uploadFiles
Content-Type: Files

# Upload de um arquivo
POST /api/v1/files/uploadFile
Content-Type: File

# Download de um arquivo
GET /api/v1/files/download/{fileName}
```

**Autenticação**

```bash
# SignIn
POST /auth/signin
Content-Type: JSON, XML e YML

# SignUp
POST /auth/signup
Content-Type: JSON, XML e YML

# Obter o refresh token
PUT /auth/refresh-token/{username}
```

Documentação

```bash
# Acesse a documentação via swagger
http://localhost:8080/swagger-ui/
```

## **📋 Configuração**

**Váriaveis de Ambiente**

```bash
# Database configuration
DB_HOST=postgres    # manter esse host
POSTGRES_DB=MyAppDB # manter esse nome do banco
POSTGRES_USER=user
POSTGRES_PASSWORD=passowrd

# Email configuration
EMAIL_USERNAME=email@gmail.com
EMAIL_PASSWORD=senha-de-app-para-o-gmail

JWT_SECRET_KEY=chave-de-criptografia-para-o-jwt
JWT_EXPIRATION_MS=3600000

# Password Encoder configuration
PASSWORD_ENCODER_SECRET=chave-de-criptografia-para-o-passowrd-encoder
PASSWORD_ENCODER_ITERATIONS=185000
```

## **🚀 Execução**

```bash
# [1] Clonar repositório
git clone <repository-url>

# [2] Configurar as variaveis de ambiente

# [3] Execute o comando docker dentro da pasta do projeto
docker compose --profile full up -d
```