package pl.edu.agh.secutiy.deps.financial.service.rest;

import javax.inject.Named;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@Named("messageProcessor")
public class MessageProcessor implements Processor {

	@Override
	public void process(Exchange arg0) throws Exception {
		System.out.println(arg0.getIn().getBody());
	}

}
