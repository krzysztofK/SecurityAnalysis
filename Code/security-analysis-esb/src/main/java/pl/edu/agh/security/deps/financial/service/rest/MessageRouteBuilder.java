package pl.edu.agh.security.deps.financial.service.rest;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;

public class MessageRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		JaxbDataFormat jaxb = new JaxbDataFormat(
				"pl.edu.agh.security.deps.financial.service.rest");

		from("switchyard://RestOrderService")
				.setProperty("{urn:oasis:names:tc:SAML:2.0:assertion}Assertion")
				.simple("${header.samlassertion}").setHeader("samlAssertion")
				.simple("${header.samlassertion}").marshal(jaxb)
				.log("Message received: ${body}").beanRef("messageProcessor")
				.to("switchyard://OrderService").unmarshal(jaxb);
	}
}
