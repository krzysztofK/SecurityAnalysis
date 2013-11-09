package pl.edu.agh.security.order.process;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;

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
import org.picketlink.trust.jbossws.SAML2Constants;
import org.picketlink.trust.jbossws.handler.SAML2Handler;
import org.w3c.dom.Element;

import pl.edu.agh.security.common.Utils;
import pl.edu.agh.security.delivery.pojos.DeliveryRequest;
import pl.edu.agh.security.delivery.pojos.Priority;
import pl.edu.agh.security.store.state.service.client.IDeliveryService;
import pl.edu.agh.security.store.state.service.client.IStoreState;
import pl.edu.agh.security.store.state.service.client.Store;
import pl.edu.agh.security.store.state.service.client.StoreStateRequest;

public class OrderProcess {

    private static final Logger LOGGER = Logger.getLogger(OrderProcess.class);
    private static final String USER_NAME = "magister";
    private static final String PASSWORD = "inzynier";

    private static final String ORDERED_PRODUCT = "woda";
    private static final int COUNT = 3;
    private static final boolean INVOICE_REQUESTED = true;
    private static final String COMPANY_NAME = "ACME";

    private static final String STS_SERVICE_NAME = "PicketLinkSTS";
    private static final String STS_PORT = "PicketLinkSTSPort";

    private static final String STORES_REQUEST_PATH_LOCAL = "http://stores-states.security.agh.edu.pl:8080/stores-state-service/state";
    private static final String DELIVERY_REQUEST_PATH_LOCAL = "http://delivery.security.agh.edu.pl:8080/delivery-service/rest";
    private static final String STS_ENDPOINT_URI_LOCAL = "http://localhost:8080/picketlink-sts/PicketLinkSTS";
    private static final String STORES_HOST_LOCAL = "stores-states.security.agh.edu.pl";
    private static final String DELIVERY_HOST_LOCAL = "delivery.security.agh.edu.pl";
    private static final int PORT_LOCAL = 8080;

    private static final String STORES_REQUEST_PATH_OPENSHIFT = "http://orderprocess-tomash.rhcloud.com/stores-state-service/state";
    private static final String DELIVERY_REQUEST_PATH_OPENSHIFT = "http://orderprocess-tomash.rhcloud.com/delivery-service/rest";
    private static final String STS_ENDPOINT_URI_OPENSHIFT = "http://orderprocess-tomash.rhcloud.com/picketlink-sts/PicketLinkSTS";
    private static final String STORES_HOST_OPENSHIFT = "orderprocess-tomash.rhcloud.com";
    private static final String DELIVERY_HOST_OPENSHIFT = "orderprocess-tomash.rhcloud.com";
    private static final int PORT_OPENSHIFT = 80;
    
    private Element samlAssertion;
    private String samlAssertionString;
    private final String userName;
    private final String password;
    private final String orderedProduct;
    private final int count;
    private final boolean invoiceRequested;
    
    private final String storesUrl;
    private final String deliveryUrl;
    private final String stsUrl;
    private final String storesHost;
    private final String deliveryHost;
    private final int port;
    

    public OrderProcess(String userName, String password, String orderedProduct, int count, boolean invoiceRequested,
            boolean openshift) {
        super();
        this.userName = userName;
        this.password = password;
        this.orderedProduct = orderedProduct;
        this.count = count;
        this.invoiceRequested = invoiceRequested;
        this.storesUrl = openshift ? STORES_REQUEST_PATH_OPENSHIFT : STORES_REQUEST_PATH_LOCAL;
        this.deliveryUrl = openshift ? DELIVERY_REQUEST_PATH_OPENSHIFT : DELIVERY_REQUEST_PATH_LOCAL;
        this.stsUrl = openshift ? STS_ENDPOINT_URI_OPENSHIFT : STS_ENDPOINT_URI_LOCAL;
        this.storesHost = openshift ? STORES_HOST_OPENSHIFT : STORES_HOST_LOCAL;
        this.deliveryHost = openshift ? DELIVERY_HOST_OPENSHIFT : DELIVERY_HOST_LOCAL;
        this.port = openshift ? PORT_OPENSHIFT : PORT_LOCAL;
    }

    public void execute() throws ConfigurationException, ProcessingException, ParsingException {
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
        LOGGER.info("Sending request to store service: " + stateRequest);
        Store store = prepareStoreStateServiceClient().getStore(stateRequest);
        LOGGER.info("Received store info: " + store);

        if (store != null) {
            DeliveryRequest deliveryRequest = new DeliveryRequest();
            deliveryRequest.setSenderAddress(store.getLocation());
            deliveryRequest.setSenderName(COMPANY_NAME);
            deliveryRequest.setPriority(Priority.NORMAL);
            Integer deliveryId = prepareDeliveryServiceClient().registerDelivery(deliveryRequest);
            LOGGER.info("delivery id is: " + deliveryId);

            /**
             * Financial service execution
             */
            FinancialOperations financialOperations = prepareFinancialServiceClient();
            TransactionRequest transactionRequest = new TransactionRequest();
            transactionRequest.setCount(count);
            Product product = new Product();
            product.setName(orderedProduct);
            transactionRequest.setProduct(product);
            transactionRequest.setInvoiceRequested(invoiceRequested);

            TransactionResponse transactionResponse = financialOperations.registerTransaction(transactionRequest);
            if (transactionResponse != null) {
                LOGGER.info("Transaction successful. Due date: " + transactionResponse.getDueDate()
                        + ", delivery number: " + deliveryId + ", invoice number: "
                        + transactionResponse.getInvoiceIdentifier() + ", store: " + store.getLocation());
            }
        } else {
            LOGGER.warn("Requested count of the product not available");
        }
    }

    public void authenticate() throws ConfigurationException, ProcessingException, ParsingException {
        samlAssertion = Utils.retrieveSamlAssertion(STS_SERVICE_NAME, STS_PORT, stsUrl, userName, password);
        samlAssertionString = DocumentUtil.getNodeAsString(samlAssertion);
        LOGGER.info("Obtained saml assertion: " + samlAssertionString);
    }

    public IStoreState prepareStoreStateServiceClient() throws ConfigurationException, ProcessingException,
            ParsingException {
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        ApacheHttpClient4Executor executor = new ApacheHttpClient4Executor(defaultHttpClient) {

            @SuppressWarnings("rawtypes")
            @Override
            public ClientResponse execute(ClientRequest request) throws Exception {
                request.header("samlAssertion", samlAssertionString);
                LOGGER.info("Store state request uri: " + request.getUri());
                LOGGER.info("Store state request headers: " + request.getHeaders());
                ClientResponse response = super.execute(request);
                LOGGER.info("Store state response : " + response);
                return response;
            }
        };

        // This initialization only needs to be done once per VM
        RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
        defaultHttpClient.getCredentialsProvider().setCredentials(new AuthScope(storesHost, port),
                new UsernamePasswordCredentials(userName, samlAssertionString));

        return ProxyFactory.create(IStoreState.class, storesUrl, executor);
    }

    public IDeliveryService prepareDeliveryServiceClient() throws ConfigurationException, ProcessingException,
            ParsingException {
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        ApacheHttpClient4Executor executor = new ApacheHttpClient4Executor(defaultHttpClient) {

            @SuppressWarnings("rawtypes")
            @Override
            public ClientResponse execute(ClientRequest request) throws Exception {
                request.header("samlAssertion", samlAssertionString);
                LOGGER.info("Delivery request uri: " + request.getUri());
                LOGGER.info("Delivery request headers: " + request.getHeaders());
                ClientResponse response = super.execute(request);
                LOGGER.info("Delivery response : " + response);
                return response;
            }
        };

        // This initialization only needs to be done once per VM
        RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
        defaultHttpClient.getCredentialsProvider().setCredentials(new AuthScope(deliveryHost, port),
                new UsernamePasswordCredentials(userName, samlAssertionString));

        return ProxyFactory.create(IDeliveryService.class, deliveryUrl, executor);
    }

    public FinancialOperations prepareFinancialServiceClient() {
        FinancialOperations financialOperations = new FinancialOperationsService().getFinancialOperationsPort();

        BindingProvider bindingProvider = (BindingProvider) financialOperations;
        Map<String, List<String>> headers = new HashMap<String, List<String>>();
        headers.put("samlAssertion", Collections.singletonList(samlAssertionString));
        bindingProvider.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, headers);

        bindingProvider.getRequestContext().put(SAML2Constants.SAML2_ASSERTION_PROPERTY, samlAssertion);

        List<Handler> handlers = bindingProvider.getBinding().getHandlerChain();
        handlers.add(new SAML2Handler());
        bindingProvider.getBinding().setHandlerChain(handlers);
        return financialOperations;
    }

    public static void main(String[] args) throws ConfigurationException, ProcessingException, ParsingException {
        OrderProcess orderProcess = new OrderProcess(USER_NAME, PASSWORD, ORDERED_PRODUCT, COUNT, INVOICE_REQUESTED, true);
        orderProcess.execute();
    }

}
