//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.02.10 at 10:54:42 PM CET 
//


package de.biba.wrapper.newSQLWrapper.Mapping;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ValueType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ValueType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="xsd:string"/>
 *     &lt;enumeration value="numeric"/>
 *     &lt;enumeration value="date"/>
 *     &lt;enumeration value="boolean"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ValueType")
@XmlEnum
public enum ValueType {

    @XmlEnumValue("xsd:string")
    XSD_STRING("xsd:string"),
    @XmlEnumValue("numeric")
    NUMERIC("numeric"),
    @XmlEnumValue("date")
    DATE("date"),
    @XmlEnumValue("boolean")
    BOOLEAN("boolean");
    private final String value;

    ValueType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ValueType fromValue(String v) {
        for (ValueType c: ValueType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
