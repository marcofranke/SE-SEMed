//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2014.07.07 um 03:35:23 PM CEST 
//


package de.biba.mediator.webservice.configuration;

import javax.xml.bind.annotation.XmlRegistry;
/**[QLMResponseSEMedService. This is a wrapper to tranform a xml based data base schema to an ontology]
Copyright (C) [2014  [Marco Franke (BIBA-Bremer Institut für Produktion und Logistik GmbH)]

This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
    **/

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.biba.mediator.webservice.configuration package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.biba.mediator.webservice.configuration
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link WrapperConfiguration }
     * 
     */
    public WrapperConfiguration createWrapperConfiguration() {
        return new WrapperConfiguration();
    }

    /**
     * Create an instance of {@link DataSource }
     * 
     */
    public DataSource createDataSource() {
        return new DataSource();
    }

}
