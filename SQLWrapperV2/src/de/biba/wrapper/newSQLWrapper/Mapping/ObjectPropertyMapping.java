//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.02.10 at 10:54:42 PM CET 
//


package de.biba.wrapper.newSQLWrapper.Mapping;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ObjectPropertyMapping complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ObjectPropertyMapping">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SubColumn" type="{}ColumnMapping" maxOccurs="unbounded"/>
 *         &lt;element name="ObColumn" type="{}ColumnMapping" maxOccurs="unbounded"/>
 *         &lt;choice>
 *           &lt;element name="Table" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="Join" type="{}MultiJoinedTable"/>
 *         &lt;/choice>
 *         &lt;element name="Conditions" type="{}Condition" maxOccurs="unbounded" minOccurs="0"/>
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
    "subColumn",
    "obColumn",
    "table",
    "join",
    "conditions"
})
public class ObjectPropertyMapping {

    @XmlElement(name = "SubColumn", required = true)
    protected List<ColumnMapping> subColumn;
    @XmlElement(name = "ObColumn", required = true)
    protected List<ColumnMapping> obColumn;
    @XmlElement(name = "Table")
    protected String table;
    @XmlElement(name = "Join")
    protected MultiJoinedTable join;
    @XmlElement(name = "Conditions")
    protected List<Condition> conditions;
    @XmlAttribute(name = "propertyName", required = true)
    protected String propertyName;

    /**
     * Gets the value of the subColumn property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subColumn property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubColumn().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ColumnMapping }
     * 
     * 
     */
    public List<ColumnMapping> getSubColumn() {
        if (subColumn == null) {
            subColumn = new ArrayList<ColumnMapping>();
        }
        return this.subColumn;
    }

    /**
     * Gets the value of the obColumn property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the obColumn property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getObColumn().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ColumnMapping }
     * 
     * 
     */
    public List<ColumnMapping> getObColumn() {
        if (obColumn == null) {
            obColumn = new ArrayList<ColumnMapping>();
        }
        return this.obColumn;
    }

    /**
     * Gets the value of the table property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTable() {
        return table;
    }

    /**
     * Sets the value of the table property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTable(String value) {
        this.table = value;
    }

    /**
     * Gets the value of the join property.
     * 
     * @return
     *     possible object is
     *     {@link MultiJoinedTable }
     *     
     */
    public MultiJoinedTable getJoin() {
        return join;
    }

    /**
     * Sets the value of the join property.
     * 
     * @param value
     *     allowed object is
     *     {@link MultiJoinedTable }
     *     
     */
    public void setJoin(MultiJoinedTable value) {
        this.join = value;
    }

    /**
     * Gets the value of the conditions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the conditions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConditions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Condition }
     * 
     * 
     */
    public List<Condition> getConditions() {
        if (conditions == null) {
            conditions = new ArrayList<Condition>();
        }
        return this.conditions;
    }

    /**
     * Gets the value of the propertyName property.
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
     * Sets the value of the propertyName property.
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