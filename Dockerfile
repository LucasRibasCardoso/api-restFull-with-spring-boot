# =========================================================================
# ESTÁGIO 1: BUILD
# Usa uma imagem oficial com Maven e JDK 21. Fixar a versão (3.9.4) é
# ligeiramente melhor para builds mais consistentes.
# =========================================================================
FROM maven:3.9.4-eclipse-temurin-21 AS builder

# Define o diretório de trabalho
WORKDIR /app

# Copia o pom.xml e baixa as dependências para otimizar o cache
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código-fonte
COPY src ./src

# Compila a aplicação, gerando o .jar e pulando os testes
RUN mvn clean package -DskipTests


# =========================================================================
# ESTÁGIO 2: RUN
# Imagem final, mínima e segura, baseada em Alpine com JRE.
# =========================================================================
FROM eclipse-temurin:21-jre-alpine

# Instala 'curl', necessário para o comando do HEALTHCHECK.
RUN apk add --no-cache curl

# Cria um grupo e um usuário não-root para rodar a aplicação com segurança.
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Define o diretório de trabalho no estágio final
WORKDIR /app

# Copia o .jar do estágio 'builder' e já define o dono correto em um único passo.
# Esta é a forma mais eficiente e moderna.
COPY --from=builder --chown=appuser:appgroup /app/target/*.jar app.jar

# Define o usuário não-root que executará a aplicação.
USER appuser

# Expõe a porta que a aplicação usa.
EXPOSE 8080

# --- CONFIGURAÇÕES CRÍTICAS PARA PRODUÇÃO ---

# 1. Health Check: Informa ao Docker o estado real da aplicação.
# O orquestrador usará isso para gerenciar o contêiner.
# --interval: checa a cada 30s
# --timeout: considera falha se demorar mais de 3s
# --start-period: aguarda 60s após o início antes de começar a checar (dá tempo da app subir)
# --retries: tenta 3 vezes antes de marcar como 'unhealthy'
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# 2. Opções da JVM: Otimiza o uso de memória em ambientes de contêiner.
# Essencial para evitar que a aplicação seja morta por excesso de memória (OOMKilled).
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC"

# 3. Ponto de Entrada: Executa a aplicação usando um shell para que as
# variáveis de ambiente ($JAVA_OPTS) sejam processadas.
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]