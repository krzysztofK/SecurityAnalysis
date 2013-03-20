package pl.edu.agh.security.common;

import org.picketlink.identity.federation.api.wstrust.WSTrustClient;
import org.picketlink.identity.federation.api.wstrust.WSTrustClient.SecurityInfo;
import org.picketlink.identity.federation.core.exceptions.ParsingException;
import org.picketlink.identity.federation.core.wstrust.WSTrustException;
import org.picketlink.identity.federation.core.wstrust.plugins.saml.SAMLUtil;
import org.w3c.dom.Element;

public class Utils {
	public static Element retrieveSamlAssertion(String serviceName,
			String port, String endpointURI, String userName, String password)
			throws ParsingException {
		WSTrustClient client = new WSTrustClient(serviceName, port,
				endpointURI, new SecurityInfo(userName, password));

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
