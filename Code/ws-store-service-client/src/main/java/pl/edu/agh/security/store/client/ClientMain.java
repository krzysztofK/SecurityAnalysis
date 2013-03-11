package pl.edu.agh.security.store.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;

import org.picketlink.identity.federation.api.wstrust.WSTrustClient;
import org.picketlink.identity.federation.api.wstrust.WSTrustClient.SecurityInfo;
import org.picketlink.identity.federation.core.exceptions.ConfigurationException;
import org.picketlink.identity.federation.core.exceptions.ParsingException;
import org.picketlink.identity.federation.core.exceptions.ProcessingException;
import org.picketlink.identity.federation.core.saml.v2.util.DocumentUtil;
import org.picketlink.identity.federation.core.wstrust.WSTrustException;
import org.picketlink.identity.federation.core.wstrust.plugins.saml.SAMLUtil;
import org.picketlink.trust.jbossws.handler.SAML2Handler;
import org.w3c.dom.Element;

import pl.edu.agh.security.store.service.StoreManager;
import pl.edu.agh.security.store.service.StoreManagerService;
import pl.edu.agh.security.store.service.StoreStateRequest;

public class ClientMain {
	private static final String USER_NAME = "doktor";

	private static final String PASSWORD = "habilitowany";

	public static void main(String[] args) {
		StoreManager storeManager = getStoreManagerWithSAMLAuthentication();
		System.out.println(storeManager.getStoreState(new StoreStateRequest())
				.getCount());
	}

	public static StoreManager getStoreManagerWithSAMLAuthentication() {
		StoreManager storeManager = getStoreManager();

		try {
			BindingProvider bindingProvider = (BindingProvider) storeManager;
			Map<String, List<String>> headers = new HashMap<String, List<String>>();
			headers.put("samlAssertion",
					Collections.singletonList(DocumentUtil
							.getNodeAsString(retrieveSamlAssertion(USER_NAME,
									PASSWORD))));
			bindingProvider.getRequestContext().put(
					MessageContext.HTTP_REQUEST_HEADERS, headers);

			bindingProvider
					.getRequestContext()
					.put(org.picketlink.trust.jbossws.SAML2Constants.SAML2_ASSERTION_PROPERTY,
							retrieveSamlAssertion(USER_NAME, PASSWORD));

			List<Handler> handlers = bindingProvider.getBinding()
					.getHandlerChain();
			handlers.add(new SAML2Handler());
			bindingProvider.getBinding().setHandlerChain(handlers);

		} catch (ParsingException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return storeManager;
	}

	public static StoreManager getStoreManagerWithBasicAuthentication() {
		StoreManager storeManager = getStoreManager();
		BindingProvider bindingProvider = (BindingProvider) storeManager;
		bindingProvider.getRequestContext().put(
				BindingProvider.USERNAME_PROPERTY, USER_NAME);
		bindingProvider.getRequestContext().put(
				BindingProvider.PASSWORD_PROPERTY, PASSWORD);
		return storeManager;
	}

	public static StoreManager getStoreManager() {
		return new StoreManagerService().getStoreManagerPort();
	}

	private static Element retrieveSamlAssertion(String userName,
			String password) throws ParsingException {
		WSTrustClient client = new WSTrustClient("PicketLinkSTS",
				"PicketLinkSTSPort",
				"http://localhost:8080/picketlink-sts/PicketLinkSTS",
				new SecurityInfo(userName, password));

		// // issue a SAML assertion using the client API.
		Element assertion = null;
		try {
			System.out
					.println("\nInvoking token service to get SAML assertion for "
							+ userName);
			assertion = client.issueToken(SAMLUtil.SAML2_TOKEN_TYPE);
			System.out.println("SAML assertion for " + userName
					+ " successfully obtained!");
		} catch (WSTrustException wse) {
			System.out
					.println("Unable to issue assertion: " + wse.getMessage());
			wse.printStackTrace();
			System.exit(1);
		}
		return assertion;

	}

}
