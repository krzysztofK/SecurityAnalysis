package pl.edu.agh.security.handlers;

import java.io.StringWriter;

import javax.security.jacc.PolicyContext;
import javax.security.jacc.PolicyContextException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.jboss.as.web.security.HttpServletRequestPolicyContextHandler;
import org.picketlink.identity.federation.bindings.jboss.auth.SAMLTokenFromHttpRequestAbstractLoginModule;
import org.picketlink.identity.federation.core.saml.v2.constants.JBossSAMLURIConstants;
import org.picketlink.trust.jbossws.Util;
import org.picketlink.trust.jbossws.handler.SAML2Handler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SAMLHandlerModified extends SAML2Handler {

	@Override
	protected boolean handleInbound(MessageContext msgContext) {
		HttpServletRequest req = (HttpServletRequest) msgContext
				.get(MessageContext.SERVLET_REQUEST);

		boolean result = super.handleInbound(msgContext);

		String assertionNS = JBossSAMLURIConstants.ASSERTION_NSURI.get();
		SOAPMessageContext ctx = (SOAPMessageContext) msgContext;
		SOAPMessage soapMessage = ctx.getMessage();

		if (soapMessage == null)
			throw logger.nullValueError("SOAP Message");

		Document document = soapMessage.getSOAPPart();
		Element soapHeader = Util.findOrCreateSoapHeader(document
				.getDocumentElement());
		Element assertion = Util.findElement(soapHeader, new QName(assertionNS,
				"Assertion"));

		TransformerFactory transFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = transFactory.newTransformer();
			StringWriter buffer = new StringWriter();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
					"yes");
			transformer.transform(new DOMSource(assertion), new StreamResult(
					buffer));
			final String str = buffer.toString();

			final HttpServletRequestWrapper httpServletRequestWrapper = new HttpServletRequestWrapper(
					req) {
				public String getHeader(String name) {
					if (name.equals("samlAssertion")) {
						return str;
					}
					return super.getHeader(name);
				};
			};
			PolicyContext
					.registerHandler(
							SAMLTokenFromHttpRequestAbstractLoginModule.WEB_REQUEST_KEY,
							new HttpServletRequestPolicyContextHandler() {
								@Override
								public Object getContext(String key, Object data)
										throws PolicyContextException {
									if (super.getContext(key, data) != null) {
										return httpServletRequestWrapper;
									}
									return null;
								}
							}, true);

		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (PolicyContextException e) {
			e.printStackTrace();
		}
		return result;

	}
}
