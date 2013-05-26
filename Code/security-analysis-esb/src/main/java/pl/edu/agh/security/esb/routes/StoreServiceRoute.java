package pl.edu.agh.security.esb.routes;

import org.apache.camel.builder.RouteBuilder;

public class StoreServiceRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		from("switchyard://RestStoreServiceProxy").setHeader("samlAssertion")
				.simple("${header.samlassertion}")
				.to("switchyard://RestStoreService");
	}
}
