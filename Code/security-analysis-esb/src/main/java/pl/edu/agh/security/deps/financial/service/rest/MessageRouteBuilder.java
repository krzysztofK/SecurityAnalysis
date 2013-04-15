package pl.edu.agh.security.deps.financial.service.rest;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.dataformat.soap.SoapJaxbDataFormat;

public class MessageRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		JaxbDataFormat jaxb = new JaxbDataFormat(
				"pl.edu.agh.security.deps.financial.service.rest");

		SoapJaxbDataFormat soap = new SoapJaxbDataFormat(
				"pl.edu.agh.security.deps.financial.service.rest");
		from("switchyard://RestOrderService").log("Message received: ${body}")
				.marshal(jaxb).log("Message received: ${body}")
				.to("switchyard://OrderService")
				.log("Message received: ${body}").unmarshal(jaxb).log("Message received: ${body}"); //.to("switchyard://RestOrderService");
	}
}
