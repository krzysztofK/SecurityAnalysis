package pl.edu.agh.security.weather.client;

import java.util.Hashtable;

import javax.ejb.EJBAccessException;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.security.auth.login.LoginContext;

import org.jboss.security.auth.callback.UsernamePasswordHandler;
import org.jboss.security.client.SecurityClient;
import org.jboss.security.client.SecurityClientFactory;
import org.picketlink.identity.federation.api.wstrust.WSTrustClient;
import org.picketlink.identity.federation.api.wstrust.WSTrustClient.SecurityInfo;
import org.picketlink.identity.federation.core.wstrust.SamlCredential;
import org.picketlink.identity.federation.core.wstrust.WSTrustException;
import org.picketlink.identity.federation.core.wstrust.plugins.saml.SAMLUtil;
import org.w3c.dom.Element;

import pl.edu.agh.security.weather.ejb.interfaces.IWeatherService;

public class ClientMain {

	private static Hashtable<String, Object> env;

	public static void main(String[] args) throws Exception {
		initSAMLEJB3IntegrationTest();
		testSAMLEJB3Integration("magister", "inzynier");
		// testSAMLEJB3Integration("doktor", "inzynier");

	}

	public static void initSAMLEJB3IntegrationTest() {
		env = new Hashtable<String, Object>();

		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.jboss.naming.remote.client.InitialContextFactory");

		env.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		env.put(InitialContext.PROVIDER_URL, "remote://localhost:4447");
		env.put(Context.SECURITY_PRINCIPAL, "ejbuser");
		env.put(Context.SECURITY_CREDENTIALS, "password");
		env.put("jboss.naming.client.ejb.context", true);

	}

	public static void testSAMLEJB3Integration(String username, String password)
			throws Exception {
		// create a WSTrustClient instance.
		WSTrustClient client = new WSTrustClient("PicketLinkSTS",
				"PicketLinkSTSPort",
				"http://localhost:8080/picketlink-sts/PicketLinkSTS",
				new SecurityInfo(username, password));

		// issue a SAML assertion using the client API.
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

		// use the SecurityClient API to set the assertion in the client
		// security context.
		SecurityClient securityClient = SecurityClientFactory
				.getSecurityClient();
		
		securityClient.setSimple(username, new SamlCredential(assertion));
		securityClient.login();

		// invoke the EJB3 bean - the assertion will be propagated with the
		// security context.
		System.out.println(username + " invoking secure EJB3 session bean");

//		new LoginContext("client-login", new SecurityAssociationHandler(new SimplePrincipal(username), new SamlCredential(assertion))).login();
//		new LoginContext("client-login", new UsernamePasswordHandler(username, new SamlCredential(assertion))).login();

		Context context = new InitialContext(env);

		Object object = context
				.lookup("ejb:/ejb-weather-service-0.0.1-SNAPSHOT//WeatherService!pl.edu.agh.security.weather.ejb.interfaces.IWeatherService");
		IWeatherService weatherService = (IWeatherService) PortableRemoteObject
				.narrow(object, IWeatherService.class);

		// invoke method that requires the RegularUser role.
		try {
			System.out
					.println(weatherService.invokeWeatherServiceForMagister());
		} catch (EJBAccessException eae) {
			System.out.println(username
					+ " is not authorized to call regular method!");
		}

		// invoke method that allows all roles.
		try {
			System.out.println(weatherService.invokeWeatherServiceForAll());
		} catch (EJBAccessException eae) {
			// this should never happen as long as the user has successfully
			// authenticated.
			System.out.println(username
					+ " is not authorized to call unprotected method!");
		}

		// invoke method that denies access to all roles.
		try {
			System.out
					.println(weatherService.invokeUnavailableWeatherService());
		} catch (EJBAccessException eae) {
			System.out.println(username
					+ " is not authorized to call unavailable method!");
		}
	}
}
