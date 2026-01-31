package com.dac.charge_manager.business.charge;

import com.dac.charge_manager.business.charge.detail.*;
import com.dac.charge_manager.business.client.Client;
import com.dac.charge_manager.business.email.EmailService;
import com.dac.charge_manager.infra.repository.ChargeRepository;
import com.dac.charge_manager.infra.repository.ChargeDetailsRepository;
import com.dac.charge_manager.infra.soap.ChargeProxyClient;
import com.dac.chargeproxy.wsdl.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ChargeService {

    private static final Logger logger = LoggerFactory.getLogger(ChargeService.class);

    private final ChargeRepository repository;
    private final ChargeDetailsRepository detailsRepository;
    private final ChargeProxyClient proxyClient;
    private final EmailService emailService;

    public ChargeService(
            ChargeRepository repository,
            ChargeDetailsRepository detailsRepository,
            ChargeProxyClient proxyClient,
            EmailService emailService
    ) {
        this.repository = repository;
        this.detailsRepository = detailsRepository;
        this.proxyClient = proxyClient;
        this.emailService = emailService;
    }

    @Transactional
    public Charge create(
            Client client,
            Double value,
            ChargeType type,
            java.time.LocalDate dueDate,
            String creditCardToken) {
        
        ChargeDetailStrategy detailStrategy = switch (type) {
            case PIX -> new PixChargeDetail(dueDate);
            case BOLETO -> new BoletoChargeDetail(dueDate);
            case CARTAO_CREDITO -> {
                CreditCardChargeDetail card = new CreditCardChargeDetail(dueDate);
                if (creditCardToken != null) {
                    card.setCreditCardToken(creditCardToken);
                }
                yield card;
            }
        };
        
        if (dueDate == null) {
            throw new IllegalArgumentException("Data de vencimento é obrigatória");
        }
        if (dueDate.isBefore(java.time.LocalDate.now())) {
            throw new IllegalArgumentException("Data de vencimento não pode ser anterior a hoje");
        }
        
        Charge charge = new Charge();
        charge.setClient(client);
        charge.setValue(value);
        charge.setType(type);
        charge.setStatus(ChargeStatus.PENDING);
        charge = repository.save(charge);
        
        logger.info("Cobrança criada com ID: {} para cliente: {}", charge.getId(), client.getName());

        String dueDateString = dueDate != null ? dueDate.toString() : null;
        CreateChargeResponse response = proxyClient.createCharge(
                charge.getId(),
                value,
                type.name(),
                dueDateString,
                detailStrategy instanceof CreditCardChargeDetail card ? card.getCreditCardToken() : null,
                client.getName(),
                client.getEmail(),
                client.getCpfCnpj()
        );

        String asaasId = response.getAsaasId();
        charge.setAsaasId(asaasId);
        charge.setStatus(ChargeStatus.REGISTERED);

        detailStrategy.enrich(response);

        String serializedPayload = ChargeDetailSerializer.serialize(detailStrategy);
        ChargeDetails details = new ChargeDetails(charge.getId(), type, serializedPayload);
        details = detailsRepository.save(details);
        
        charge.setDetails(details);
        Charge savedCharge = repository.save(charge);
        
        logger.info("Detalhes da cobrança salvos com ID: {} para cobrança ID: {}", 
                    details.getId(), charge.getId());

        Map<String, Object> emailVariables = detailStrategy.extractEmailVariables();
        emailVariables.put("chargeType", type.toString());
        emailVariables.put("dueDate", dueDateString);
        
        emailService.sendChargeCreatedEmail(savedCharge, emailVariables);
        
        return savedCharge;
    }

    @Transactional
    public void cancel(Long chargeId) {
        Charge charge = repository.findById(chargeId).orElseThrow();
        CancelChargeResponse response = proxyClient.cancelCharge(charge.getAsaasId());
        charge.setStatus(ChargeStatus.CANCELED);
        Charge canceledCharge = repository.save(charge);
        
        emailService.sendChargeCanceledEmail(canceledCharge);
    }

    @Transactional
    public void markAsPaid(Long chargeId) {
        Charge charge = repository.findById(chargeId).orElseThrow();
        charge.setStatus(ChargeStatus.PAID);
        Charge paidCharge = repository.save(charge);
        
        emailService.sendPaymentReceivedEmail(paidCharge);
    }

    public ChargeDetailStrategy getDetail(Long chargeId) {
        ChargeDetails details = detailsRepository.findByChargeId(chargeId)
                .orElseThrow(() -> new RuntimeException("Detalhes da cobrança não encontrados"));
        
        return ChargeDetailSerializer.deserialize(details.getType(), details.getPayload());
    }
}