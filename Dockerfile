# =========================================================================
# ESTÁGIO 1: BUILD
# Usa uma imagem oficial que contém Maven e a versão 21 do Temurin JDK
# =========================================================================
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Copia apenas o pom.xml para aproveitar o cache de camadas do Docker
COPY pom.xml .

# 2. Baixa todas as dependências. Esta camada só será refeita se o pom.xml mudar.
RUN mvn dependency:go-offline -B

# 3. Agora copia o código-fonte. Se só o código mudar, os passos acima usarão o cache.
COPY src ./src

# 4. Compila o código e gera o pacote, pulando os testes que já rodamos no pipeline de CI
RUN mvn clean package -DskipTests

# =========================================================================
# ESTÁGIO 2: RUN
# Usa uma imagem JRE mínima baseada em Alpine para a execução
# =========================================================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Adiciona um grupo e um usuário não-root para rodar a aplicação
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copia o artefato do estágio de build e já define o dono correto (usuário:grupo)
# Usar --chown é mais limpo que adicionar um comando RUN chown ... extra.
COPY --from=build --chown=appuser:appgroup /app/target/*.jar app.jar

# Define o usuário que rodará a aplicação para maior segurança
USER appuser

EXPOSE 8080

# Ponto de entrada para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]