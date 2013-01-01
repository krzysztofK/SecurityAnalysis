package pl.edu.agh.security.rest.provider.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class IProviderClientTest {

	private static final String REQUEST_PATH = "http://localhost:8080/rest-provider/provider";
	private static final String HOST = "localhost";
	private static final int PORT = 8080;
	private static final String USER_NAME = "magister";
	private static final String PASSWORD = "inzynier";

	private static final String USER_NAME2 = "doktor";
	private static final String PASSWORD2 = "habilitowany";

	private static final String UNAUTHORIZED_ACCESS_MESSAGE = "Error status 401 Unauthorized returned";

	static IProviderClient client;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	static {
		prepareClient(USER_NAME, PASSWORD);
	}

	@Test
	public void testGetProducts() {
		Collection<Product> products = client.getProducts();
		assertNotNull("Products not null", products);
		assertEquals(4, products.size());
	}

	@Test
	public void testGetProduct() {
		Product product = client.getProduct("001");
		assertNotNull("Product not null", product);
		assertEquals("Product 1", product.getName());
		assertEquals("001", product.getId());
	}

	@Test
	public void testAddProduct() {
		Product product = client.addProduct("123", "Product new");
		assertNotNull("Product not null", product);
		assertEquals("Product new", product.getName());
		assertEquals("123", product.getId());
		Collection<Product> products = client.getProducts();
		assertNotNull("Products not null", products);
		assertEquals(5, products.size());
	}

	@Test
	public void testUpdateProduct() {
		Product product = client.updateProduct("123", "Product old");
		assertNotNull("Product not null", product);
		assertEquals("Product old", product.getName());
		assertEquals("123", product.getId());
		Collection<Product> products = client.getProducts();
		assertNotNull("Books not null", products);
		assertEquals(5, products.size());
	}

	@Test
	public void testRemoveProduct() {
		Product product = client.removeProduct("123");
		assertNotNull("Book not null", product);
		assertEquals("Product old", product.getName());
		assertEquals("123", product.getId());
		Collection<Product> products = client.getProducts();
		assertNotNull("Books not null", products);
		assertEquals(4, products.size());
	}

	@Test
	public void testAuthentication() {
		prepareClient(USER_NAME2, PASSWORD2);
		testGetProducts();
		expectedException.expect(ClientResponseFailure.class);
		expectedException.expectMessage(UNAUTHORIZED_ACCESS_MESSAGE);
		testGetProduct();
	}

	private static final void prepareClient(String user, String password) {
		// authentication information
		DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
		defaultHttpClient.getCredentialsProvider().setCredentials(
				new AuthScope(HOST, PORT),
				new UsernamePasswordCredentials(user, password));
		ApacheHttpClient4Executor executor = new ApacheHttpClient4Executor(
				defaultHttpClient);

		// This initialization only needs to be done once per VM
		RegisterBuiltin.register(ResteasyProviderFactory.getInstance());

		client = ProxyFactory.create(IProviderClient.class, REQUEST_PATH,
				executor);

	}
}
