package pl.edu.agh.security.esb.store.state;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.Form;

@Path("/state")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface IStoreState {

	@GET
	@Path("/store/{name}")
	public Store getStore(@Form StoreStateRequest stateRequest);
}
