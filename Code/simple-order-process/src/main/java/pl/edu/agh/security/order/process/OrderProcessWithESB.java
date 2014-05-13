package pl.edu.agh.security.order.process;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
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
import org.w3c.dom.Element;

import pl.edu.agh.security.common.Utils;
import pl.edu.agh.security.delivery.pojos.DeliveryRequest;
import pl.edu.agh.security.delivery.pojos.Priority;
import pl.edu.agh.security.financial.dep.service.client.IFinancialService;
import pl.edu.agh.security.financial.dep.service.client.Product;
import pl.edu.agh.security.financial.dep.service.client.TransactionRequest;
import pl.edu.agh.security.financial.dep.service.client.TransactionResponse;
import pl.edu.agh.security.store.state.service.client.IDeliveryService;
import pl.edu.agh.security.store.state.service.client.IStoreState;
import pl.edu.agh.security.store.state.service.client.Store;
import pl.edu.agh.security.store.state.service.client.StoreStateRequest;
import pl.edu.agh.security.warehouse.client.IWarehouseService;
import pl.edu.agh.security.warehouse.client.WarehouseRequest;
import pl.edu.agh.security.warehouse.client.WarehouseResponse;

public class OrderProcessWithESB {
    
    private static final Logger LOGGER = Logger.getLogger(OrderProcessWithESB.class);
	private static final String STORES_REQUEST_PATH_LOCAL = "http://esb.security.agh.edu.pl:8080/rest-store-binding/state";
	private static final String STORES_REQUEST_PATH_OPENSHIFT = "http://orderprocess-tomash.rhcloud.com/rest-store-binding/state";
	private static final String DELIVERY_REQUEST_PATH_LOCAL = "http://esb.security.agh.edu.pl:8080/rest-delivery-binding";
	private static final String DELIVERY_REQUEST_PATH_OPENSHIFT = "http://orderprocess-tomash.rhcloud.com/rest-delivery-binding";
	private static final String FINANCE_REQUEST_PATH_LOCAL = "http://esb.security.agh.edu.pl:8080/rest-binding/financial-service";
	private static final String FINANCE_REQUEST_PATH_OPENSHIFT = "http://orderprocess-tomash.rhcloud.com/rest-binding/financial-service";
	private static final String WAREHOUSE_REQUEST_PATH_LOCAL = "http://esb.security.agh.edu.pl:8080/rest-binding/warehouse-service";
	private static final String WAREHOUSE_REQUEST_PATH_OPENSHIFT = "http://orderprocess-tomash.rhcloud.com/rest-binding/warehouse-service";

	private static final String DELIVERY_HOST_LOCAL = "delivery.security.agh.edu.pl";
	private static final String DELIVERY_HOST_OPENSHIFT = "http://orderprocess-tomash.rhcloud.com";

	private static final int PORT_LOCAL = 8080;
	private static final int PORT_OPENSHIFT = 80;
	private static final String STS_SERVICE_NAME = "PicketLinkSTS";
	private static final String STS_PORT = "PicketLinkSTSPort";
	private static final String STS_ENDPOINT_URI_LOCAL = "http://localhost:8080/picketlink-sts/PicketLinkSTS";
	private static final String STS_ENDPOINT_URI_OPENSHIFT = "http://orderprocess-tomash.rhcloud.com/picketlink-sts/PicketLinkSTS";
	private static final String COMPANY_NAME = "ACME";

	private Element samlAssertion;
	private String samlAssertionString;
	private final String userName;
	private final String password;
	private final String orderedProduct;
	private final int count;
	private final boolean invoiceRequested;
	
	private final String storesUrl;
    private final String deliveryUrl;
    private final String financeUrl;
    private final String warehouseUrl;
    private final String stsUrl;
    //private final String storesHost;
    private final String deliveryHost;
    private final int port;

	public OrderProcessWithESB(String userName, String password,
			String orderedProduct, int count, boolean invoiceRequested, boolean openshift) {
		super();
		this.userName = userName;
		this.password = password;
		this.orderedProduct = orderedProduct;
		this.count = count;
		this.invoiceRequested = invoiceRequested;
		
		this.storesUrl = openshift ? STORES_REQUEST_PATH_OPENSHIFT : STORES_REQUEST_PATH_LOCAL;
        this.deliveryUrl = openshift ? DELIVERY_REQUEST_PATH_OPENSHIFT : DELIVERY_REQUEST_PATH_LOCAL;
        this.financeUrl = openshift ? FINANCE_REQUEST_PATH_OPENSHIFT : FINANCE_REQUEST_PATH_LOCAL;
        this.warehouseUrl = openshift ? WAREHOUSE_REQUEST_PATH_OPENSHIFT : WAREHOUSE_REQUEST_PATH_LOCAL;
        this.stsUrl = openshift ? STS_ENDPOINT_URI_OPENSHIFT : STS_ENDPOINT_URI_LOCAL;
        //this.storesHost = openshift ? STORES_HOST_OPENSHIFT : STORES_HOST_LOCAL;
        this.deliveryHost = openshift ? DELIVERY_HOST_OPENSHIFT : DELIVERY_HOST_LOCAL;
        this.port = openshift ? PORT_OPENSHIFT : PORT_LOCAL;
	}

	public void execute() throws ConfigurationException, ProcessingException,
			ParsingException {
		/**
		 * Retrieves SAML assertion
		 */
		authenticate();

		StoreStateRequest stateRequest = new StoreStateRequest();
		stateRequest.setCount(count);
		stateRequest.setProductName(orderedProduct);
		/**
		 * Looks for store with requested count of the product
		 */
		LOGGER.info("Sending request to store service: "+stateRequest);
		Store store = prepareStoreStateServiceClient().getStore(stateRequest);
		LOGGER.info("Received store info: "+store);
		if (store != null) {
		    DeliveryRequest deliveryRequest = new DeliveryRequest();
            deliveryRequest.setSenderAddress(store.getLocation());
            deliveryRequest.setSenderName(COMPANY_NAME);
            deliveryRequest.setPriority(Priority.NORMAL);
            Integer deliveryId = prepareDeliveryServiceClient().registerDelivery(deliveryRequest);
            LOGGER.info("delivery id is: "+ deliveryId);

			/**
			 * Financial service execution
			 */
			IFinancialService financialOperations = prepareFinancialServiceClient();

			TransactionRequest transactionRequest = new TransactionRequest();
			transactionRequest.setCount(count);
			Product product = new Product();
			product.setName(orderedProduct);
			transactionRequest.setProduct(product);
			transactionRequest.setInvoiceRequested(invoiceRequested);

			TransactionResponse transactionResponse = financialOperations
					.registerTransaction(transactionRequest);
			
			IWarehouseService warehouse = prepareWarehouseServiceClient();
			WarehouseRequest warehouseRequest = new WarehouseRequest();
			warehouseRequest.setCount(count);
			warehouseRequest.setProduct(orderedProduct);
			WarehouseResponse warehouseResponse = warehouse.registerTransaction(warehouseRequest);
			
			if (transactionResponse != null) {
			    LOGGER.info("Transaction successful. Due date: "
                        + transactionResponse.getDueDate()
                        + ", delivery number: "
                        + deliveryId
                        + ", invoice number: "
                        + transactionResponse.getInvoiceIdentifier()
                        + ", store: " + store.getLocation()
                        + ", warehouse response: " + warehouseResponse);
			}
		} else {
			System.out.println("Requested count of the product not available");
		}
	}

	public void authenticate() throws ConfigurationException,
			ProcessingException, ParsingException {
		samlAssertion = Utils.retrieveSamlAssertion(STS_SERVICE_NAME, STS_PORT,
				stsUrl, userName, password);
		samlAssertionString = DocumentUtil.getNodeAsString(samlAssertion);
	}

	public IStoreState prepareStoreStateServiceClient()
			throws ConfigurationException, ProcessingException,
			ParsingException {
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

		return ProxyFactory.create(IStoreState.class, storesUrl,
				executor);
	}

	public IDeliveryService prepareDeliveryServiceClient()
			throws ConfigurationException, ProcessingException,
			ParsingException {
		DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
		defaultHttpClient.getCredentialsProvider().setCredentials(
				new AuthScope(deliveryHost, port),
				new UsernamePasswordCredentials(userName, samlAssertionString));
		ApacheHttpClient4Executor executor = new ApacheHttpClient4Executor(
				defaultHttpClient) {

			@SuppressWarnings("rawtypes")
			@Override
			public ClientResponse execute(ClientRequest request)
					throws Exception {
			    request.header("samlAssertion", samlAssertionString);
                LOGGER.info("Delivery request uri: "+request.getUri());
                LOGGER.info("Delivery request headers: "+request.getHeaders());
                ClientResponse response =  super.execute(request);
                LOGGER.info("Delivery response : "+response);
                return response;
			}
		};

		// This initialization only needs to be done once per VM
		RegisterBuiltin.register(ResteasyProviderFactory.getInstance());

		return ProxyFactory.create(IDeliveryService.class,
				deliveryUrl, executor);
	}

	public IFinancialService prepareFinancialServiceClient() {
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

		return ProxyFactory.create(IFinancialService.class,
				financeUrl, executor);
	}
	
	public IWarehouseService prepareWarehouseServiceClient() {
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

		return ProxyFactory.create(IWarehouseService.class,
				warehouseUrl, executor);
	}

}
