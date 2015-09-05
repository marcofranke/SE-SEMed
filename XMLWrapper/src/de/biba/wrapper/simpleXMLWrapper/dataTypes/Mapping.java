//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2014.09.04 um 03:38:37 PM CEST 
//


package de.biba.wrapper.simpleXMLWrapper.dataTypes;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ClassMapping" type="{}ClassMapping" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="DatatypePropertyMapping" type="{}DatatypePropertyMapping" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ObjectPropertyMapping" type="{}ObjectPropertyMapping" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="namespace" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "classMapping",
    "datatypePropertyMapping",
    "objectPropertyMapping"
})
@XmlRootElement(name = "Mapping")
public class Mapping {

    @XmlElement(name = "ClassMapping")
    protected List<ClassMapping> classMapping;
    @XmlElement(name = "DatatypePropertyMapping")
    protected List<DatatypePropertyMapping> datatypePropertyMapping;
    @XmlElement(name = "ObjectPropertyMapping")
    protected List<ObjectPropertyMapping> objectPropertyMapping;
    @XmlAttribute(name = "namespace")
    protected String namespace;

    /**
     * Gets the value of the classMapping property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the classMapping property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClassMapping().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ClassMapping }
     * 
     * 
     */
    public List<ClassMapping> getClassMapping() {
        if (classMapping == null) {
            classMapping = new ArrayList<ClassMapping>();
        }
        return this.classMapping;
    }

    /**
     * Gets the value of the datatypePropertyMapping property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the datatypePropertyMapping property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDatatypePropertyMapping().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DatatypePropertyMapping }
     * 
     * 
     */
    public List<DatatypePropertyMapping> getDatatypePropertyMapping() {
        if (datatypePropertyMapping == null) {
            datatypePropertyMapping = new ArrayList<DatatypePropertyMapping>();
        }
        return this.datatypePropertyMapping;
    }

    /**
     * Gets the value of the objectPropertyMapping property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the objectPropertyMapping property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getObjectPropertyMapping().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ObjectPropertyMapping }
     * 
     * 
     */
    public List<ObjectPropertyMapping> getObjectPropertyMapping() {
        if (objectPropertyMapping == null) {
            objectPropertyMapping = new ArrayList<ObjectPropertyMapping>();
        }
        return this.objectPropertyMapping;
    }

    /**
     * Ruft den Wert der namespace-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Legt den Wert der namespace-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNamespace(String value) {
        this.namespace = value;
    }

}
