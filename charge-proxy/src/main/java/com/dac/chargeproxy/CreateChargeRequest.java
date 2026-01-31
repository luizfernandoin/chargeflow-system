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
 *         &lt;element name="chargeId" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dueDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="creditCardToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="clientName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="clientEmail" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="clientCpfCnpj" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
    "chargeId",
    "value",
    "type",
    "dueDate",
    "creditCardToken",
    "clientName",
    "clientEmail",
    "clientCpfCnpj"
})
@XmlRootElement(name = "CreateChargeRequest")
public class CreateChargeRequest {

    protected long chargeId;
    protected double value;
    @XmlElement(required = true)
    protected String type;
    @XmlElement(required = true)
    protected String dueDate;
    protected String creditCardToken;
    @XmlElement(required = true)
    protected String clientName;
    @XmlElement(required = true)
    protected String clientEmail;
    @XmlElement(required = true)
    protected String clientCpfCnpj;

    /**
     * Obtém o valor da propriedade chargeId.
     * 
     */
    public long getChargeId() {
        return chargeId;
    }

    /**
     * Define o valor da propriedade chargeId.
     * 
     */
    public void setChargeId(long value) {
        this.chargeId = value;
    }

    /**
     * Obtém o valor da propriedade value.
     * 
     */
    public double getValue() {
        return value;
    }

    /**
     * Define o valor da propriedade value.
     * 
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Obtém o valor da propriedade type.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Define o valor da propriedade type.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Obtém o valor da propriedade dueDate.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDueDate() {
        return dueDate;
    }

    /**
     * Define o valor da propriedade dueDate.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDueDate(String value) {
        this.dueDate = value;
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

    /**
     * Obtém o valor da propriedade clientName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * Define o valor da propriedade clientName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientName(String value) {
        this.clientName = value;
    }

    /**
     * Obtém o valor da propriedade clientEmail.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientEmail() {
        return clientEmail;
    }

    /**
     * Define o valor da propriedade clientEmail.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientEmail(String value) {
        this.clientEmail = value;
    }

    /**
     * Obtém o valor da propriedade clientCpfCnpj.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientCpfCnpj() {
        return clientCpfCnpj;
    }

    /**
     * Define o valor da propriedade clientCpfCnpj.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientCpfCnpj(String value) {
        this.clientCpfCnpj = value;
    }

}
