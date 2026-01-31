package com.dac.charge_manager.api;

import com.dac.charge_manager.api.dto.CreateChargeRequest;
import com.dac.charge_manager.business.charge.Charge;
import com.dac.charge_manager.business.charge.ChargeService;
import com.dac.charge_manager.business.charge.ChargeType;
import com.dac.charge_manager.business.client.Client;
import com.dac.charge_manager.infra.repository.ChargeRepository;
import com.dac.charge_manager.infra.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/charges")
public class ChargeController {

    private static final Logger logger = LoggerFactory.getLogger(ChargeController.class);

    private final ChargeService chargeService;
    private final ClientRepository clientRepository;
    private final ChargeRepository chargeRepository;

    public ChargeController(
            ChargeService chargeService,
            ClientRepository clientRepository,
            ChargeRepository chargeRepository
    ) {
        this.chargeService = chargeService;
        this.clientRepository = clientRepository;
        this.chargeRepository = chargeRepository;
    }

    @PostMapping
    public Charge create(@RequestBody CreateChargeRequest request) {
        logger.info("Criando cobrança - ClientId: {}, Type: {}, Value: {}", 
                    request.getClientId(), request.getType(), request.getValue());
        
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        return chargeService.create(
                client,
                request.getValue(),
                request.getType(),
                request.getDueDate(),
                request.getCreditCardToken()
        );
    }

    @PostMapping("/{id}/cancel")
    public Charge cancel(@PathVariable Long id) {
        chargeService.cancel(id);
        return chargeRepository.findById(id).orElseThrow();
    }
}