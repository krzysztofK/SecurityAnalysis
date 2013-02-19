package pl.edu.agh.security.rest.provider.client;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.junit.Before;
import org.junit.BeforeClass;

public class ProviderWithUserPasswordAuthenticationTest extends
		IProviderClientTest {

	private static IProviderClient testClient;

	@BeforeClass
	public static void setUpForAll() {
		prepareTestClient(USER_NAME, PASSWORD);
	}

	public static void prepareTestClient(String userName, String password) {
		// authentication information
		DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
		defaultHttpClient.getCredentialsProvider().setCredentials(
				new AuthScope(HOST, PORT),
				new UsernamePasswordCredentials(userName, password));
		ApacheHttpClient4Executor executor = new ApacheHttpClient4Executor(
				defaultHttpClient);

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
		prepareTestClient(userName, password);
		super.client = testClient;
	}
}
