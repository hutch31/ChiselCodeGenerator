package org.ghutchis.codegen;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

public class RecordDefFactory {
    public static RecordDef unmarshalResponse(String xmlData) {
        RecordDef device = null;
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(RecordDef.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(xmlData);

            device = (RecordDef) jaxbUnmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            System.out.println("Parsing exception: " + e.toString());
            device = new RecordDef();
        }
        return device;
    }
}
