package pl.edu.agh.security.rest.provider.client;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.picketlink.identity.federation.api.wstrust.WSTrustClient;
import org.picketlink.identity.federation.api.wstrust.WSTrustClient.SecurityInfo;
import org.picketlink.identity.federation.core.exceptions.ConfigurationException;
import org.picketlink.identity.federation.core.exceptions.ParsingException;
import org.picketlink.identity.federation.core.exceptions.ProcessingException;
import org.picketlink.identity.federation.core.saml.v2.util.DocumentUtil;
import org.picketlink.identity.federation.core.util.Base64;
import org.picketlink.identity.federation.core.wstrust.WSTrustException;
import org.picketlink.identity.federation.core.wstrust.plugins.saml.SAMLUtil;
import org.w3c.dom.Element;

public class ProviderWithSamlAuthenticationTest extends IProviderClientTest {

	private static IProviderClient testClient;

	@BeforeClass
	public static void setUpForAll() {
		try {
			prepareTestClient(USER_NAME, PASSWORD);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void prepareTestClient(String userName, String password)
			throws ConfigurationException, ProcessingException,
			ParsingException {
		// authentication information
		DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
		String assertion = DocumentUtil.getNodeAsString(retrieveSamlAssertion(
				userName, password));
		final String encodedAssertion = Base64
				.encodeBytes(assertion.getBytes()).replaceAll("\n", "");
		defaultHttpClient.getCredentialsProvider().setCredentials(
				new AuthScope(HOST, PORT),
				new UsernamePasswordCredentials(userName, assertion));
		ApacheHttpClient4Executor executor = new ApacheHttpClient4Executor(
				defaultHttpClient) {
			
			@Override
			public ClientResponse execute(ClientRequest request)
					throws Exception {
				request.header("samlAssertion", encodedAssertion);
				return super.execute(request);
			}
		};

		// This initialization only needs to be done once per VM
		RegisterBuiltin.register(ResteasyProviderFactory.getInstance());

		testClient = ProxyFactory.create(IProviderClient.class, REQUEST_PATH,
				executor);
	}

	@Before
	public void setUp() {
		super.client = testClient;
	}

	@Override
	protected void prepareClient(String userName, String password) {
		try {
			prepareTestClient(userName, password);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.client = testClient;
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
