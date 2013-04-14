package pl.edu.agh.security.deps.financial.service.rest;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.dataformat.soap.SoapJaxbDataFormat;
import org.apache.camel.dataformat.soap.name.ServiceInterfaceStrategy;

public class MessageRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		/*
		 * JaxbDataFormat jaxbDataFormat = new JaxbDataFormat(
		 * "pl.edu.agh.security.deps.financial.service.rest");
		 */

		SoapJaxbDataFormat jaxbDataFormat = new SoapJaxbDataFormat(
				"pl.edu.agh.security.deps.financial.service.rest");

		from("switchyard://RestOrderService").log("Message received: ${body}")
				.marshal(jaxbDataFormat).log("Message received: ${body}")
				.to("switchyard://OrderService"); // ?operationName=registerTransaction
	}
}
