package pl.edu.agh.security.deps.financial.service.rest;

import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

@Named("messageProcessor")
public class MessageProcessor implements Processor {

	private JAXBContext jaxbContext;
	private Marshaller marshaller;

	public MessageProcessor() {
		try {
			jaxbContext = JAXBContext.newInstance(TransactionRequest.class,
					TransactionResponse.class);
			this.marshaller = jaxbContext.createMarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(Exchange arg0) throws Exception {
		System.out.println(arg0.getIn().getBody());
		DOMSource domSource = new DOMSource();
		DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = documentBuilder.newDocument();
		Node node = marshaller.getNode(arg0.getIn().getBody());
		jaxbContext.createMarshaller().marshal(arg0.getIn().getBody(), document);
		domSource.setNode(document);
		arg0.getIn().setBody(domSource);
	}

}
