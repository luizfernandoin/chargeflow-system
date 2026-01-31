package com.dac.charge_manager.business.charge.detail;

import com.dac.charge_manager.business.charge.ChargeType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChargeDetailSerializer {

    private static final Logger logger = LoggerFactory.getLogger(ChargeDetailSerializer.class);
    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static String serialize(ChargeDetailStrategy strategy) {
        try {
            return mapper.writeValueAsString(strategy);
        } catch (Exception e) {
            logger.error("Erro ao serializar ChargeDetailStrategy", e);
            throw new RuntimeException("Erro ao serializar detalhes da cobrança", e);
        }
    }

    public static ChargeDetailStrategy deserialize(ChargeType type, String jsonPayload) {
        try {
            Class<? extends ChargeDetailStrategy> strategyClass = switch (type) {
                case PIX -> PixChargeDetail.class;
                case BOLETO -> BoletoChargeDetail.class;
                case CARTAO_CREDITO -> CreditCardChargeDetail.class;
            };
            
            return mapper.readValue(jsonPayload, strategyClass);
        } catch (Exception e) {
            logger.error("Erro ao desserializar ChargeDetailStrategy para tipo: {}", type, e);
            throw new RuntimeException("Erro ao desserializar detalhes da cobrança", e);
        }
    }
}
