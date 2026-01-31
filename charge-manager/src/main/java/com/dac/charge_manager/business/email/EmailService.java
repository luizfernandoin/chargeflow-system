package com.dac.charge_manager.business.email;

import com.dac.charge_manager.business.charge.Charge;
import com.dac.charge_manager.business.client.Client;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@ConditionalOnProperty(name = "app.mail.enabled", havingValue = "true", matchIfMissing = true)
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final String from;

    public EmailService(
            JavaMailSender mailSender,
            TemplateEngine templateEngine,
            @Value("${app.mail.from}") String from
    ) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.from = from;
    }

    public void sendWelcomeEmail(Client client) {
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("client", client);
            
            String htmlContent = renderTemplate("welcome", variables);
            sendEmail(client.getEmail(), "Bem-vindo ao Gerenciador de Cobranças", htmlContent);
            logger.info("Email de boas-vindas enviado para: {}", client.getEmail());
        } catch (Exception e) {
            logger.error("Erro ao enviar email de boas-vindas para: {}", client.getEmail(), e);
        }
    }

    public void sendChargeCreatedEmail(Charge charge) {
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("charge", charge);
            
            String htmlContent = renderTemplate("charge-created", variables);
            sendEmail(charge.getClient().getEmail(), "Cobrança Criada com Sucesso", htmlContent);
            logger.info("Email de cobrança criada enviado para: {}", charge.getClient().getEmail());
        } catch (Exception e) {
            logger.error("Erro ao enviar email de cobrança criada para: {}", charge.getClient().getEmail(), e);
        }
    }

    public void sendChargeCreatedEmail(Charge charge, Map<String, Object> chargeDetails) {
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("charge", charge);
            variables.putAll(chargeDetails);
            
            String htmlContent = renderTemplate("charge-created", variables);
            sendEmail(charge.getClient().getEmail(), "Cobrança Criada com Sucesso", htmlContent);
            logger.info("Email de cobrança criada enviado para: {}", charge.getClient().getEmail());
        } catch (Exception e) {
            logger.error("Erro ao enviar email de cobrança criada para: {}", charge.getClient().getEmail(), e);
        }
    }

    public void sendPaymentReceivedEmail(Charge charge) {
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("charge", charge);
            variables.put("now", LocalDateTime.now());
            
            String htmlContent = renderTemplate("payment-received", variables);
            sendEmail(charge.getClient().getEmail(), "Pagamento Recebido com Sucesso", htmlContent);
            logger.info("Email de pagamento recebido enviado para: {}", charge.getClient().getEmail());
        } catch (Exception e) {
            logger.error("Erro ao enviar email de pagamento para: {}", charge.getClient().getEmail(), e);
        }
    }

    public void sendChargeCanceledEmail(Charge charge) {
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("charge", charge);
            
            String htmlContent = renderTemplate("charge-canceled", variables);
            sendEmail(charge.getClient().getEmail(), "Cobrança Cancelada", htmlContent);
            logger.info("Email de cobrança cancelada enviado para: {}", charge.getClient().getEmail());
        } catch (Exception e) {
            logger.error("Erro ao enviar email de cancelamento para: {}", charge.getClient().getEmail(), e);
        }
    }

    private String renderTemplate(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        variables.forEach(context::setVariable);
        return templateEngine.process("email/" + templateName, context);
    }

    private void sendEmail(String to, String subject, String htmlContent) throws MessagingException {
        logger.debug("=== ENVIANDO EMAIL ===");
        logger.debug("Remetente (From): {}", from);
        logger.debug("Destinatário (To): {}", to);
        logger.debug("Assunto (Subject): {}", subject);
        logger.debug("Conteúdo HTML (Content): {}", htmlContent);
        logger.debug("=====================");
        
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
        logger.info("Email enviado com sucesso para: {} | Assunto: {}", to, subject);
    }
}
