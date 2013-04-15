package pl.edu.agh.security.deps.financial.service.rest;

import javax.inject.Named;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@Named
@XmlRegistry
public class ObjectFactory {

	public final static QName _TransactionRequest_QNAME = new QName(
			"http://service.financial.deps.security.agh.edu.pl/",
			"registerTransaction");
	public final static QName _TransactionResponse_QNAME = new QName(
			"http://service.financial.deps.security.agh.edu.pl/",
			"registerTransactionResponse");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package:
	 * quickstart.switchyard.transform_jaxb._1
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link OrderAck }
	 * 
	 */
	public TransactionRequest createTransactionRequest() {
		return new TransactionRequest();
	}

	/**
	 * Create an instance of {@link Order }
	 * 
	 */
	public TransactionResponse createTransactionResponse() {
		return new TransactionResponse();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link OrderAck }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.financial.deps.security.agh.edu.pl/", name = "registerTransaction")
	public JAXBElement<TransactionRequest> createTransactionRequest(
			TransactionRequest value) {
		return new JAXBElement<TransactionRequest>(_TransactionRequest_QNAME,
				TransactionRequest.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Order }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.financial.deps.security.agh.edu.pl/", name = "registerTransactionResponse")
	public JAXBElement<TransactionResponse> createTransactionResponse(
			TransactionResponse value) {
		return new JAXBElement<TransactionResponse>(_TransactionResponse_QNAME,
				TransactionResponse.class, null, value);
	}
}
