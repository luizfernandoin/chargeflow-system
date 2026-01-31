package com.dac.charge_manager.api;

import com.dac.charge_manager.business.client.Client;
import com.dac.charge_manager.business.email.EmailService;
import com.dac.charge_manager.infra.repository.ClientRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientRepository repository;
    private final EmailService emailService;

    public ClientController(ClientRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    @PostMapping
    public Client create(@RequestBody Client client) {
        
        Client savedClient = repository.save(client);
        
        emailService.sendWelcomeEmail(savedClient);
        
        return savedClient;
    }
}