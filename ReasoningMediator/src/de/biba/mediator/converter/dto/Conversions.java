//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.23 at 12:09:57 PM CET 
//


package de.biba.mediator.converter.dto;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Rule" type="{}SimpleRule" maxOccurs="unbounded"/>
 *         &lt;element name="ComplexRule" type="{}ComplexRule" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "rule",
    "complexRule"
})
@XmlRootElement(name = "Conversions")
public class Conversions {

    @XmlElement(name = "Rule", required = true)
    protected List<SimpleRule> rule;
    @XmlElement(name = "ComplexRule", required = true)
    protected List<ComplexRule> complexRule;

    /**
     * Gets the value of the rule property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rule property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRule().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SimpleRule }
     * 
     * 
     */
    public List<SimpleRule> getRule() {
        if (rule == null) {
            rule = new ArrayList<SimpleRule>();
        }
        return this.rule;
    }

    /**
     * Gets the value of the complexRule property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the complexRule property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComplexRule().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ComplexRule }
     * 
     * 
     */
    public List<ComplexRule> getComplexRule() {
        if (complexRule == null) {
            complexRule = new ArrayList<ComplexRule>();
        }
        return this.complexRule;
    }

}
