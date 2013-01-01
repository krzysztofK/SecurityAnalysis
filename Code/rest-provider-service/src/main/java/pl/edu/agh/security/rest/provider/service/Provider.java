package pl.edu.agh.security.rest.provider.service;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/provider")
@Consumes({ "application/json" })
@Produces({ "application/json" })
@RolesAllowed({ "magister", "doktor" })
@DeclareRoles({ "magister", "doktor" })
public class Provider {

	private static Map<String, Product> products = new LinkedHashMap<String, Product>();

	static {
		products.put("001", new Product("001", "Product 1"));
		products.put("002", new Product("002", "Product 2"));
		products.put("003", new Product("003", "Product 3"));
		products.put("004", new Product("004", "Product 4"));
	}

	@GET
	@Path("/products")
	@PermitAll
	public Collection<Product> getProducts() {
		return products.values();
	}

	@GET
	@Path("/product/{id}")
	@RolesAllowed({ "magister" })
	public Product getProduct(@PathParam("id") String id) {
		return products.get(id);
	}

	@PUT
	@Path("/product/{id}")
	@RolesAllowed({ "magister" })
	public Product addProduct(@PathParam("id") String id,
			@QueryParam("name") String name) {
		Product product = new Product(id, name);
		products.put(id, product);
		return product;
	}

	@POST
	@Path("/product/{id}")
	@RolesAllowed({ "magister" })
	public Product updateProduct(@PathParam("id") String id, String name) {
		Product product = products.get(id);
		if (product != null) {
			product.setName(name);
		}
		return product;
	}

	@DELETE
	@Path("/product/{id}")
	@RolesAllowed({ "magister" })
	public Product removeProduct(@PathParam("id") String id) {
		Product product = products.remove(id);
		return product;
	}
}
