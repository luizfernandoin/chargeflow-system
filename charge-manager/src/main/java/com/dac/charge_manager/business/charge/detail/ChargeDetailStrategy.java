package com.dac.charge_manager.business.charge.detail;

import com.dac.charge_manager.business.charge.ChargeType;
import com.dac.chargeproxy.wsdl.CreateChargeResponse;
import java.util.Map;

public interface ChargeDetailStrategy {

    Map<String, Object> extractEmailVariables();

    ChargeType getType();

    void enrich(CreateChargeResponse response);
}
