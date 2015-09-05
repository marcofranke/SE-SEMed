//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2014.09.04 um 03:38:37 PM CEST 
//


package de.biba.wrapper.simpleXMLWrapper.dataTypes;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.biba.wrapper.simpleXMLWrapper.dataTypes package. 
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.biba.wrapper.simpleXMLWrapper.dataTypes
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Mapping }
     * 
     */
    public Mapping createMapping() {
        return new Mapping();
    }

    /**
     * Create an instance of {@link ClassMapping }
     * 
     */
    public ClassMapping createClassMapping() {
        return new ClassMapping();
    }

    /**
     * Create an instance of {@link DatatypePropertyMapping }
     * 
     */
    public DatatypePropertyMapping createDatatypePropertyMapping() {
        return new DatatypePropertyMapping();
    }

    /**
     * Create an instance of {@link ObjectPropertyMapping }
     * 
     */
    public ObjectPropertyMapping createObjectPropertyMapping() {
        return new ObjectPropertyMapping();
    }

    /**
     * Create an instance of {@link Condition }
     * 
     */
    public Condition createCondition() {
        return new Condition();
    }

    /**
     * Create an instance of {@link Replacements }
     * 
     */
    public Replacements createReplacements() {
        return new Replacements();
    }

    /**
     * Create an instance of {@link TokenExtraction }
     * 
     */
    public TokenExtraction createTokenExtraction() {
        return new TokenExtraction();
    }

    /**
     * Create an instance of {@link Replacement }
     * 
     */
    public Replacement createReplacement() {
        return new Replacement();
    }

}
