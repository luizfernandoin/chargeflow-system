package com.dac.charge_manager.api.dto;

import com.dac.charge_manager.business.charge.ChargeType;
import java.time.LocalDate;

public class CreateChargeRequest {

    private Long clientId;
    private Double value;
    private ChargeType type;
    private LocalDate dueDate;
    private String creditCardToken;

    public CreateChargeRequest() {
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public ChargeType getType() {
        return type;
    }

    public void setType(ChargeType type) {
        this.type = type;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getCreditCardToken() {
        return creditCardToken;
    }

    public void setCreditCardToken(String creditCardToken) {
        this.creditCardToken = creditCardToken;
    }
}
