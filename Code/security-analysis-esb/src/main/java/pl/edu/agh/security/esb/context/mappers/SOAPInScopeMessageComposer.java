package pl.edu.agh.security.esb.context.mappers;

import org.switchyard.Exchange;
import org.switchyard.component.soap.composer.SOAPBindingData;
import org.switchyard.component.soap.composer.SOAPMessageComposer;

public class SOAPInScopeMessageComposer extends SOAPMessageComposer {

	@Override
	public SOAPBindingData decompose(Exchange exchange, SOAPBindingData target)
			throws Exception {
		return super.decompose(exchange, target);
	}
}
