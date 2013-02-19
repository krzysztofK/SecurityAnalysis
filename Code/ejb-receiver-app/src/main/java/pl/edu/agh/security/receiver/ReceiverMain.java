package pl.edu.agh.security.receiver;

import java.util.Hashtable;

import javax.ejb.EJBAccessException;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.picketlink.identity.federation.api.wstrust.WSTrustClient;
import org.picketlink.identity.federation.api.wstrust.WSTrustClient.SecurityInfo;
import org.picketlink.identity.federation.core.wstrust.SamlCredential;
import org.picketlink.identity.federation.core.wstrust.WSTrustException;
import org.picketlink.identity.federation.core.wstrust.plugins.saml.SAMLUtil;
import org.w3c.dom.Element;

import pl.edu.agh.security.provider.ejb.interfaces.IProvider;

public class ReceiverMain {

	private static Hashtable<String, Object> env;

	public static void main(String[] args) throws Exception {
		initSAMLEJB3IntegrationTest();
		// testBasicRemoteAuthentication();
		testSAMLEJB3Integration("magister", "inzynier");
	}

	public static void initSAMLEJB3IntegrationTest() {
		env = new Hashtable<String, Object>();

		env.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");

		env.put("jboss.naming.client.ejb.context", true);
	}

	public static void testBasicRemoteAuthentication() throws Exception {
		Context context = new InitialContext(env);

		IProvider provider = (IProvider) context
				.lookup("ejb:/ejb-provider-app-0.0.1-SNAPSHOT//Provider!pl.edu.agh.security.provider.ejb.interfaces.IProvider");

		// invoke method that requires the RegularUser role.
		try {
			System.out.println(provider.provide());
		} catch (EJBAccessException eae) {
			System.out
					.println("User is not authorized to call regular method!");
		}

		// invoke method that allows all roles.
		try {
			System.out.println(provider.provideForAll());
		} catch (EJBAccessException eae) {
			// this should never happen as long as the user has successfully
			// authenticated.
			System.out
					.println("User is not authorized to call unprotected method!");
		}

		// invoke method that requires other role
		try {
			System.out.println(provider.provideForOtherRole());
		} catch (EJBAccessException eae) {
			// this should never happen as long as the user has successfully
			// authenticated.
			System.out
					.println("User is not authorized to call method for other role!");
		}

		// invoke method that dynies all roles
		try {
			System.out.println(provider.provideDenyForAll());
		} catch (EJBAccessException eae) {
			// this should never happen as long as the user has successfully
			// authenticated.
			System.out
					.println("User is not authorized to call forbidden method!");
		}

	}

	public static void testSAMLEJB3Integration(String username, String password)
			throws Exception {
		// create a WSTrustClient instance.
		WSTrustClient client = new WSTrustClient("PicketLinkSTS",
				"PicketLinkSTSPort",
				"http://localhost:8080/picketlink-sts/PicketLinkSTS",
				new SecurityInfo(username, password));

		// // issue a SAML assertion using the client API.
		Element assertion = null;
		try {
			System.out
					.println("\nInvoking token service to get SAML assertion for "
							+ username);
			assertion = client.issueToken(SAMLUtil.SAML2_TOKEN_TYPE);
			System.out.println("SAML assertion for " + username
					+ " successfully obtained!");
		} catch (WSTrustException wse) {
			System.out
					.println("Unable to issue assertion: " + wse.getMessage());
			wse.printStackTrace();
			System.exit(1);
		}

		// invoke the EJB3 bean - the assertion will be propagated with the
		// security context.
		System.out.println(username + " invoking secure EJB3 session bean");

		// new LoginContext("jboss-ejb-client", new SecurityAssociationHandler(
		// new SimplePrincipal(username), new SamlCredential(assertion)))
		// .login();
		// new LoginContext("client-login", new
		// UsernamePasswordHandler(username,
		// new SamlCredential(assertion))).login();
		/****************************************/
//		Properties clientProp = new Properties();
//		clientProp
//				.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED",
//						"false");
//		clientProp.put("remote.connections", "default");
//		clientProp.put("remote.connection.default.port", "4447");
//		clientProp.put("remote.connection.default.host", "localhost");
//		clientProp.put("remote.connection.default.username", username);
//		clientProp.put("remote.connection.default.password",
//				/*DocumentUtil.getNodeAsString(assertion)*/new SamlCredential(assertion));
//		clientProp
//				.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS",
//						"false");
//		clientProp
//
//				.put("remote.connection.default.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS",
//						"JBOSS-LOCAL-USER");
//		clientProp
//				.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT",
//						"false");
//		EJBClientConfiguration cc = new PropertiesBasedEJBClientConfiguration(
//				clientProp);
//		ContextSelector<EJBClientContext> selector = new ConfigBasedEJBClientContextSelector(
//				cc);
//		EJBClientContext.setSelector(selector);
		/****************************************/
		env.put(Context.SECURITY_CREDENTIALS, new SamlCredential(assertion));
		// DocumentUtil.getNodeAsString(assertion)

		AssertionCallbackHandler
				.setSamlCredential(new SamlCredential(assertion));
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

		// invoke method that requires other role
		try {
			System.out.println(provider.provideForOtherRole());
		} catch (EJBAccessException eae) {
			// this should never happen as long as the user has successfully
			// authenticated.
			System.out.println(username
					+ " is not authorized to call method for other role!");
		}

		// invoke method that dynies all roles
		try {
			System.out.println(provider.provideDenyForAll());
		} catch (EJBAccessException eae) {
			// this should never happen as long as the user has successfully
			// authenticated.
			System.out.println(username
					+ " is not authorized to call forbidden method!");
		}

	}
}
