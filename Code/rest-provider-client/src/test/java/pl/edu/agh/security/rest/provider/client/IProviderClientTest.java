package pl.edu.agh.security.rest.provider.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.jboss.resteasy.client.ClientResponseFailure;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public abstract class IProviderClientTest {

	protected static final String REQUEST_PATH = "http://localhost:8080/rest-provider/provider";
	protected static final String HOST = "localhost";
	protected static final int PORT = 8080;
	protected static final String USER_NAME = "magister";
	protected static final String PASSWORD = "inzynier";

	private static final String USER_NAME2 = "doktor";
	private static final String PASSWORD2 = "habilitowany";

	private static final String UNAUTHORIZED_ACCESS_MESSAGE = "Error status 401 Unauthorized returned";

	protected IProviderClient client;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

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
	
	protected abstract void prepareClient(String userName, String password);
}
