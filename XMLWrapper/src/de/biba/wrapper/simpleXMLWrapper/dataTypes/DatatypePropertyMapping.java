//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2014.09.04 um 03:38:37 PM CEST 
//


package de.biba.wrapper.simpleXMLWrapper.dataTypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für DatatypePropertyMapping complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="DatatypePropertyMapping">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SubClass" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="XMLTag" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AttributeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TokenExtraction" type="{}TokenExtraction" minOccurs="0"/>
 *         &lt;element name="Replacements" type="{}Replacements" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="propertyName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Unit" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatatypePropertyMapping", propOrder = {
    "subClass",
    "xmlTag",
    "attributeName",
    "tokenExtraction",
    "replacements"
})
public class DatatypePropertyMapping {

    @XmlElement(name = "SubClass", required = true)
    protected String subClass;
    @XmlElement(name = "XMLTag", required = true)
    protected String xmlTag;
    @XmlElement(name = "AttributeName")
    protected String attributeName;
    @XmlElement(name = "TokenExtraction")
    protected TokenExtraction tokenExtraction;
    @XmlElement(name = "Replacements")
    protected Replacements replacements;
    @XmlAttribute(name = "propertyName", required = true)
    protected String propertyName;
    @XmlAttribute(name = "Unit", required = true)
    protected String unit;

    /**
     * Ruft den Wert der subClass-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubClass() {
        return subClass;
    }

    /**
     * Legt den Wert der subClass-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubClass(String value) {
        this.subClass = value;
    }

    /**
     * Ruft den Wert der xmlTag-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXMLTag() {
        return xmlTag;
    }

    /**
     * Legt den Wert der xmlTag-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXMLTag(String value) {
        this.xmlTag = value;
    }

    /**
     * Ruft den Wert der attributeName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * Legt den Wert der attributeName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttributeName(String value) {
        this.attributeName = value;
    }

    /**
     * Ruft den Wert der tokenExtraction-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link TokenExtraction }
     *     
     */
    public TokenExtraction getTokenExtraction() {
        return tokenExtraction;
    }

    /**
     * Legt den Wert der tokenExtraction-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link TokenExtraction }
     *     
     */
    public void setTokenExtraction(TokenExtraction value) {
        this.tokenExtraction = value;
    }

    /**
     * Ruft den Wert der replacements-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Replacements }
     *     
     */
    public Replacements getReplacements() {
        return replacements;
    }

    /**
     * Legt den Wert der replacements-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Replacements }
     *     
     */
    public void setReplacements(Replacements value) {
        this.replacements = value;
    }

    /**
     * Ruft den Wert der propertyName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Legt den Wert der propertyName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPropertyName(String value) {
        this.propertyName = value;
    }

    /**
     * Ruft den Wert der unit-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Legt den Wert der unit-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnit(String value) {
        this.unit = value;
    }

}
