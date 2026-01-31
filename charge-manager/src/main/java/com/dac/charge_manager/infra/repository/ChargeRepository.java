package com.dac.charge_manager.infra.repository;

import com.dac.charge_manager.business.charge.Charge;
import com.dac.charge_manager.business.charge.ChargeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChargeRepository extends JpaRepository<Charge, Long> {
    Optional<Charge> findByAsaasId(String asaasId);
    
    List<Charge> findByStatusAndCreatedAtBefore(ChargeStatus status, LocalDateTime createdAt);
}