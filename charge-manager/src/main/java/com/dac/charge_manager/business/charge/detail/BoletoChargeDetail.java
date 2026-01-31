package com.dac.charge_manager.business.charge.detail;

import com.dac.charge_manager.business.charge.ChargeType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class BoletoChargeDetail implements ChargeDetailStrategy {
    private LocalDate dueDate;
    
    private String bankSlipUrl; // URL do PDF do boleto
    private String identificationField; // Linha digitável
    private String barCode; // Código de barras
    private String nossoNumero; // Nosso número

    public BoletoChargeDetail() {
    }

    public BoletoChargeDetail(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BoletoChargeDetail(String bankSlipUrl, String identificationField, String barCode, String nossoNumero, LocalDate dueDate) {
        this.bankSlipUrl = bankSlipUrl;
        this.identificationField = identificationField;
        this.barCode = barCode;
        this.nossoNumero = nossoNumero;
        this.dueDate = dueDate;
    }

    @Override
    public Map<String, Object> extractEmailVariables() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("bankSlipUrl", bankSlipUrl);
        variables.put("digitableLine", identificationField);
        variables.put("barCode", barCode);
        variables.put("nossoNumero", nossoNumero);
        variables.put("dueDate", dueDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        variables.put("instruction", "Pague o boleto até a data de vencimento");
        return variables;
    }

    @Override
    public ChargeType getType() {
        return ChargeType.BOLETO;
    }

    @Override
    public void enrich(com.dac.chargeproxy.wsdl.CreateChargeResponse response) {
        this.bankSlipUrl = response.getBankSlipUrl();
        this.identificationField = response.getBoletoIdentificationField();
        this.barCode = response.getBoletoBarCode();
        this.nossoNumero = response.getBoletoNossoNumero();
    }

    public String getBankSlipUrl() {
        return bankSlipUrl;
    }

    public void setBankSlipUrl(String bankSlipUrl) {
        this.bankSlipUrl = bankSlipUrl;
    }

    public String getIdentificationField() {
        return identificationField;
    }

    public void setIdentificationField(String identificationField) {
        this.identificationField = identificationField;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getNossoNumero() {
        return nossoNumero;
    }

    public void setNossoNumero(String nossoNumero) {
        this.nossoNumero = nossoNumero;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
