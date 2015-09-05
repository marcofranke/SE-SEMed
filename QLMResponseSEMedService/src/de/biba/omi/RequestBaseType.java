//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2014.12.01 um 02:19:11 PM CET 
//


package de.biba.omi;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * Base type for "read" and "write" requests.
 * 
 * <p>Java-Klasse für requestBaseType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="requestBaseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nodeList" type="{omi.xsd}nodesType" minOccurs="0"/>
 *         &lt;element name="requestID" type="{http://www.w3.org/2001/XMLSchema}anyType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{omi.xsd}msg" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="callback" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="msgformat" type="{omi.xsd}schemaID" />
 *       &lt;attribute name="targetType" default="node">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="device"/>
 *             &lt;enumeration value="node"/>
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
@XmlType(name = "requestBaseType", propOrder = {
    "nodeList",
    "requestID",
    "msg"
})
@XmlSeeAlso({
    ReadRequest.class,
    WriteRequest.class
})
public class RequestBaseType {

    protected NodesType nodeList;
    protected List<Object> requestID;
    protected Object msg;
    @XmlAttribute(name = "callback")
    @XmlSchemaType(name = "anyURI")
    protected String callback;
    @XmlAttribute(name = "msgformat")
    protected String msgformat;
    @XmlAttribute(name = "targetType")
    protected String targetType;

    /**
     * Ruft den Wert der nodeList-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link NodesType }
     *     
     */
    public NodesType getNodeList() {
        return nodeList;
    }

    /**
     * Legt den Wert der nodeList-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link NodesType }
     *     
     */
    public void setNodeList(NodesType value) {
        this.nodeList = value;
    }

    /**
     * Gets the value of the requestID property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the requestID property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRequestID().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
    public List<Object> getRequestID() {
        if (requestID == null) {
            requestID = new ArrayList<Object>();
        }
        return this.requestID;
    }

    /**
     * Ruft den Wert der msg-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getMsg() {
        return msg;
    }

    /**
     * Legt den Wert der msg-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setMsg(Object value) {
        this.msg = value;
    }

    /**
     * Ruft den Wert der callback-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCallback() {
        return callback;
    }

    /**
     * Legt den Wert der callback-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCallback(String value) {
        this.callback = value;
    }

    /**
     * Ruft den Wert der msgformat-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgformat() {
        return msgformat;
    }

    /**
     * Legt den Wert der msgformat-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgformat(String value) {
        this.msgformat = value;
    }

    /**
     * Ruft den Wert der targetType-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTargetType() {
        if (targetType == null) {
            return "node";
        } else {
            return targetType;
        }
    }

    /**
     * Legt den Wert der targetType-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetType(String value) {
        this.targetType = value;
    }

}
