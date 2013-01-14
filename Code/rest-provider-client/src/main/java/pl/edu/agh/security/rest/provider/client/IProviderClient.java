package pl.edu.agh.security.rest.provider.client;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface IProviderClient {

	@GET
	@Path("/products")
	public Collection<Product> getProducts();

	@GET
	@Path("/product/{id}")
	public Product getProduct(@PathParam("id") String id);

	@PUT
	@Path("/product/{id}")
	public Product addProduct(@PathParam("id") String id,
			@QueryParam("name") String name);

	@POST
	@Path("/product/{id}")
	public Product updateProduct(@PathParam("id") String id, String name);

	@DELETE
	@Path("/product/{id}")
	public Product removeProduct(@PathParam("id") String id);
}
