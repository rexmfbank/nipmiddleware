package com.globalaccelerex.nipmiddleware.util;

import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class XmlUtil {

    private final ConcurrentHashMap<Class, JAXBContext> jaxbContext = new ConcurrentHashMap();

    private JAXBContext getJAXBContext(Class z) {
        if (jaxbContext.contains(z)) {
            return jaxbContext.get(z);
        }
        try {
            JAXBContext context = JAXBContext.newInstance(z);
            jaxbContext.put(z, context);
            return context;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

    }

    public <T> T unmarshal(String xmlString, Class<T> type) {
        try {
            // Unmarshallers are not thread-safe.  Create a new one every time.
            final Unmarshaller unmarshaller = getJAXBContext(type).createUnmarshaller();
            XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(xmlString));
            final JAXBElement<T> u = unmarshaller.unmarshal(reader, type);
            return u.getValue();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public String marshal(Class z, Object obj) {
        try {
            // Marshallers are not thread-safe.  Create a new one every time.
            final Marshaller marshaller = getJAXBContext(z).createMarshaller();
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(obj, stringWriter);
            return stringWriter.toString();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
