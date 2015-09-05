//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2014.11.21 um 08:58:37 AM CET 
//


package de.biba.informationrequestservice.datatypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für OutputWrapper complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="OutputWrapper">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="javaClassForStorage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="configurationForStorage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OutputWrapper", propOrder = {
    "javaClassForStorage",
    "configurationForStorage"
})
public class OutputWrapper {

    @XmlElement(required = true)
    protected String javaClassForStorage;
    protected String configurationForStorage;

    /**
     * Ruft den Wert der javaClassForStorage-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJavaClassForStorage() {
        return javaClassForStorage;
    }

    /**
     * Legt den Wert der javaClassForStorage-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJavaClassForStorage(String value) {
        this.javaClassForStorage = value;
    }

    /**
     * Ruft den Wert der configurationForStorage-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConfigurationForStorage() {
        return configurationForStorage;
    }

    /**
     * Legt den Wert der configurationForStorage-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConfigurationForStorage(String value) {
        this.configurationForStorage = value;
    }

}
