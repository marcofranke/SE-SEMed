//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2014.12.01 um 02:19:11 PM CET 
//


package de.biba.omi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Read request type.
 * 
 * <p>Java-Klasse für readRequest complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="readRequest">
 *   &lt;complexContent>
 *     &lt;extension base="{omi.xsd}requestBaseType">
 *       &lt;attribute name="interval">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *             &lt;minInclusive value="-2"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="oldest">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;minInclusive value="1"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="begin" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="end" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="newest">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;minInclusive value="1"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "readRequest")
public class ReadRequest
    extends RequestBaseType
{

    @XmlAttribute(name = "interval")
    protected Double interval;
    @XmlAttribute(name = "oldest")
    protected Integer oldest;
    @XmlAttribute(name = "begin")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar begin;
    @XmlAttribute(name = "end")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar end;
    @XmlAttribute(name = "newest")
    protected Integer newest;

    /**
     * Ruft den Wert der interval-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getInterval() {
        return interval;
    }

    /**
     * Legt den Wert der interval-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setInterval(Double value) {
        this.interval = value;
    }

    /**
     * Ruft den Wert der oldest-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOldest() {
        return oldest;
    }

    /**
     * Legt den Wert der oldest-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOldest(Integer value) {
        this.oldest = value;
    }

    /**
     * Ruft den Wert der begin-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getBegin() {
        return begin;
    }

    /**
     * Legt den Wert der begin-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setBegin(XMLGregorianCalendar value) {
        this.begin = value;
    }

    /**
     * Ruft den Wert der end-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEnd() {
        return end;
    }

    /**
     * Legt den Wert der end-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEnd(XMLGregorianCalendar value) {
        this.end = value;
    }

    /**
     * Ruft den Wert der newest-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNewest() {
        return newest;
    }

    /**
     * Legt den Wert der newest-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNewest(Integer value) {
        this.newest = value;
    }

}
