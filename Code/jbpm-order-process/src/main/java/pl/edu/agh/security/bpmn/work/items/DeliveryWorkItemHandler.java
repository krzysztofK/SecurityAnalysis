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

import pl.edu.agh.security.delivery.pojos.DeliveryRequest;
import pl.edu.agh.security.delivery.pojos.Priority;
import pl.edu.agh.security.esb.delivery.IDeliveryService;
import pl.edu.agh.security.store.state.service.client.IStoreState;
import pl.edu.agh.security.store.state.service.client.Store;
import pl.edu.agh.security.store.state.service.client.StoreStateRequest;

public class DeliveryWorkItemHandler implements WorkItemHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoreStateRequestWorkItemHandler.class);
    private static final String DELIVERY_REQUEST_PATH = "http://esb.security.agh.edu.pl:8080/rest-delivery-binding";
    private static final String DELIVERY_REQUEST_PATH_OPENSHIFT = "http://orderprocess-tomash.rhcloud.com/rest-delivery-binding";
    private static final String COMPANY_NAME = "ACME";
    
    public static final String LOCATION_PARAMETER = "Location";
    public static final String DELIVERY_ID = "DeliveryID";

    @Override
    public void abortWorkItem(WorkItem arg0, WorkItemManager arg1) {
    }

    @Override
    public void executeWorkItem(WorkItem workItem,
            WorkItemManager workItemManager) {
        String store = (String) workItem.getParameter(LOCATION_PARAMETER);
        String assertion = (String) workItem
                .getParameter(AuthenticateWorkItemHandler.ASSERTION_PARAMETER);
        LOGGER.info("Trying to create delivery from {}", store);
        LOGGER.info("Saml assertion is " +assertion);
        try {
            DeliveryRequest deliveryRequest = new DeliveryRequest();
            deliveryRequest.setSenderAddress(store);
            deliveryRequest.setSenderName(COMPANY_NAME);
            deliveryRequest.setPriority(Priority.NORMAL);
            Integer deliveryId = prepareDeliveryServiceClient(assertion).registerDelivery(deliveryRequest);
            LOGGER.info("delivery id is: "+ deliveryId);
            Map<String, Object> results = new HashMap<String, Object>();
            results.put(DELIVERY_ID, deliveryId.toString());
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

    public IDeliveryService prepareDeliveryServiceClient(final String samlAssertionString)
            throws ConfigurationException, ProcessingException, ParsingException {
        
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
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
                DELIVERY_REQUEST_PATH_OPENSHIFT, executor);
    }

}
