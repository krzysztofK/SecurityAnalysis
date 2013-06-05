package pl.edu.agh.security.test.client;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import pl.edu.agh.security.delivery.marshallers.TestDataMarshaller;

/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) throws Exception {
	    ClientRequest request = new ClientRequest("http://localhost:8080/delivery-service/rest/deliveries/create");
	    request.body("application/xml", TestDataMarshaller.createSamplePayload());
	    ClientResponse<String> response = request.post(String.class);
	    if (response.getStatus() == 200) // OK!
	    {
	       String str = response.getEntity();
	       System.out.println("Received response: \n"+str);
	    }
	}
}
