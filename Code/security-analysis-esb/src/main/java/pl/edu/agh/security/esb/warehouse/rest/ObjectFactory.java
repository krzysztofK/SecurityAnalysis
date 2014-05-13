package pl.edu.agh.security.esb.warehouse.rest;

import javax.inject.Named;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

	public final static QName _WarehouseRequest_QNAME = new QName(
			"http://warehouse.deps.security.agh.edu.pl/",
			"registerTransaction");
	public final static QName _WarehouseResponse_QNAME = new QName(
			"http://warehouse.deps.security.agh.edu.pl/",
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
	 * Create an instance of {@link JAXBElement }{@code <}{@link OrderAck }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://warehouse.deps.security.agh.edu.pl/", name = "registerTransaction")
	public JAXBElement<WarehouseRequest> createTransactionRequest(
			WarehouseRequest value) {
		return new JAXBElement<WarehouseRequest>(_WarehouseRequest_QNAME,
				WarehouseRequest.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Order }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://warehouse.deps.security.agh.edu.pl/", name = "registerTransactionResponse")
	public JAXBElement<WarehouseResponse> createTransactionResponse(
			WarehouseResponse value) {
		return new JAXBElement<WarehouseResponse>(_WarehouseResponse_QNAME,
				WarehouseResponse.class, null, value);
	}
}
