package pl.edu.agh.security.esb.routes;

import org.apache.camel.builder.RouteBuilder;

public class DeliveryServiceRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        //@formatter:off
        from("switchyard://RestDeliveryServiceProxy")
            .setHeader("samlAssertion")
            .simple("${header.samlassertion}")
            .to("switchyard://RestDeliveryService");
      //@formatter:on
    }
}
