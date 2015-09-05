//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2014.07.07 um 03:35:23 PM CEST 
//


package de.biba.mediator.webservice.configuration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für DataSource complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="DataSource">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pathToConfigFile" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="javaClassForWrapper" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataSource", propOrder = {
    "pathToConfigFile",
    "javaClassForWrapper"
})
public class DataSource {

    @XmlElement(required = true)
    protected String pathToConfigFile;
    @XmlElement(required = true)
    protected String javaClassForWrapper;

    /**
     * Ruft den Wert der pathToConfigFile-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPathToConfigFile() {
        return pathToConfigFile;
    }

    /**
     * Legt den Wert der pathToConfigFile-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPathToConfigFile(String value) {
        this.pathToConfigFile = value;
    }

    /**
     * Ruft den Wert der javaClassForWrapper-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJavaClassForWrapper() {
        return javaClassForWrapper;
    }

    /**
     * Legt den Wert der javaClassForWrapper-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJavaClassForWrapper(String value) {
        this.javaClassForWrapper = value;
    }

}
