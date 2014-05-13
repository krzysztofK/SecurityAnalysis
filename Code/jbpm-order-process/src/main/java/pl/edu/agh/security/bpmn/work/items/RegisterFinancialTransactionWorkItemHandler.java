package pl.edu.agh.security.bpmn.work.items;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.impl.client.DefaultHttpClient;
import org.drools.process.instance.WorkItemHandler;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.security.financial.dep.service.client.IFinancialService;
import pl.edu.agh.security.financial.dep.service.client.Product;
import pl.edu.agh.security.financial.dep.service.client.TransactionRequest;
import pl.edu.agh.security.financial.dep.service.client.TransactionResponse;

public class RegisterFinancialTransactionWorkItemHandler implements
		WorkItemHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterFinancialTransactionWorkItemHandler.class);
	private static final String INVOICE_REQUESTED_PARAMETER = "InvoiceRequested";
	private static final String FINANCE_REQUEST_PATH = "http://esb.security.agh.edu.pl:8080/rest-binding/financial-service";
	private static final String FINANCE_REQUEST_PATH_OPENSHIFT = "https://orderprocess-tomash.rhcloud.com/rest-binding/financial-service";

	public static final String INVOICE_IDENTIFIER_PARAMTETER = "InvoiceIdentifier";
	public static final String DUE_DATE_PARAMETER = "DueDate";

	@Override
	public void abortWorkItem(WorkItem arg0, WorkItemManager arg1) {
	}

	@Override
	public void executeWorkItem(WorkItem workItem,
			WorkItemManager workItemManager) {

		boolean invoiceRequested = (Boolean) workItem
				.getParameter(INVOICE_REQUESTED_PARAMETER);
		String productName = (String) workItem
				.getParameter(StoreStateRequestWorkItemHandler.PRODUCT_PARAMETER);
		int count = (Integer) workItem
				.getParameter(StoreStateRequestWorkItemHandler.COUNT_PARAMETER);
		String assertion = (String) workItem
				.getParameter(AuthenticateWorkItemHandler.ASSERTION_PARAMETER);
		IFinancialService financialService = prepareFinancialServiceClient(assertion);
		TransactionRequest transactionRequest = new TransactionRequest();
		transactionRequest.setCount(count);
		transactionRequest.setInvoiceRequested(invoiceRequested);
		Product product = new Product();
		product.setName(productName);
		transactionRequest.setProduct(product);
		TransactionResponse transactionResponse = financialService
				.registerTransaction(transactionRequest);
		LOGGER.info("Obtained response from financial service: {}", transactionResponse);
		Map<String, Object> results = new HashMap<String, Object>();
		results.put(DUE_DATE_PARAMETER, transactionResponse.getDueDate());
		results.put(INVOICE_IDENTIFIER_PARAMTETER,
				transactionResponse.getInvoiceIdentifier());
		workItemManager.completeWorkItem(workItem.getId(), results);
	}

	public IFinancialService prepareFinancialServiceClient(
			final String samlAssertionString) {
		DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
		ApacheHttpClient4Executor executor = new ApacheHttpClient4Executor(
				defaultHttpClient) {

			@SuppressWarnings("rawtypes")
			@Override
			public ClientResponse execute(ClientRequest request)
					throws Exception {
			    request.header("samlAssertion", samlAssertionString);
			    LOGGER.info("Financial request uri: "+request.getUri());
                LOGGER.info("Financial request headers: "+request.getHeaders());
                ClientResponse response = super.execute(request);
                LOGGER.info("Financial response: status={}, responseStatus={}", response.getStatus(), response.getResponseStatus());
                return response;
			}
		};

		// This initialization only needs to be done once per VM
		RegisterBuiltin.register(ResteasyProviderFactory.getInstance());

		return ProxyFactory.create(IFinancialService.class,
		        FINANCE_REQUEST_PATH_OPENSHIFT, executor);
	}

}
