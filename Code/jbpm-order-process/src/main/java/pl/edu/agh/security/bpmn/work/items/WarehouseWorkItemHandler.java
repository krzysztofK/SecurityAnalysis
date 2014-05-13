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

import pl.edu.agh.security.warehouse.client.IWarehouseService;
import pl.edu.agh.security.warehouse.client.WarehouseRequest;
import pl.edu.agh.security.warehouse.client.WarehouseResponse;

public class WarehouseWorkItemHandler implements
		WorkItemHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseWorkItemHandler.class);
	private static final String WAREHOUSE_REQUEST_PATH = "http://esb.security.agh.edu.pl:8080/rest-binding/warehouse-service";
	private static final String WAREHOUSE_REQUEST_PATH_OPENSHIFT = "https://orderprocess-tomash.rhcloud.com/rest-binding/warehouse-service";

	public static final String WAREHOUSE_RESPONSE_PARAMTETER = "WarehouseResponse";

	@Override
	public void abortWorkItem(WorkItem arg0, WorkItemManager arg1) {
	}

	@Override
	public void executeWorkItem(WorkItem workItem,
			WorkItemManager workItemManager) {

		String productName = (String) workItem
				.getParameter(StoreStateRequestWorkItemHandler.PRODUCT_PARAMETER);
		int count = (Integer) workItem
				.getParameter(StoreStateRequestWorkItemHandler.COUNT_PARAMETER);
		String assertion = (String) workItem
				.getParameter(AuthenticateWorkItemHandler.ASSERTION_PARAMETER);
		IWarehouseService warehouseService = prepareWarehouseServiceClient(assertion);
		WarehouseRequest warehouseRequest = new WarehouseRequest();
		warehouseRequest.setCount(count);
		warehouseRequest.setProduct(productName);
		WarehouseResponse warehouseResponse = warehouseService
				.registerTransaction(warehouseRequest);
		LOGGER.info("Obtained response from warehouse service: {}", warehouseResponse);
		Map<String, Object> results = new HashMap<String, Object>();
		results.put(WAREHOUSE_RESPONSE_PARAMTETER,
				warehouseResponse);
		workItemManager.completeWorkItem(workItem.getId(), results);
	}

	public IWarehouseService prepareWarehouseServiceClient(
			final String samlAssertionString) {
		DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
		ApacheHttpClient4Executor executor = new ApacheHttpClient4Executor(
				defaultHttpClient) {

			@SuppressWarnings("rawtypes")
			@Override
			public ClientResponse execute(ClientRequest request)
					throws Exception {
			    request.header("samlAssertion", samlAssertionString);
			    LOGGER.info("Warehouse request uri: "+request.getUri());
                LOGGER.info("Warehouse request headers: "+request.getHeaders());
                ClientResponse response = super.execute(request);
                LOGGER.info("Warehouse response: status={}, responseStatus={}", response.getStatus(), response.getResponseStatus());
                return response;
			}
		};

		// This initialization only needs to be done once per VM
		RegisterBuiltin.register(ResteasyProviderFactory.getInstance());

		return ProxyFactory.create(IWarehouseService.class,
				WAREHOUSE_REQUEST_PATH_OPENSHIFT, executor);
	}

}
