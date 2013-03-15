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
import pl.edu.agh.security.deps.financial.service.FinancialOperations;
import pl.edu.agh.security.deps.financial.service.FinancialOperationsService;
import pl.edu.agh.security.deps.financial.service.Product;
import pl.edu.agh.security.deps.financial.service.TransactionRequest;
import pl.edu.agh.security.deps.financial.service.TransactionResponse;
import pl.edu.agh.security.store.state.service.client.IStoreState;
import pl.edu.agh.security.store.state.service.client.Store;

public class ProcessMain {

	private static final String REQUEST_PATH = "http://localhost:8080/stores-state-service/state";
	private static final String USER_NAME = "magister";
	private static final String PASSWORD = "inzynier";
	protected static final String HOST = "localhost";
	protected static final int PORT = 8080;

	private static final String ORDERED_PRODUCT = "woda";
	private static final int COUNT = 20;
	private static final boolean INVOICE_REQUESTED = true;

	public static void main(String[] args) throws ConfigurationException,
			ProcessingException, ParsingException {
		Store store = prepareStoreStateServiceClient().getStore(
				ORDERED_PRODUCT, COUNT);
		if (store != null) {
			// TODO: store service + shipments

			/**
			 * Financial service execution
			 */
			FinancialOperations financialOperations = new FinancialOperationsService()
					.getFinancialOperationsPort();
			TransactionRequest transactionRequest = new TransactionRequest();
			transactionRequest.setCount(COUNT);
			Product product = new Product();
			product.setName(ORDERED_PRODUCT);
			transactionRequest.setProduct(product);
			transactionRequest.setInvoiceRequested(INVOICE_REQUESTED);

			TransactionResponse transactionResponse = financialOperations
					.registerTransaction(transactionRequest);
			if (transactionResponse != null) {
				System.out.println("Transaction successful. Due date: "
						+ transactionResponse.getDueDate()
						+ ", invoice number: "
						+ transactionResponse.getInvoiceIdentifier());
			}
		} else {
			System.out.println("Requested count of the products not available");
		}
	}

	public static IStoreState prepareStoreStateServiceClient()
			throws ConfigurationException, ProcessingException,
			ParsingException {
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
