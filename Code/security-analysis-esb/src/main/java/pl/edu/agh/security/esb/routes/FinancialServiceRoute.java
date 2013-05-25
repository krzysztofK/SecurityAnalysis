package pl.edu.agh.security.esb.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.dataformat.soap.SoapJaxbDataFormat;

public class FinancialServiceRoute extends RouteBuilder {

	private static final String SOAP_ASSERTION_HEADER = "{urn:oasis:names:tc:SAML:2.0:assertion}Assertion";

	@Override
	public void configure() throws Exception {

		JaxbDataFormat jaxb = new JaxbDataFormat(
				"pl.edu.agh.security.deps.financial.service.rest");
		SoapJaxbDataFormat soapJaxbDataFormat = new SoapJaxbDataFormat(
				"pl.edu.agh.security.deps.financial.service.rest");
		from("switchyard://RestOrderService").setHeader(SOAP_ASSERTION_HEADER)
				.simple("${header.samlassertion}").setHeader("samlAssertion")
				.simple("${header.samlassertion}")
				.setProperty(SOAP_ASSERTION_HEADER)
				.simple("${header.samlassertion}").setProperty("samlAssertion")
				.simple("${header.samlassertion}").marshal(jaxb)
				.log("Poszlo: ${body}").to("switchyard://OrderService")
				.unmarshal(jaxb);
	}
}
