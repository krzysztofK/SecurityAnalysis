package pl.edu.agh.security.delivery.mappers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.ejb.Stateless;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import pl.edu.agh.security.delivery.entities.Delivery;
import pl.edu.agh.security.delivery.pojos.DeliveryRequest;
import pl.edu.agh.security.delivery.pojos.DeliveryStatus;
import pl.edu.agh.security.delivery.pojos.Priority;

@Stateless
public class DeliveryMapper {
    public Delivery mapToEntity(DeliveryRequest pojo) {
        Delivery entity = new Delivery();
        entity.setCompletionDateTime(xmlGregorianCalendarToDate(pojo.getCompletionDateTime()));
        entity.setPriority(pojo.getPriority().name());
        entity.setRecipientAddress(pojo.getRecipientAddress());
        entity.setRecipientName(pojo.getRecipientName());
        entity.setSenderAddress(pojo.getSenderAddress());
        entity.setSenderName(pojo.getSenderName());
        entity.setStatus(DeliveryStatus.ACCEPTED.name());
        entity.setWeight(pojo.getWeight().intValue());
        return entity;
    }
    
    public DeliveryRequest mapToPojo(Delivery entity) {
        DeliveryRequest pojo = new DeliveryRequest();
        pojo.setCompletionDateTime(dateToXMLGregorianCalendar(entity.getCompletionDateTime()));
        pojo.setPriority(Priority.fromValue(entity.getPriority()));
        pojo.setRecipientAddress(entity.getRecipientAddress());
        pojo.setRecipientName(entity.getRecipientName());
        pojo.setSenderAddress(entity.getSenderAddress());
        pojo.setSenderName(entity.getSenderName());
        pojo.setStatus(DeliveryStatus.fromValue(entity.getStatus()));
        pojo.setWeight(new BigDecimal(entity.getWeight()));
        return pojo;
    }
    
    private XMLGregorianCalendar dateToXMLGregorianCalendar(Date date) {
        if (date == null) {
            return null;
        }

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
    
    private Date xmlGregorianCalendarToDate(XMLGregorianCalendar xmlGregorianCalendar) {
        if (xmlGregorianCalendar == null) {
            return null;
        }
        return xmlGregorianCalendar.toGregorianCalendar().getTime();
    }
}
