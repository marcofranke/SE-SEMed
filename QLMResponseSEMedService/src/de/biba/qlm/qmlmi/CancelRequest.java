//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2014.08.06 um 04:45:15 PM CEST 
//


package de.biba.qlm.qmlmi;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Cancel request type.
 * 
 * <p>Java-Klasse für cancelRequest complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="cancelRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nodeList" type="{QLMmi.xsd}nodesType" minOccurs="0"/>
 *         &lt;element name="requestId" type="{QLMmi.xsd}idType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cancelRequest", propOrder = {
    "nodeList",
    "requestId"
})
public class CancelRequest {

    protected NodesType nodeList;
    @XmlElement(required = true)
    protected List<IdType> requestId;

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
     * Gets the value of the requestId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the requestId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRequestId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IdType }
     * 
     * 
     */
    public List<IdType> getRequestId() {
        if (requestId == null) {
            requestId = new ArrayList<IdType>();
        }
        return this.requestId;
    }

}
