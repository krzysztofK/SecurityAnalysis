package pl.edu.agh.security.esb.delivery;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import pl.edu.agh.security.delivery.pojos.DeliveryRequest;

@Path("/deliveries")
public interface IDeliveryService {
	@POST
    @Path("/create")
    @Consumes({ "application/xml" })
    @Produces({ "text/plain" })
    @RolesAllowed({ "magister", "doktor" })
    public Integer registerDelivery(DeliveryRequest request);
}
