//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2014.11.21 um 08:58:37 AM CET 
//


package de.biba.informationrequestservice.datatypes;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für Service complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Service">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="query" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="namespace" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="requestInterval" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="URLWebService" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DataSource" type="{}DataSource" maxOccurs="unbounded"/>
 *         &lt;element name="OutputWrapper" type="{}OutputWrapper" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Service", propOrder = {
    "id",
    "query",
    "namespace",
    "requestInterval",
    "urlWebService",
    "dataSource",
    "outputWrapper"
})
public class Service {

    protected int id;
    @XmlElement(required = true)
    protected String query;
    @XmlElement(required = true)
    protected String namespace;
    protected int requestInterval;
    @XmlElement(name = "URLWebService")
    protected String urlWebService;
    @XmlElement(name = "DataSource", required = true)
    protected List<DataSource> dataSource;
    @XmlElement(name = "OutputWrapper", required = true)
    protected List<OutputWrapper> outputWrapper;

    /**
     * Ruft den Wert der id-Eigenschaft ab.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Legt den Wert der id-Eigenschaft fest.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * Ruft den Wert der query-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQuery() {
        return query;
    }

    /**
     * Legt den Wert der query-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQuery(String value) {
        this.query = value;
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

    /**
     * Ruft den Wert der requestInterval-Eigenschaft ab.
     * 
     */
    public int getRequestInterval() {
        return requestInterval;
    }

    /**
     * Legt den Wert der requestInterval-Eigenschaft fest.
     * 
     */
    public void setRequestInterval(int value) {
        this.requestInterval = value;
    }

    /**
     * Ruft den Wert der urlWebService-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getURLWebService() {
        return urlWebService;
    }

    /**
     * Legt den Wert der urlWebService-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setURLWebService(String value) {
        this.urlWebService = value;
    }

    /**
     * Gets the value of the dataSource property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataSource property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataSource().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataSource }
     * 
     * 
     */
    public List<DataSource> getDataSource() {
        if (dataSource == null) {
            dataSource = new ArrayList<DataSource>();
        }
        return this.dataSource;
    }

    /**
     * Gets the value of the outputWrapper property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the outputWrapper property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOutputWrapper().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OutputWrapper }
     * 
     * 
     */
    public List<OutputWrapper> getOutputWrapper() {
        if (outputWrapper == null) {
            outputWrapper = new ArrayList<OutputWrapper>();
        }
        return this.outputWrapper;
    }

}
