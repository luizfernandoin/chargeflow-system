//
// Este arquivo foi gerado pela Eclipse Implementation of JAXB, v3.0.2 
// Consulte https://eclipse-ee4j.github.io/jaxb-ri 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2026.01.30 às 02:08:56 PM BRT 
//


package com.dac.chargeproxy;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de anonymous complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="asaasId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="invoiceUrl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="bankSlipUrl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pixEncodedImage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pixPayload" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pixExpirationDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="boletoIdentificationField" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="boletoBarCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="boletoNossoNumero" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="creditCardToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "asaasId",
    "status",
    "invoiceUrl",
    "bankSlipUrl",
    "pixEncodedImage",
    "pixPayload",
    "pixExpirationDate",
    "boletoIdentificationField",
    "boletoBarCode",
    "boletoNossoNumero",
    "creditCardToken"
})
@XmlRootElement(name = "CreateChargeResponse")
public class CreateChargeResponse {

    @XmlElement(required = true)
    protected String asaasId;
    @XmlElement(required = true)
    protected String status;
    protected String invoiceUrl;
    protected String bankSlipUrl;
    protected String pixEncodedImage;
    protected String pixPayload;
    protected String pixExpirationDate;
    protected String boletoIdentificationField;
    protected String boletoBarCode;
    protected String boletoNossoNumero;
    protected String creditCardToken;

    /**
     * Obtém o valor da propriedade asaasId.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAsaasId() {
        return asaasId;
    }

    /**
     * Define o valor da propriedade asaasId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAsaasId(String value) {
        this.asaasId = value;
    }

    /**
     * Obtém o valor da propriedade status.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Define o valor da propriedade status.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Obtém o valor da propriedade invoiceUrl.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    /**
     * Define o valor da propriedade invoiceUrl.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvoiceUrl(String value) {
        this.invoiceUrl = value;
    }

    /**
     * Obtém o valor da propriedade bankSlipUrl.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankSlipUrl() {
        return bankSlipUrl;
    }

    /**
     * Define o valor da propriedade bankSlipUrl.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankSlipUrl(String value) {
        this.bankSlipUrl = value;
    }

    /**
     * Obtém o valor da propriedade pixEncodedImage.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPixEncodedImage() {
        return pixEncodedImage;
    }

    /**
     * Define o valor da propriedade pixEncodedImage.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPixEncodedImage(String value) {
        this.pixEncodedImage = value;
    }

    /**
     * Obtém o valor da propriedade pixPayload.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPixPayload() {
        return pixPayload;
    }

    /**
     * Define o valor da propriedade pixPayload.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPixPayload(String value) {
        this.pixPayload = value;
    }

    /**
     * Obtém o valor da propriedade pixExpirationDate.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPixExpirationDate() {
        return pixExpirationDate;
    }

    /**
     * Define o valor da propriedade pixExpirationDate.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPixExpirationDate(String value) {
        this.pixExpirationDate = value;
    }

    /**
     * Obtém o valor da propriedade boletoIdentificationField.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBoletoIdentificationField() {
        return boletoIdentificationField;
    }

    /**
     * Define o valor da propriedade boletoIdentificationField.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBoletoIdentificationField(String value) {
        this.boletoIdentificationField = value;
    }

    /**
     * Obtém o valor da propriedade boletoBarCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBoletoBarCode() {
        return boletoBarCode;
    }

    /**
     * Define o valor da propriedade boletoBarCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBoletoBarCode(String value) {
        this.boletoBarCode = value;
    }

    /**
     * Obtém o valor da propriedade boletoNossoNumero.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBoletoNossoNumero() {
        return boletoNossoNumero;
    }

    /**
     * Define o valor da propriedade boletoNossoNumero.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBoletoNossoNumero(String value) {
        this.boletoNossoNumero = value;
    }

    /**
     * Obtém o valor da propriedade creditCardToken.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreditCardToken() {
        return creditCardToken;
    }

    /**
     * Define o valor da propriedade creditCardToken.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreditCardToken(String value) {
        this.creditCardToken = value;
    }

}
