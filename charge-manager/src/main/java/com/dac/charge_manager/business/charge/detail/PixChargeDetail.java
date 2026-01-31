package com.dac.charge_manager.business.charge.detail;

import com.dac.charge_manager.business.charge.ChargeType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class PixChargeDetail implements ChargeDetailStrategy {
    private String encodedImage; // QR Code em Base64
    private String payload; // Código copia e cola
    private String expirationDate; // Data de expiração
    
    private LocalDate dueDate;

    public PixChargeDetail() {
    }

    public PixChargeDetail(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public PixChargeDetail(String encodedImage, String payload, String expirationDate, LocalDate dueDate) {
        this.encodedImage = encodedImage;
        this.payload = payload;
        this.expirationDate = expirationDate;
        this.dueDate = dueDate;
    }

    @Override
    public Map<String, Object> extractEmailVariables() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("qrCodeImage", encodedImage);
        variables.put("copyPaste", payload);
        
        if (expirationDate != null && !expirationDate.isEmpty()) {
            variables.put("expirationDate", expirationDate);
        } else {
            variables.put("expirationDate", "12 meses após o vencimento");
        }
        
        variables.put("instruction", "Escaneie o QR Code ou copie o código PIX para pagar");
        return variables;
    }

    @Override
    public ChargeType getType() {
        return ChargeType.PIX;
    }

    @Override
    public void enrich(com.dac.chargeproxy.wsdl.CreateChargeResponse response) {
        this.encodedImage = response.getPixEncodedImage();
        this.payload = response.getPixPayload();
        this.expirationDate = response.getPixExpirationDate();
    }

    public String getEncodedImage() {
        return encodedImage;
    }

    public void setEncodedImage(String encodedImage) {
        this.encodedImage = encodedImage;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
