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
 * <p>Java-Klasse für ClassMapping complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ClassMapping">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="XMLTag" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Condition" type="{}Condition" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="className" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClassMapping", propOrder = {
    "id",
    "xmlTag",
    "condition"
})
public class ClassMapping {

    @XmlElement(name = "ID")
    protected int id;
    @XmlElement(name = "XMLTag")
    protected String xmlTag;
    @XmlElement(name = "Condition")
    protected Condition condition;
    @XmlAttribute(name = "className", required = true)
    protected String className;

    /**
     * Ruft den Wert der id-Eigenschaft ab.
     * 
     */
    public int getID() {
        return id;
    }

    /**
     * Legt den Wert der id-Eigenschaft fest.
     * 
     */
    public void setID(int value) {
        this.id = value;
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
     * Ruft den Wert der condition-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Condition }
     *     
     */
    public Condition getCondition() {
        return condition;
    }

    /**
     * Legt den Wert der condition-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Condition }
     *     
     */
    public void setCondition(Condition value) {
        this.condition = value;
    }

    /**
     * Ruft den Wert der className-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassName() {
        return className;
    }

    /**
     * Legt den Wert der className-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassName(String value) {
        this.className = value;
    }

}
