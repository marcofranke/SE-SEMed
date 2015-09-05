//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2014.08.06 um 04:44:10 PM CEST 
//


package de.biba.qlm.qmldf;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.biba.qlm.qmldf package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Object_QNAME = new QName("QLMdf.xsd", "Object");
    private final static QName _Objects_QNAME = new QName("QLMdf.xsd", "Objects");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.biba.qlm.qmldf
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link InfoItemType }
     * 
     */
    public InfoItemType createInfoItemType() {
        return new InfoItemType();
    }

    /**
     * Create an instance of {@link ObjectsType }
     * 
     */
    public ObjectsType createObjectsType() {
        return new ObjectsType();
    }

    /**
     * Create an instance of {@link Description }
     * 
     */
    public Description createDescription() {
        return new Description();
    }

    /**
     * Create an instance of {@link ObjectType }
     * 
     */
    public ObjectType createObjectType() {
        return new ObjectType();
    }

    /**
     * Create an instance of {@link QlmID }
     * 
     */
    public QlmID createQlmID() {
        return new QlmID();
    }

    /**
     * Create an instance of {@link ValueType }
     * 
     */
    public ValueType createValueType() {
        return new ValueType();
    }

    /**
     * Create an instance of {@link InfoItemType.MetaData }
     * 
     */
    public InfoItemType.MetaData createInfoItemTypeMetaData() {
        return new InfoItemType.MetaData();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObjectType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "QLMdf.xsd", name = "Object")
    public JAXBElement<ObjectType> createObject(ObjectType value) {
        return new JAXBElement<ObjectType>(_Object_QNAME, ObjectType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObjectsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "QLMdf.xsd", name = "Objects")
    public JAXBElement<ObjectsType> createObjects(ObjectsType value) {
        return new JAXBElement<ObjectsType>(_Objects_QNAME, ObjectsType.class, null, value);
    }

}