package com.dac.charge_proxy.soap;

import com.dac.charge_proxy.asaas.AsaasRestClient;
import com.dac.chargeproxy.*;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.Map;


@Endpoint
public class ChargeProxyEndpoint {
    private static final String NAMESPACE = "http://dac.com/chargeproxy";

    private final AsaasRestClient asaasClient;
    private final ObjectFactory objectFactory;

    public ChargeProxyEndpoint(AsaasRestClient asaasClient) {
        this.asaasClient = asaasClient;
        this.objectFactory = new ObjectFactory();
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "CreateChargeRequest")
    @ResponsePayload
    public CreateChargeResponse createCharge(@RequestPayload CreateChargeRequest request) {
        System.out.println("Chegou no proxy para criar o charge");
        String clientName = request.getClientName();
        String clientEmail = request.getClientEmail();
        String clientCpfCnpj = request.getClientCpfCnpj();
        double value = request.getValue();
        String type = request.getType();

        Map<String, Object> customer = asaasClient.createCustomer(clientName, clientEmail, clientCpfCnpj);
        String customerId = customer.get("id").toString();

        Map<String, Object> payment = asaasClient.createPayment(
                customerId,
                value,
                type,
                java.time.LocalDate.parse(request.getDueDate()),
                request.getCreditCardToken()
        );

        String asaasId = payment.get("id").toString();

        CreateChargeResponse response = objectFactory.createCreateChargeResponse();
        response.setAsaasId(asaasId);
        response.setStatus("REGISTERED");

        if (payment.get("invoiceUrl") != null) {
            response.setInvoiceUrl(payment.get("invoiceUrl").toString());
        }
        if (payment.get("bankSlipUrl") != null) {
            response.setBankSlipUrl(payment.get("bankSlipUrl").toString());
        }
        if (payment.get("creditCardToken") != null) {
            response.setCreditCardToken(payment.get("creditCardToken").toString());
        }

        if ("PIX".equalsIgnoreCase(type)) {
            Map<String, Object> pixQrCode = asaasClient.getPixQrCode(asaasId);
            if (pixQrCode != null) {
                if (pixQrCode.get("encodedImage") != null) {
                    response.setPixEncodedImage(pixQrCode.get("encodedImage").toString());
                }
                if (pixQrCode.get("payload") != null) {
                    response.setPixPayload(pixQrCode.get("payload").toString());
                }
                if (pixQrCode.get("expirationDate") != null) {
                    response.setPixExpirationDate(pixQrCode.get("expirationDate").toString());
                }
            }
        } else if ("BOLETO".equalsIgnoreCase(type)) {
            Map<String, Object> identification = asaasClient.getBoletoIdentificationField(asaasId);
            if (identification != null) {
                if (identification.get("identificationField") != null) {
                    response.setBoletoIdentificationField(identification.get("identificationField").toString());
                }
                if (identification.get("barCode") != null) {
                    response.setBoletoBarCode(identification.get("barCode").toString());
                }
                if (identification.get("nossoNumero") != null) {
                    response.setBoletoNossoNumero(identification.get("nossoNumero").toString());
                }
            }
        }

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "CancelChargeRequest")
    @ResponsePayload
    public CancelChargeResponse cancelCharge(@RequestPayload CancelChargeRequest request) {
        String asaasId = request.getAsaasId();

        asaasClient.cancelPayment(asaasId);

        CancelChargeResponse response = objectFactory.createCancelChargeResponse();
        response.setStatus("CANCELED");

        return response;
    }
}
