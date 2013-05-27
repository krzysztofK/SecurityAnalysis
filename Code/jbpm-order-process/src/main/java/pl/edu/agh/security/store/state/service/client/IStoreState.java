package pl.edu.agh.security.store.state.service.client;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.Form;

@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface IStoreState {

	@GET
	@Path("/store/{name}")
	public Store getStore(@Form StoreStateRequest stateRequest);
}
