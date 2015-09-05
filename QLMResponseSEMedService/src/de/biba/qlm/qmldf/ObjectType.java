//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2014.08.06 um 04:44:10 PM CEST 
//


package de.biba.qlm.qmldf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;


/**
 * <p>Java-Klasse für ObjectType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ObjectType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{QLMdf.xsd}qlmID" maxOccurs="unbounded"/>
 *         &lt;element ref="{QLMdf.xsd}description" minOccurs="0"/>
 *         &lt;element name="InfoItem" type="{QLMdf.xsd}InfoItemType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{QLMdf.xsd}Object" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="udef" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;anyAttribute/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ObjectType", propOrder = {
    "id",
    "description",
    "infoItem",
    "object"
})
public class ObjectType {

    @XmlElement(required = true)
    protected List<QlmID> id;
    protected Description description;
    @XmlElement(name = "InfoItem")
    protected List<InfoItemType> infoItem;
    @XmlElement(name = "Object")
    protected List<ObjectType> object;
    @XmlAttribute(name = "type")
    @XmlSchemaType(name = "anySimpleType")
    protected String type;
    @XmlAttribute(name = "udef")
    @XmlSchemaType(name = "anySimpleType")
    protected String udef;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the id property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the id property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link QlmID }
     * 
     * 
     */
    public List<QlmID> getId() {
        if (id == null) {
            id = new ArrayList<QlmID>();
        }
        return this.id;
    }

    /**
     * Ruft den Wert der description-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Description }
     *     
     */
    public Description getDescription() {
        return description;
    }

    /**
     * Legt den Wert der description-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Description }
     *     
     */
    public void setDescription(Description value) {
        this.description = value;
    }

    /**
     * Gets the value of the infoItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the infoItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInfoItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InfoItemType }
     * 
     * 
     */
    public List<InfoItemType> getInfoItem() {
        if (infoItem == null) {
            infoItem = new ArrayList<InfoItemType>();
        }
        return this.infoItem;
    }

    /**
     * Gets the value of the object property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the object property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getObject().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ObjectType }
     * 
     * 
     */
    public List<ObjectType> getObject() {
        if (object == null) {
            object = new ArrayList<ObjectType>();
        }
        return this.object;
    }

    /**
     * Ruft den Wert der type-Eigenschaft ab.
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
     * Legt den Wert der type-Eigenschaft fest.
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
     * Ruft den Wert der udef-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUdef() {
        return udef;
    }

    /**
     * Legt den Wert der udef-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUdef(String value) {
        this.udef = value;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * 
     * <p>
     * the map is keyed by the name of the attribute and 
     * the value is the string value of the attribute.
     * 
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     * 
     * 
     * @return
     *     always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

}
