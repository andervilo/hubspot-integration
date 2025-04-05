# HubSpot Integration

Este projeto é uma integração com a plataforma HubSpot, desenvolvida em Java utilizando o framework Spring Boot.

## 🚀 Tecnologias Utilizadas

- Java 17
- Spring Boot 3.2.5
- Spring Cloud 2023.0.0
- Spring Data JPA
- Spring Security
- PostgreSQL
- H2 Database (para desenvolvimento)
- Resilience4j
- OpenFeign
- Lombok
- OpenAPI/Swagger

## 📋 Pré-requisitos

- Java 17 ou superior
- Maven 3.6 ou superior
- PostgreSQL (para ambiente de produção)
- Conta no HubSpot com as credenciais necessárias

## 🔧 Configuração do Ambiente

1. Clone o repositório:
```bash
git clone [URL_DO_REPOSITORIO]
```

2. Configure as variáveis de ambiente:
- Crie um arquivo `.env` na raiz do projeto
- Configure as credenciais do HubSpot e do banco de dados

3. Instale as dependências:
```bash
mvn clean install
```

## 🏃 Executando o Projeto

Para iniciar o projeto em modo de desenvolvimento:

```bash
mvn spring-boot:run
```

O servidor estará disponível em `http://localhost:8080`

## 🌐 URLs do Projeto

- **Desenvolvimento**: `http://localhost:8080`
- **Produção**: `https://hubspot-integration-test-79aef516164b.herokuapp.com`

## 📚 Documentação da API

A documentação da API está disponível através do Swagger UI em:
- **Desenvolvimento**: `http://localhost:8080/swagger-ui/index.html`
- **Produção**: `https://hubspot-integration-test-79aef516164b.herokuapp.com/swagger-ui/index.html`

## 📡 Exemplos de Requisições

### Autenticação
```bash
# Obter URL de autenticação (Desenvolvimento)
curl -X GET "http://localhost:8080/auth/url" \
     -H "Accept: application/json"

# Obter URL de autenticação (Produção)
curl -X GET "https://hubspot-integration-test-79aef516164b.herokuapp.com/auth/url" \
     -H "Accept: application/json"
```

### Contatos
```bash
# Criar um novo contato (Desenvolvimento)
curl -X POST "http://localhost:8080/contacts" \
     -H "Content-Type: application/json" \
     -H "Accept: application/json" \
     -d '{
           "email": "exemplo@email.com",
           "firstName": "João",
           "lastName": "Silva",
           "phone": "+5511999999999",
           "company": "Empresa Exemplo"
         }'

# Criar um novo contato (Produção)
curl -X POST "https://hubspot-integration-test-79aef516164b.herokuapp.com/contacts" \
     -H "Content-Type: application/json" \
     -H "Accept: application/json" \
     -d '{
           "email": "exemplo@email.com",
           "firstName": "João",
           "lastName": "Silva",
           "phone": "+5511999999999",
           "company": "Empresa Exemplo"
         }'
```

## 🔄 Fluxo de Integração

### Webhook de Contatos
O endpoint `/webhook/contacts` é responsável por receber notificações do HubSpot sobre mudanças nos contatos. Este é um fluxo assíncrono que funciona da seguinte forma:

1. **Registro do Webhook**:
   - O webhook é registrado no HubSpot durante o processo de autenticação
   - O HubSpot envia requisições POST para este endpoint sempre que um contato é criado

2. **Processamento do Webhook**:
   ```bash
   # Exemplo de payload recebido do HubSpot
   {
     "subscriptionId": "12345",
     "portalId": "67890",
     "eventId": "98765",
     "subscriptionType": "contact.creation",
     "occurredAt": 1234567890,
     "objectId": 54321,
     "propertyName": "email",
     "propertyValue": "novo@email.com"
   }
   ```

3. **Validação e Segurança**:
   - O endpoint valida a assinatura do webhook usando o header `X-HubSpot-Signature-v3`
   - Verifica se o evento é válido e se o portalId corresponde ao esperado

4. **Ações Realizadas**:
   - Registra o evento para auditoria
   - Pode disparar ações adicionais baseadas no tipo de evento

### Callback de Autenticação
O endpoint `/auth/callback` é parte crucial do fluxo de autenticação OAuth2 com o HubSpot:

1. **Início do Fluxo**:
   - Usuário acessa `/auth/url` para obter a URL de autenticação
   - É redirecionado para a página de login do HubSpot

2. **Processo de Callback**:
   ```bash
   # Exemplo de URL de callback
   https://hubspot-integration-test-79aef516164b.herokuapp.com/auth/callback?code=AUTH_CODE
   ```

3. **Etapas do Callback**:
   - Recebe o código de autorização (`code`) como parâmetro
   - Troca o código por um token de acesso
   - Armazena o token de acesso e refresh token

4. **Tratamento de Erros**:
   - Valida o código recebido
   - Verifica se a requisição é válida
   - Trata possíveis erros de autenticação
   - Registra falhas para monitoramento

5. **Segurança**:
   - Valida o estado (state parameter) para prevenir CSRF
   - Verifica se o callback é de uma origem confiável
   - Implementa rate limiting para prevenir abusos

### Diagrama do Fluxo Completo
```
Usuário -> /auth/url -> HubSpot Login -> /auth/callback -> Token -> Webhook
   ↑                                                           ↓
   └─────────────────────── Contatos ─────────────────────────┘
```

## 🛠️ Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   └── com/hubspot/integration/
│   │       ├── app/                # Camada de aplicação
│   │       │   ├── clients/        # Clientes HTTP e integrações
│   │       │   ├── constants/      # Constantes do sistema
│   │       │   ├── dto/           # Objetos de transferência de dados
│   │       │   └── services/       # Serviços de aplicação
│   │       ├── domain/            # Camada de domínio
│   │       │   └── entities/       # Entidades do domínio
│   │       ├── infra/             # Camada de infraestrutura
│   │       │   ├── configs/        # Configurações do Spring
│   │       │   ├── exception/      # Tratamento de exceções
│   │       │   ├── filters/        # Filtros HTTP
│   │       │   └── repositories/   # Repositórios JPA
│   │       ├── rest/              # Camada de apresentação
│   │       │   ├── advice/         # Manipulador de exceção
│   │       │   ├── AuthController.java
│   │       │   ├── ContactController.java
│   │       │   └── WebhookController.java
│   │       └── IntegrationApplication.java
│   └── resources/
│       ├── application.properties
│       └── application-dev.properties
```

## 🔒 Segurança

O projeto utiliza Spring Security com OAuth2 para autenticação e autorização. Certifique-se de configurar corretamente as credenciais do HubSpot no arquivo de configuração.

## 📦 Build

Para criar o pacote executável:

```bash
mvn clean package
```

O arquivo JAR será gerado em `target/integration-0.0.1-SNAPSHOT.jar`

## ⚙️ Configurações do Application.yml

O arquivo `application.yml` contém as configurações principais do projeto. Abaixo está a explicação de cada seção:

### Configurações do Servidor
```yaml
server:
  port: 8080  # Porta em que a aplicação será executada
```

### Configurações do Banco de Dados
```yaml
spring:
  datasource:
    url: ${DATABASE_URL}           # URL do banco de dados PostgreSQL
    driver-class-name: org.postgresql.Driver
    username: ${DATABASE_USER}     # Usuário do banco de dados
    password: ${DATABASE_PASS}     # Senha do banco de dados
  jpa:
    hibernate:
      ddl-auto: update            # Atualiza o schema do banco automaticamente
    show-sql: true               # Exibe as queries SQL no console
    database-platform: org.hibernate.dialect.PostgreSQLDialect
```

### Configurações do HubSpot
```yaml
hubspot:
  client-id: ${HUBSPOT_CLIENT_ID}        # ID do cliente OAuth2
  client-secret: ${HUBSPOT_CLIENT_SECRET} # Segredo do cliente OAuth2
  redirect-uri: ${HUBSPOT_REDIRECT_URI}   # URI de redirecionamento após autenticação
  scopes: ${HUBSPOT_SCOPES}              # Escopos de acesso solicitados
  auth-url: ${HUBSPOT_AUTH_URL}          # URL de autorização
  token-url: ${HUBSPOT_TOKEN_URL}        # URL para obter tokens
  api-url: ${HUBSPOT_API_URL}            # URL base da API do HubSpot
```

### Configurações de Rate Limiting
```yaml
resilience4j:
  ratelimiter:
    instances:
      hubspotRateLimiter:
        limit-for-period: 190      # Número máximo de requisições
        limit-refresh-period: 10s  # Período de refresh do limite
        timeout-duration: 0        # Tempo de espera para requisições bloqueadas
```

### Configurações do Circuit Breaker
```yaml
circuitbreaker:
  instances:
    hubspotCircuitBreaker:
      register-health-indicator: true
      sliding-window-type: COUNT_BASED
      sliding-window-size: 10
      minimum-number-of-calls: 5
      failure-rate-threshold: 50
      wait-duration-in-open-state: 10s
      permitted-number-of-calls-in-half-open-state: 3
      automatic-transition-from-open-to-half-open-enabled: true
```

### Variáveis de Ambiente Necessárias
As seguintes variáveis de ambiente devem ser configuradas:
- `DATABASE_URL`: URL de conexão com o PostgreSQL
- `DATABASE_USER`: Usuário do banco de dados
- `DATABASE_PASS`: Senha do banco de dados
- `HUBSPOT_CLIENT_ID`: ID do cliente OAuth2 do HubSpot
- `HUBSPOT_CLIENT_SECRET`: Segredo do cliente OAuth2 do HubSpot
- `HUBSPOT_REDIRECT_URI`: URI de redirecionamento após autenticação
- `HUBSPOT_SCOPES`: Escopos de acesso solicitados
- `HUBSPOT_AUTH_URL`: URL de autorização do HubSpot
- `HUBSPOT_TOKEN_URL`: URL para obter tokens do HubSpot
- `HUBSPOT_API_URL`: URL base da API do HubSpot 