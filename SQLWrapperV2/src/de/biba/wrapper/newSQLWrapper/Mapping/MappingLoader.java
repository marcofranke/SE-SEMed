package de.biba.wrapper.newSQLWrapper.Mapping;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

public class MappingLoader {
	public static Mapping loadMapping(String mappingFile) throws JAXBException, SAXException {
		JAXBContext jc = JAXBContext.newInstance(Mapping.class);
	    Unmarshaller unmarshaller = jc.createUnmarshaller();
//	    unmarshaller.setEventHandler(new ValidationEventHandler() {
//			@Override
//			public boolean handleEvent(ValidationEvent arg0) {
//				System.out.println(arg0);
//				return false;
//			}
//		});
	    SchemaFactory sf = SchemaFactory.newInstance( "http://www.w3.org/2001/XMLSchema" );
	    Schema schema = sf.newSchema( MappingLoader.class.getResource("/SQLMappingV2.xsd") );
	    unmarshaller.setSchema( schema );
	    Mapping m = (Mapping) unmarshaller.unmarshal(new File(mappingFile));
	    return m;
	}
}
