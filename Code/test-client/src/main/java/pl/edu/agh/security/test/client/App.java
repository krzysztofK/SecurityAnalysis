package pl.edu.agh.security.test.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.ext.ClientFactory;

/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) {
		Client client = ClientFactory.newClient();
		String resp = client.target("localhost:8080/delivery-service/delivery").request().get(String.class);
		System.out.println("resp: "+resp);
	}
}
