package pl.edu.agh.security.delivery.marshallers;

import java.io.StringWriter;
import java.math.BigDecimal;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import pl.edu.agh.security.delivery.pojos.DeliveryRequest;
import pl.edu.agh.security.delivery.pojos.ObjectFactory;
import pl.edu.agh.security.delivery.pojos.Priority;

public class TestDataMarshaller {
    
    public static String createSamplePayload() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance("pl.edu.agh.security.delivery.pojos");
        Marshaller marshaller = jaxbContext.createMarshaller();
        ObjectFactory objectFactory = new ObjectFactory();
        DeliveryRequest deliveryRequest = objectFactory.createDeliveryRequest();
        XMLGregorianCalendar xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar();
        xgc.setYear(2013);
        xgc.setMonth(DatatypeConstants.JUNE);
        xgc.setDay(2);
        xgc.setTime(15, 10, 1);
        deliveryRequest.setCompletionDateTime(xgc);
        deliveryRequest.setPriority(Priority.TOP);
        deliveryRequest.setRecipientAddress("Springfield 123");
        deliveryRequest.setRecipientName("Homer Simpson");
        deliveryRequest.setSenderAddress("Southpark 456");
        deliveryRequest.setSenderName("Eric Cartman");
        deliveryRequest.setWeight(BigDecimal.TEN);
        StringWriter sw = new StringWriter();
        marshaller.marshal(objectFactory.createDeliveryRequest(deliveryRequest), sw);
        return sw.toString();
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println(createSamplePayload());
    }
}
