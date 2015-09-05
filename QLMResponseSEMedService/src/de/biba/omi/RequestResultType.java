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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import de.biba.odf.ObjectsType;


/**
 * Result of a request.
 * 
 * <p>Java-Klasse für requestResultType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="requestResultType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{omi.xsd}returnType"/>
 *         &lt;element name="requestId" type="{omi.xsd}idType" minOccurs="0"/>
 *         &lt;element ref="{omi.xsd}msg" minOccurs="0"/>
 *         &lt;element name="nodeList" type="{omi.xsd}nodesType" minOccurs="0"/>
 *         &lt;element ref="{omi.xsd}omiEnvelope" minOccurs="0"/>
 *       &lt;/sequence>
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
@XmlType(name = "requestResultType", propOrder = {
    "_return",
    "requestId",
    "msg",
    "nodeList",
    "omiEnvelope"
})

@XmlSeeAlso({ObjectsType.class})
public class RequestResultType {

    @XmlElement(name = "return", required = true)
    protected ReturnType _return;
    protected IdType requestId;
    protected Object msg;
    protected NodesType nodeList;
    protected OmiEnvelope omiEnvelope;
    @XmlAttribute(name = "msgformat")
    protected String msgformat;
    @XmlAttribute(name = "targetType")
    protected String targetType;

    /**
     * Ruft den Wert der return-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ReturnType }
     *     
     */
    public ReturnType getReturn() {
        return _return;
    }

    /**
     * Legt den Wert der return-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ReturnType }
     *     
     */
    public void setReturn(ReturnType value) {
        this._return = value;
    }

    /**
     * Ruft den Wert der requestId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link IdType }
     *     
     */
    public IdType getRequestId() {
        return requestId;
    }

    /**
     * Legt den Wert der requestId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link IdType }
     *     
     */
    public void setRequestId(IdType value) {
        this.requestId = value;
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
     * Present if the sender wants to submit a new request to the receiver. This is useful for engaging direct dialogs" while the connection is alive. It is also especially useful for communicating with firewall/NAT-protected nodes.
     * 
     * @return
     *     possible object is
     *     {@link OmiEnvelope }
     *     
     */
    public OmiEnvelope getOmiEnvelope() {
        return omiEnvelope;
    }

    /**
     * Legt den Wert der omiEnvelope-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link OmiEnvelope }
     *     
     */
    public void setOmiEnvelope(OmiEnvelope value) {
        this.omiEnvelope = value;
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
