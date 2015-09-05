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
 * <p>Java-Klasse für ObjectPropertyMapping complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ObjectPropertyMapping">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ReferenceXMLTag" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SubjectID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SubClass" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ObjectID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ObClass" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="propertyName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ObjectPropertyMapping", propOrder = {
    "referenceXMLTag",
    "subjectID",
    "subClass",
    "objectID",
    "obClass"
})
public class ObjectPropertyMapping {

    @XmlElement(name = "ReferenceXMLTag", required = true)
    protected String referenceXMLTag;
    @XmlElement(name = "SubjectID")
    protected int subjectID;
    @XmlElement(name = "SubClass", required = true)
    protected String subClass;
    @XmlElement(name = "ObjectID")
    protected int objectID;
    @XmlElement(name = "ObClass", required = true)
    protected String obClass;
    @XmlAttribute(name = "propertyName", required = true)
    protected String propertyName;

    /**
     * Ruft den Wert der referenceXMLTag-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferenceXMLTag() {
        return referenceXMLTag;
    }

    /**
     * Legt den Wert der referenceXMLTag-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferenceXMLTag(String value) {
        this.referenceXMLTag = value;
    }

    /**
     * Ruft den Wert der subjectID-Eigenschaft ab.
     * 
     */
    public int getSubjectID() {
        return subjectID;
    }

    /**
     * Legt den Wert der subjectID-Eigenschaft fest.
     * 
     */
    public void setSubjectID(int value) {
        this.subjectID = value;
    }

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
     * Ruft den Wert der objectID-Eigenschaft ab.
     * 
     */
    public int getObjectID() {
        return objectID;
    }

    /**
     * Legt den Wert der objectID-Eigenschaft fest.
     * 
     */
    public void setObjectID(int value) {
        this.objectID = value;
    }

    /**
     * Ruft den Wert der obClass-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObClass() {
        return obClass;
    }

    /**
     * Legt den Wert der obClass-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObClass(String value) {
        this.obClass = value;
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

}
