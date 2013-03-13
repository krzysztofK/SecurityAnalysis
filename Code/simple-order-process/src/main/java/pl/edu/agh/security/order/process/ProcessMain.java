package pl.edu.agh.security.order.process;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.picketlink.identity.federation.core.exceptions.ConfigurationException;
import org.picketlink.identity.federation.core.exceptions.ParsingException;
import org.picketlink.identity.federation.core.exceptions.ProcessingException;
import org.picketlink.identity.federation.core.saml.v2.util.DocumentUtil;

import pl.edu.agh.security.common.Utils;
import pl.edu.agh.security.store.state.service.client.IStoreState;

public class ProcessMain {

	private static final String REQUEST_PATH = "http://localhost:8080/stores-state-service/state";
	private static final String USER_NAME = "magister";
	private static final String PASSWORD = "inzynier";
	protected static final String HOST = "localhost";
	protected static final int PORT = 8080;

	public static void main(String[] args) throws ConfigurationException, ProcessingException, ParsingException {
		System.out.println(prepareTestClient().getStore("woda", 20));
	}

	public static IStoreState prepareTestClient() throws ConfigurationException, ProcessingException, ParsingException {
		DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
		String assertion = DocumentUtil.getNodeAsString(Utils
				.retrieveSamlAssertion(USER_NAME, PASSWORD));
		final String encodedAssertion = assertion; // Base64.encodeBytes(assertion.getBytes()).replaceAll("\n",
													// "");
		defaultHttpClient.getCredentialsProvider().setCredentials(
				new AuthScope(HOST, PORT),
				new UsernamePasswordCredentials(USER_NAME, assertion));
		ApacheHttpClient4Executor executor = new ApacheHttpClient4Executor(
				defaultHttpClient) {

			@SuppressWarnings("rawtypes")
			@Override
			public ClientResponse execute(ClientRequest request)
					throws Exception {
				request.header("samlAssertion", encodedAssertion);
				return super.execute(request);
			}
		};

		// This initialization only needs to be done once per VM
		RegisterBuiltin.register(ResteasyProviderFactory.getInstance());

		return ProxyFactory.create(IStoreState.class, REQUEST_PATH, executor);
	}
}
