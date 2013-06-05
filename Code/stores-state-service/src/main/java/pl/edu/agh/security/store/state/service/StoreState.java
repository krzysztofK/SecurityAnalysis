package pl.edu.agh.security.store.state.service;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import pl.edu.agh.security.store.state.dao.StoreDAO;
import pl.edu.agh.security.store.state.objects.Store;

/**
 * 
 * @author Krzysztof
 * 
 */
@Stateless
@Path("/state")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class StoreState {

	@EJB
	private StoreDAO storeDAO;

	@GET
	@Path("/store/{name}")
	@RolesAllowed({ "magister" })
	public Store getStore(@PathParam("name") String productName,
			@QueryParam("count") int count) {
		System.out.println("Received: " + productName + " - " + count);
		return storeDAO.findStoreByRequestedProduct(productName, count);
	}
}
