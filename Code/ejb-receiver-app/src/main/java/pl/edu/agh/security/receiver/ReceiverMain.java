package pl.edu.agh.security.receiver;

import java.util.Hashtable;

import javax.ejb.EJBAccessException;
import javax.naming.Context;
import javax.naming.InitialContext;

import pl.edu.agh.security.provider.ejb.interfaces.IProvider;

public class ReceiverMain {

	private static Hashtable<String, Object> env;

	public static void main(String[] args) throws Exception {
		initSAMLEJB3IntegrationTest();
		testSAMLEJB3Integration("magister", "inzynier");
		// testSAMLEJB3Integration("doktor", "inzynier");

	}

	public static void initSAMLEJB3IntegrationTest() {
		env = new Hashtable<String, Object>();

		env.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
	}

	public static void testSAMLEJB3Integration(String username, String password)
			throws Exception {
		// create a WSTrustClient instance.
		// WSTrustClient client = new WSTrustClient("PicketLinkSTS",
		// "PicketLinkSTSPort",
		// "http://localhost:8080/picketlink-sts/PicketLinkSTS",
		// new SecurityInfo(username, password));
		//
		// // issue a SAML assertion using the client API.
		// Element assertion = null;
		// try {
		// System.out
		// .println("\nInvoking token service to get SAML assertion for "
		// + username);
		// assertion = client.issueToken(SAMLUtil.SAML2_TOKEN_TYPE);
		// System.out.println("SAML assertion for " + username
		// + " successfully obtained!");
		// } catch (WSTrustException wse) {
		// System.out
		// .println("Unable to issue assertion: " + wse.getMessage());
		// wse.printStackTrace();
		// System.exit(1);
		// }
		//
		// // use the SecurityClient API to set the assertion in the client
		// // security context.
		// SecurityClient securityClient = SecurityClientFactory
		// .getSecurityClient();
		//
		// securityClient.setSimple(username, new SamlCredential(assertion));
		// securityClient.login();

		// invoke the EJB3 bean - the assertion will be propagated with the
		// security context.
		// System.out.println(username + " invoking secure EJB3 session bean");

		// new LoginContext("client-login", new SecurityAssociationHandler(new
		// SimplePrincipal(username), new SamlCredential(assertion))).login();
		// new LoginContext("client-login", new
		// UsernamePasswordHandler(username, new
		// SamlCredential(assertion))).login();

		Context context = new InitialContext(env);

		IProvider provider = (IProvider) context
				.lookup("ejb:/ejb-provider-app-0.0.1-SNAPSHOT//Provider!pl.edu.agh.security.provider.ejb.interfaces.IProvider");

		// invoke method that requires the RegularUser role.
		try {
			System.out.println(provider.provide());
		} catch (EJBAccessException eae) {
			System.out.println(username
					+ " is not authorized to call regular method!");
		}

		// invoke method that allows all roles.
		try {
			System.out.println(provider.provideForAll());
		} catch (EJBAccessException eae) {
			// this should never happen as long as the user has successfully
			// authenticated.
			System.out.println(username
					+ " is not authorized to call unprotected method!");
		}
	}
}
