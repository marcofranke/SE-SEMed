//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2014.10.21 um 09:15:53 AM CEST 
//


package de.biba.informationrequestservice.datatypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für TokenExtraction complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="TokenExtraction">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Seperator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RegularExpression" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="index" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TokenExtraction", propOrder = {
    "seperator",
    "regularExpression",
    "index"
})
public class TokenExtraction {

    @XmlElement(name = "Seperator")
    protected String seperator;
    @XmlElement(name = "RegularExpression")
    protected String regularExpression;
    protected int index;

    /**
     * Ruft den Wert der seperator-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSeperator() {
        return seperator;
    }

    /**
     * Legt den Wert der seperator-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSeperator(String value) {
        this.seperator = value;
    }

    /**
     * Ruft den Wert der regularExpression-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegularExpression() {
        return regularExpression;
    }

    /**
     * Legt den Wert der regularExpression-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegularExpression(String value) {
        this.regularExpression = value;
    }

    /**
     * Ruft den Wert der index-Eigenschaft ab.
     * 
     */
    public int getIndex() {
        return index;
    }

    /**
     * Legt den Wert der index-Eigenschaft fest.
     * 
     */
    public void setIndex(int value) {
        this.index = value;
    }

}
