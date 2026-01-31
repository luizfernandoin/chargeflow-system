# Configuração de Email no Charge Manager

## 📧 Visão Geral

O sistema de email foi integrado para notificar usuários em eventos importantes:

### Eventos que Disparam Emails

1. **Bem-vindo** - Quando um cliente é registrado
2. **Cobrança Criada** - Quando uma nova cobrança é criada
3. **Pagamento Recebido** - Quando o pagamento é confirmado (via webhook Asaas)
4. **Cobrança Cancelada** - Quando uma cobrança é cancelada
5. **Lembrete de Pagamento** - Para cobranças pendentes (pronto para implementação)

## 🔧 Configuração

### 1. Variáveis de Ambiente

Defina as seguintes variáveis no seu ambiente:

```bash
# SMTP Configuration
MAIL_HOST=smtp.gmail.com          # Host SMTP
MAIL_PORT=587                     # Porta (587 para STARTTLS, 465 para SMTPS)
MAIL_USERNAME=seu-email@gmail.com # Email da conta
MAIL_PASSWORD=sua-senha           # Senha de app (não senha de login)
MAIL_FROM=noreply@chargemanager.com # Email remetente
MAIL_ENABLED=true                 # Ativar/desativar envio
```

### 2. Para Gmail

1. Ative a autenticação de dois fatores em sua conta Google
2. Gere uma [senha de app](https://myaccount.google.com/apppasswords)
3. Use essa senha em `MAIL_PASSWORD`

### 3. Properties Padrão

Em `application.properties`:

```properties
spring.mail.host=${MAIL_HOST:smtp.gmail.com}
spring.mail.port=${MAIL_PORT:587}
spring.mail.username=${MAIL_USERNAME:seu-email@gmail.com}
spring.mail.password=${MAIL_PASSWORD:sua-senha}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true

app.mail.from=${MAIL_FROM:noreply@chargemanager.com}
app.mail.enabled=${MAIL_ENABLED:true}
```

## 📝 Implementação no Código

### EmailService

Localizado em: `src/main/java/com/dac/charge_manager/business/email/EmailService.java`

Métodos disponíveis:

```java
// Enviar email de boas-vindas
emailService.sendWelcomeEmail(Client client);

// Confirmar cobrança criada
emailService.sendChargeCreatedEmail(Charge charge);

// Confirmar pagamento recebido
emailService.sendPaymentReceivedEmail(Charge charge);

// Confirmar cobrança cancelada
emailService.sendChargeCanceledEmail(Charge charge);
```

### Integração nos Serviços

#### ClientController
- Envia email de boas-vindas ao registrar novo cliente

#### ChargeService
- `create()` - Envia email de confirmação de cobrança criada
- `cancel()` - Envia email de confirmação de cancelamento
- `markAsPaid()` - Envia email de pagamento recebido

#### AsaasEventService
- Processa webhook `PAYMENT_RECEIVED` e envia email automático

## 🚀 Próximas Melhorias

### Melhorias Futuras

- [ ] Adicionar templates HTML mais robustos
- [ ] Suporte a anexos
- [ ] Notificações por SMS (Twilio)
- [ ] Dashboard de envios e falhas
- [ ] Retry automático em caso de falha
- [ ] Templates customizáveis por cliente

## ⚠️ Tratamento de Erros

Todos os erros de envio são logados mas não interrompem o fluxo da aplicação. Verifique os logs:

```
ERROR EmailService: Erro ao enviar email para: usuario@example.com
```

Para desabilitar emails em desenvolvimento:

```properties
app.mail.enabled=false
```

## 📧 Exemplos de Email

### Email de Boas-vindas
- Confirmação de cadastro
- Dados do cliente
- Instruções iniciais

### Email de Cobrança Criada
- ID da cobrança
- Valor e tipo
- Status atual
- Link para acompanhamento

### Email de Pagamento Recebido
- Confirmação de pagamento
- Valor pago
- Data e hora
- ID da cobrança

## 🔐 Segurança

- Senhas nunca são hardcoded
- Use variáveis de ambiente ou secrets
- Em produção, use um serviço de email dedicado (SendGrid, AWS SES, etc.)

## 📞 Troubleshooting

**Email não é enviado:**
- Verifique se `app.mail.enabled=true`
- Verifique credenciais SMTP
- Verifique firewall/porta 587 aberta
- Verifique logs da aplicação

**Erro de autenticação:**
- Para Gmail: Use senha de app, não senha de login
- Verifique dois fatores habilitado
- Regenere a senha de app

**Erro de conexão:**
- Verifique host SMTP
- Verifique porta (587 ou 465)
- Teste com telnet: `telnet smtp.gmail.com 587`
