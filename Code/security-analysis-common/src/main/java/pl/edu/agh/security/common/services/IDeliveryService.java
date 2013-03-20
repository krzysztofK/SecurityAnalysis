package pl.edu.agh.security.common.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/delivery")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface IDeliveryService {

	@PUT
	DeliveryState putDelivery(Delivery body);
	
}
