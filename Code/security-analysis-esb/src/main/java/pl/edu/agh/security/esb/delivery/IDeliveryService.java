package pl.edu.agh.security.esb.delivery;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import pl.edu.agh.security.delivery.pojos.DeliveryRequest;

@Path("/deliveries")
public interface IDeliveryService {
	@POST
    @Path("/create")
    @Consumes({ "application/xml" })
    @Produces({ "text/plain" })
    @RolesAllowed({ "magister", "doktor" })
    public Integer registerDelivery(DeliveryRequest request);
	
	@GET
    @Path("/delivery/{id}")
    @Produces({ "application/xml" })
    @RolesAllowed({ "magister", "doktor" })
    public DeliveryRequest receiveDeliveryRequest(@PathParam("id") Integer id);
	
	@GET
    @Path("/smoketest/{param}")
    public Response printMessage(@PathParam("param") String msg);
}
