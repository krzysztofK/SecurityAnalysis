package pl.edu.agh.security.esb.context.mappers;

import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;

import org.apache.cxf.bus.CXFBusFactory;
import org.apache.cxf.phase.PhaseManager;
import org.switchyard.Context;
import org.switchyard.Property;
import org.switchyard.Scope;
import org.switchyard.common.io.pull.ElementPuller;
import org.switchyard.common.lang.Strings;
import org.switchyard.common.xml.XMLHelper;
import org.switchyard.component.soap.composer.SOAPBindingData;
import org.switchyard.component.soap.composer.SOAPContextMapper;
import org.switchyard.component.soap.composer.SOAPHeadersType;
import org.switchyard.config.Configuration;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SOAPInScopeHeadersMapper extends SOAPContextMapper {

	@Override
	public void mapTo(Context context, SOAPBindingData target) throws Exception {
		Map<String, List<String>> headers = new HashMap<String, List<String>>();

		SOAPMessage soapMessage = target.getSOAPMessage();
		MimeHeaders mimeHeaders = soapMessage.getMimeHeaders();
		SOAPHeader soapHeader = soapMessage.getSOAPHeader();
		for (Property property : context.getProperties(Scope.IN)) {
			Object value = property.getValue();
			if (value != null) {
				String name = property.getName();
				QName qname = XMLHelper.createQName(name);
				boolean qualifiedForSoapHeader = Strings.trimToNull(qname
						.getNamespaceURI()) != null;
				if (qualifiedForSoapHeader && matches(qname)) {
					if (value instanceof Node) {
						Node domNode = soapHeader.getOwnerDocument()
								.importNode((Node) value, true);
						soapHeader.appendChild(domNode);
					} else if (value instanceof Configuration) {
						Element configElement = new ElementPuller()
								.pull(new StringReader(value.toString()));
						Node configNode = soapHeader.getOwnerDocument()
								.importNode(configElement, true);
						soapHeader.appendChild(configNode);
					} else {
						String v = value.toString();
						if (SOAPHeadersType.XML.equals(getSOAPHeadersType())) {
							try {
								Element xmlElement = new ElementPuller()
										.pull(new StringReader(v));
								Node xmlNode = soapHeader.getOwnerDocument()
										.importNode(xmlElement, true);
								soapHeader.appendChild(xmlNode);
							} catch (Throwable t) {
								soapHeader.addChildElement(qname).setValue(v);
							}
						} else {
							soapHeader.addChildElement(qname).setValue(v);
						}
					}
				} else {
					if (matches(name)) {
						mimeHeaders.addHeader(name, String.valueOf(value));
						mimeHeaders.setHeader(name, String.valueOf(value));
						headers.put("samlAssertion", Collections
								.singletonList(String.valueOf(value)));
					}
				}
			}
		}
		soapMessage.saveChanges();
		context.setProperty(MessageContext.HTTP_REQUEST_HEADERS, headers);
		target.getSOAPMessage().setProperty(
				MessageContext.HTTP_REQUEST_HEADERS, headers);
		target.getSOAPMessage().saveChanges();
		System.out.println(target.getWebServiceContext());
		System.out.println(CXFBusFactory.getThreadDefaultBus()
				.getExtension(PhaseManager.class).getOutPhases());
		// EndpointReference endpointReference = target.getWebServiceContext()
		// .getEndpointReference();
	}
}
