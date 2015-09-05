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
 *         &lt;choice>
 *           &lt;element name="read" type="{omi.xsd}readRequest"/>
 *           &lt;element name="write" type="{omi.xsd}writeRequest"/>
 *           &lt;element name="response" type="{omi.xsd}responseListType"/>
 *           &lt;element name="cancel" type="{omi.xsd}cancelRequest"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="version" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="ttl" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *             &lt;minInclusive value="-1"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "read",
    "write",
    "response",
    "cancel"
})
@XmlRootElement(name = "omiEnvelope")
public class OmiEnvelope {

    protected ReadRequest read;
    protected WriteRequest write;
    protected ResponseListType response;
    protected CancelRequest cancel;
    @XmlAttribute(name = "version", required = true)
    protected String version;
    @XmlAttribute(name = "ttl", required = true)
    protected double ttl;

    /**
     * Ruft den Wert der read-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ReadRequest }
     *     
     */
    public ReadRequest getRead() {
        return read;
    }

    /**
     * Legt den Wert der read-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ReadRequest }
     *     
     */
    public void setRead(ReadRequest value) {
        this.read = value;
    }

    /**
     * Ruft den Wert der write-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link WriteRequest }
     *     
     */
    public WriteRequest getWrite() {
        return write;
    }

    /**
     * Legt den Wert der write-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link WriteRequest }
     *     
     */
    public void setWrite(WriteRequest value) {
        this.write = value;
    }

    /**
     * Ruft den Wert der response-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ResponseListType }
     *     
     */
    public ResponseListType getResponse() {
        return response;
    }

    /**
     * Legt den Wert der response-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseListType }
     *     
     */
    public void setResponse(ResponseListType value) {
        this.response = value;
    }

    /**
     * Ruft den Wert der cancel-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CancelRequest }
     *     
     */
    public CancelRequest getCancel() {
        return cancel;
    }

    /**
     * Legt den Wert der cancel-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CancelRequest }
     *     
     */
    public void setCancel(CancelRequest value) {
        this.cancel = value;
    }

    /**
     * Ruft den Wert der version-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Legt den Wert der version-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Ruft den Wert der ttl-Eigenschaft ab.
     * 
     */
    public double getTtl() {
        return ttl;
    }

    /**
     * Legt den Wert der ttl-Eigenschaft fest.
     * 
     */
    public void setTtl(double value) {
        this.ttl = value;
    }

}
