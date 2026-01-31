package com.dac.charge_manager.infra.repository;

import com.dac.charge_manager.business.charge.detail.ChargeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChargeDetailsRepository extends JpaRepository<ChargeDetails, Long> {

    Optional<ChargeDetails> findByChargeId(Long chargeId);

    void deleteByChargeId(Long chargeId);
}
