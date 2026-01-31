package com.dac.charge_manager.business.charge.detail;

import com.dac.chargeproxy.wsdl.CreateChargeResponse;
import com.dac.charge_manager.business.charge.ChargeType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CreditCardChargeDetail implements ChargeDetailStrategy {
    private LocalDate dueDate;
    
    private String invoiceUrl; // URL da fatura para o cliente pagar
    private String creditCardToken; // Token do cartão de crédito
    private String creditCardNumber; // Últimos 4 dígitos
    private String creditCardBrand; // Bandeira (Visa, Master, etc)

    public CreditCardChargeDetail() {
    }

    public CreditCardChargeDetail(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public CreditCardChargeDetail(String invoiceUrl, LocalDate dueDate) {
        this.invoiceUrl = invoiceUrl;
        this.dueDate = dueDate;
    }

    @Override
    public Map<String, Object> extractEmailVariables() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("invoiceUrl", invoiceUrl);
        variables.put("paymentText", "à vista");
        
        if (creditCardNumber != null && creditCardBrand != null) {
            variables.put("cardInfo", creditCardBrand + " final " + creditCardNumber);
        }
        
        variables.put("instruction", "Clique no link abaixo para pagar com cartão de crédito");
        return variables;
    }

    @Override
    public ChargeType getType() {
        return ChargeType.CARTAO_CREDITO;
    }

    @Override
    public void enrich(com.dac.chargeproxy.wsdl.CreateChargeResponse response) {
        this.invoiceUrl = response.getInvoiceUrl();
        if (response.getCreditCardToken() != null) {
            this.creditCardToken = response.getCreditCardToken();
        }
    }

    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl;
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

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCreditCardBrand() {
        return creditCardBrand;
    }

    public void setCreditCardBrand(String creditCardBrand) {
        this.creditCardBrand = creditCardBrand;
    }
}
