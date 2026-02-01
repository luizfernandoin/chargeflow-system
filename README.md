# ChargeFlow - Sistema de Gerenciamento de Cobranças

<div align="center">
  <img alt="Version" src="https://img.shields.io/badge/version-1.0.0-blue.svg?cacheSeconds=2592000" />
  <img alt="Java" src="https://img.shields.io/badge/Java-21-red" />
  <img alt="Spring Boot" src="https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen" />
</div>

## Sumário

- [Sobre o projeto](#sobre)
- [Tecnologias](#tecnologias)
- [Requisitos](#requisitos)
- [Instalação](#instalação)
- [Como Iniciar](#como-iniciar)
- [Configurações](#configurações)
- [Endpoints da API](#endpoints-da-api)
- [Webhook Asaas](#webhook-asaas)
- [Arquitetura](#arquitetura)

---

## Sobre o Projeto

**ChargeFlow** é um sistema de gerenciamento de cobranças integrado com a plataforma Asaas. Permite criar, monitorar e processar cobranças com suporte a múltiplos tipos de pagamento (PIX, Boleto e Cartão de Crédito).

O sistema utiliza uma arquitetura de **microserviços** com:
- **charge-proxy**: Serviço proxy que intermedia chamadas SOAP com o Asaas
- **charge-manager**: Serviço principal que gerencia cobranças e notificações
- **PostgreSQL**: Banco de dados para persistência
- **Docker Swarm**: Orquestração de containers em ambiente distribuído
- **Vagrant**: Gerenciamos de VMs para simulação de SD
---

## Tecnologias

### Backend
- **Java 21** com Spring Boot 3.3.4
- **Spring Data JPA** para persistência
- **Spring WS** para SOAP
- **Hibernate 6.5** com suporte a JSONB
- **Maven** para build

### Banco de Dados
- **PostgreSQL 15** com suporte a JSONB
- **Flyway** para migrations

### Infraestrutura
- **Docker** e **Docker Compose** para containerização
- **Docker Swarm** para orquestração
- **Vagrant** para VMs
- **VirtualBox** para virtualização

### Email
- **Thymeleaf** para templates
- **Spring Mail** para envio

### Integração
- **Asaas API** para processamento de pagamentos
- **WSDL/SOAP** para comunicação entre serviços

---

## Requisitos

### Sistemas Operacionais Suportados
- **Linux** (Ubuntu 20.04+)
- **macOS** (10.15+)
- **Windows 11** (com WSL2 + Ubuntu)

### Dependências Obrigatórias
- **Docker** 20.10+
- **Docker Compose** 1.29+
- **VirtualBox** 6.1+
- **Vagrant** 2.3+
- **Git** 2.30+

### Opcionais
- **Maven** 3.8+ (pré-instalado via `mvnw`)
- **Java 21** (incluído nos Dockerfiles)

---

## Instalação

### 1. Clonar o Repositório

```bash
git clone <repositório>
cd final-project
```

### 2. Configurar Arquivo `.env`

Copie o arquivo de exemplo e configure:

```bash
cp .env.example .env
```

Edite `.env` com suas credenciais:

```env
# Asaas Configuration
ASAAS_BASE_URL=https://sandbox.asaas.com/api/v3
ASAAS_API_KEY=sua_chave_api_aqui
ASAAS_WEBHOOK_TOKEN=seu_token_webhook_aqui

# Email Configuration
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=seu_email@gmail.com
MAIL_PASSWORD=sua_senha_app_aqui

# Database
DB_HOST=postgres
DB_PORT=5432
DB_NAME=chargerdb
DB_USER=postgres
DB_PASSWORD=sua_senha_aqui

# Manager SOAP
MANAGER_SOAP_URL=http://charge-manager:8081/ws
```

### 3. Inicializar VMs (Opcional)

Se usando Vagrant para ambiente isolado:

```bash
cd vagrant
vagrant up
```

---

## Como Iniciar

### Opção 1: Script Automático (Linux/macOS)

```bash
./start.sh
```

O script vai:
1. Configurar Docker no host
2. Subir as VMs com Vagrant
3. Fazer build das imagens
4. Fazer deploy em Docker Swarm
5. Aguardar serviços ficarem prontos

### Opção 2: Windows com WSL2

```bash
# Abrir WSL2 (Ubuntu) na raiz do projeto
wsl
./start.sh
```

### Opção 3: Docker Compose Local (Desenvolvimento)

```bash
docker-compose up -d
```

### Opção 4: Build e Deploy Manual

```bash
# Build das imagens
docker build -t charge-proxy:latest charge-proxy
docker build -t charge-manager:latest charge-manager

# Deploy em Swarm
docker stack deploy -c docker-compose.yml chargeflow
```

---

## Configurações

### Variáveis de Ambiente

| Variável | Descrição | Exemplo |
|----------|-----------|---------|
| `ASAAS_BASE_URL` | URL base da API Asaas | `https://sandbox.asaas.com/api/v3` |
| `ASAAS_API_KEY` | Chave de API do Asaas | `aak_...` |
| `ASAAS_WEBHOOK_TOKEN` | Token para validar webhooks | `token_...` |
| `MAIL_HOST` | Host do servidor SMTP | `smtp.gmail.com` |
| `MAIL_PORT` | Porta SMTP | `587` |
| `MAIL_USERNAME` | Email para autenticação | `seu@email.com` |
| `MAIL_PASSWORD` | Senha do email | `app_password` |
| `MANAGER_SOAP_URL` | URL do serviço SOAP do Manager | `http://charge-manager:8081/ws` |

### Propriedades da Aplicação

Edite `charge-manager/src/main/resources/application.properties`:

```properties
# Server
server.port=8081
spring.application.name=charge-manager

# Database
spring.datasource.url=jdbc:postgresql://postgres:5432/chargerdb
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# Email
spring.mail.host=${MAIL_HOST}
spring.mail.port=${MAIL_PORT}
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

---

## 📁 Estrutura do Projeto

```
final-project/
├── charge-manager/              # Serviço principal
│   ├── src/main/java/com/dac/charge_manager/
│   │   ├── api/                 # Controllers REST
│   │   ├── business/            # Lógica de negócio
│   │   │   ├── charge/          # Serviço de cobranças
│   │   │   └── asaas/           # Integração Asaas
│   │   ├── infra/               # Camada de infraestrutura
│   │   │   ├── soap/            # Cliente SOAP
│   │   │   └── email/           # Serviço de email
│   │   └── persistence/         # JPA entities e repositories
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   ├── templates/email/     # Templates Thymeleaf
│   │   └── wsdl/                # Contratos WSDL
│   └── pom.xml
│
├── charge-proxy/                # Serviço proxy SOAP
│   ├── src/main/java/com/dac/charge_proxy/
│   │   ├── controller/          # Endpoints
│   │   ├── soap/                # Cliente Asaas SOAP
│   │   └── business/            # Lógica de webhook
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   └── wsdl/                # Contratos WSDL
│   └── pom.xml
│
├── vagrant/                     # Configuração de VMs
│   ├── Vagrantfile
│   ├── scripts/                 # Scripts de provisioning
│   └── stacks/                  # Docker stack files
│
├── scripts/                     # Scripts utilitários
│   └── init-db.sql              # Inicialização do banco
│
├── docker-compose.yml           # Composição local
├── start.sh                     # Script de inicialização
├── rebuild.sh                   # Script de rebuild
└── README.md                    # Este arquivo
```

---

## Endpoints da API

### Clientes

#### Criar Cliente
```bash
POST http://localhost:8081/clients
Content-Type: application/json

{
  "name": "João Silva",
  "email": "joao@email.com",
  "cpfCnpj": "111.444.777-35"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "João Silva",
  "email": "joao@email.com",
  "cpfCnpj": "111.444.777-35",
  "createdAt": "2026-02-01T10:23:00"
}
```

**Nota**: O email de boas-vindas é enviado automaticamente após a criação.

---

### Cobranças

#### Criar Cobrança
```bash
POST http://localhost:8081/charges
Content-Type: application/json

{
  "clientId": 1,
  "value": 1500.50,
  "type": "PIX",
  "dueDate": "2026-03-15",
  "creditCardToken": null
}
```

**Parâmetros:**
- `clientId` (Long) - ID do cliente (obrigatório)
- `value` (Double) - Valor da cobrança (obrigatório)
- `type` (ChargeType) - Tipo de cobrança: `PIX`, `BOLETO` ou `CARTAO_CREDITO` (obrigatório)
- `dueDate` (LocalDate) - Data de vencimento no formato YYYY-MM-DD (obrigatório)
- `creditCardToken` (String) - Token do cartão (obrigatório apenas para tipo CARTAO_CREDITO)

**Response (201 Created):**
```json
{
  "id": 1,
  "value": 1500.50,
  "type": "PIX",
  "status": "PENDING",
  "asaasId": "pay_8pbflpj7cog084m3",
  "client": {
    "id": 1,
    "name": "João Silva",
    "email": "joao@email.com",
    "cpfCnpj": "111.444.777-35"
  },
  "createdAt": "2026-02-01T10:23:00"
}
```

**Email Enviado**: Email de confirmação é enviado automaticamente com detalhes do pagamento (QR Code para PIX, Boleto para Boleto, etc).

#### Cancelar Cobrança
```bash
POST http://localhost:8081/charges/{id}/cancel
```

**Parâmetros:**
- `id` (Long) - ID da cobrança na URL (obrigatório)

**Response:**
```json
{
  "id": 1,
  "value": 1500.50,
  "type": "PIX",
  "status": "CANCELED",
  "asaasId": "pay_8pbflpj7cog084m3",
  "client": {
    "id": 1,
    "name": "João Silva"
  },
  "createdAt": "2026-02-01T10:23:00"
}
```

**Nota**: Um email de cancelamento é enviado para o cliente.

---

## Webhook Asaas

### Configuração

1. Acesse o painel do Asaas
2. Vá em **Configurações** → **Webhooks**
3. Cadastre uma nova URL:
   - **URL**: `https://seu-dominio.com/webhook/asaas`
   - **Token**: Configure no arquivo `.env` (`ASAAS_WEBHOOK_TOKEN`)

### Eventos Suportados

- `PAYMENT_RECEIVED` - Pagamento recebido
- `PAYMENT_CREATED` - Pagamento criado
- `PAYMENT_CONFIRMED` - Pagamento confirmado
- `PAYMENT_OVERDUE` - Pagamento atrasado

### Exemplo de Payload

```json
{
  "id": "evt_xxx",
  "event": "PAYMENT_RECEIVED",
  "dateCreated": "2026-02-01 10:23:00",
  "account": {
    "id": "057ccdd6-ba38-4618-bfb5-83dd81394df0"
  },
  "payment": {
    "id": "pay_xxx",
    "value": 1500.50,
    "status": "RECEIVED",
    "billingType": "PIX"
  }
}
```

### Teste com ngrok (Desenvolvimento)

```bash
# Instalar ngrok
# https://ngrok.com/download

# Expor porta local
ngrok http 8080

# Usar a URL gerada no webhook Asaas
# https://seu-ngrok-url.ngrok-free.dev/webhook/asaas
```

---

## Arquitetura

### Padrões de Design

#### Strategy Pattern
Implementado para gerenciar diferentes tipos de pagamento:
- `ChargeDetailStrategy` - Interface
- `PixChargeDetail` - Implementação PIX
- `BoletoChargeDetail` - Implementação Boleto
- `CreditCardChargeDetail` - Implementação Cartão

#### Observer Pattern
Usado para processar eventos do webhook Asaas:
- `WebhookEventObserver` - Interface
- `AsaasEventService` - Observer que persiste eventos
- `ChargeStatusUpdateObserver` - Observer que atualiza cobranças

#### SOAP Client Pattern
Comunicação entre serviços via WSDL:
- `ChargeProxyEndpoint` - Endpoint SOAP no proxy
- `ChargeProxyClient` - Cliente SOAP no manager

### Fluxo de Cobrança

```
1. Cliente cria cobrança via API REST
   ↓
2. ChargeService valida dados
   ↓
3. ChargeService chama ChargeProxyClient (SOAP)
   ↓
4. ChargeProxyClient faz chamada ao Asaas API
   ↓
5. Asaas retorna ID de pagamento (asaasId)
   ↓
6. ChargeService enriquece detalhes com strategy
   ↓
7. ChargeService persiste cobrança e detalhes (JSONB)
   ↓
8. ChargeService envia email de confirmação
   ↓
9. Webhook Asaas notifica pagamento (PAYMENT_RECEIVED)
   ↓
10. AsaasWebhookController persiste evento
    ↓
11. Observers processam evento
    ↓
12. ChargeStatusUpdateObserver atualiza status para PAID
```

---

## Email

### Templates Disponíveis

- `charge-created.html` - Confirmação de cobrança criada
- `payment-received.html` - Comprovante de pagamento
- `welcome.html` - Email de boas-vindas
- `charge-canceled.html` - Notificação de cancelamento

### Variáveis Disponíveis

Os templates recebem:
- `clientName` - Nome do cliente
- `chargeType` - Tipo de cobrança (PIX, BOLETO, CARTAO_CREDITO)
- `dueDate` - Data de vencimento
- `value` - Valor da cobrança
- Variáveis específicas do tipo de pagamento

---

## Referências

- [Asaas API Docs](https://docs.asaas.com/docs/visao-geral)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Docker Swarm](https://docs.docker.com/engine/swarm/)
- [PostgreSQL JSONB](https://www.postgresql.org/docs/current/datatype-json.html)
- [Vagrant](https://www.vagrantup.com/docs)

---
