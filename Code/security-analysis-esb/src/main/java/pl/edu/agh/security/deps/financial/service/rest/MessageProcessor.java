package pl.edu.agh.security.deps.financial.service.rest;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.switchyard.Context;
import org.switchyard.Scope;

@Named("messageProcessor")
public class MessageProcessor implements Processor {

	@Inject
	private Context context;

	public MessageProcessor() {
	}

	@Override
	public void process(Exchange arg0) throws Exception {
        //context.setProperty("Assertion", "", Scope.EXCHANGE);
		System.out.println(arg0.getIn().getBody());
	}

}
