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
import org.picketlink.identity.federation.core.exceptions.ConfigurationException;
import org.picketlink.identity.federation.core.exceptions.ParsingException;
import org.picketlink.identity.federation.core.exceptions.ProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.security.store.state.service.client.IStoreState;
import pl.edu.agh.security.store.state.service.client.Store;
import pl.edu.agh.security.store.state.service.client.StoreStateRequest;

public class StoreStateRequestWorkItemHandler implements WorkItemHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoreStateRequestWorkItemHandler.class);
	public static final String COUNT_PARAMETER = "Count";
	public static final String PRODUCT_PARAMETER = "Product";
	private static final String STORES_REQUEST_PATH = "http://esb.security.agh.edu.pl:8080/rest-store-binding/state";
	private static final String STORES_REQUEST_PATH_OPENSHIFT = "http://orderprocess-tomash.rhcloud.com/rest-store-binding/state";

	public static final String LOCATION_PARAMETER = "Location";
	public static final String SERVICE_URL_PARAMETER = "ServiceURL";

	@Override
	public void abortWorkItem(WorkItem arg0, WorkItemManager arg1) {
	}

	@Override
	public void executeWorkItem(WorkItem workItem,
			WorkItemManager workItemManager) {
		String product = (String) workItem.getParameter(PRODUCT_PARAMETER);
		int count = (Integer) workItem.getParameter(COUNT_PARAMETER);
		String assertion = (String) workItem
				.getParameter(AuthenticateWorkItemHandler.ASSERTION_PARAMETER);
		LOGGER.info("Trying to get store for {} items of {}", count, product);
		LOGGER.info("Saml assertion is " +assertion);
		try {
			IStoreState storeState = prepareStoreStateServiceClient(assertion);
			StoreStateRequest request = new StoreStateRequest();
			request.setCount(count);
			request.setProductName(product);
			Store store = storeState.getStore(request);
			LOGGER.info("Obtained store {}", store);
			Map<String, Object> results = new HashMap<String, Object>();
			results.put(LOCATION_PARAMETER, store.getLocation());
			results.put(SERVICE_URL_PARAMETER, store.getServiceUrl());
			workItemManager.completeWorkItem(workItem.getId(), results);
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

	public IStoreState prepareStoreStateServiceClient(
			final String samlAssertionString) throws ConfigurationException,
			ProcessingException, ParsingException {
		DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
		ApacheHttpClient4Executor executor = new ApacheHttpClient4Executor(
				defaultHttpClient) {

			@SuppressWarnings("rawtypes")
			@Override
			public ClientResponse execute(ClientRequest request)
					throws Exception {
				request.header("samlAssertion", samlAssertionString);
				return super.execute(request);
			}
		};

		// This initialization only needs to be done once per VM
		RegisterBuiltin.register(ResteasyProviderFactory.getInstance());

		return ProxyFactory.create(IStoreState.class, STORES_REQUEST_PATH_OPENSHIFT,
				executor);
	}

}
