package pl.edu.agh.security.delivery;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import pl.edu.agh.security.delivery.dao.DeliveryDAO;
import pl.edu.agh.security.delivery.entities.Delivery;
import pl.edu.agh.security.delivery.mappers.DeliveryMapper;
import pl.edu.agh.security.delivery.pojos.DeliveryRequest;

@Stateless
@Path("/deliveries")
public class DeliveryService {
    
    @EJB
    private DeliveryDAO deliveryDAO;
    
    @EJB
    private DeliveryMapper deliveryMapper;

    @POST
    @Path("/create")
    @Consumes({ "application/xml" })
    @Produces({ "text/plain" })
    @RolesAllowed({ "magister", "doktor" })
    public Integer registerDelivery(DeliveryRequest request) {
        Delivery entity = deliveryMapper.mapToEntity(request);
        Integer id = deliveryDAO.insertDelivery(entity);
        return id;
    }
    
    
    @GET
    @Path("/delivery/{id}")
    @Produces({ "application/xml" })
    @RolesAllowed({ "magister", "doktor" })
    public DeliveryRequest receiveDeliveryRequest(@PathParam("id") Integer id) {
        Delivery entity = deliveryDAO.obtainDelivery(id);
        return deliveryMapper.mapToPojo(entity);
    }
    

    @GET
    @Path("/smoketest/{param}")
    public Response printMessage(@PathParam("param") String msg) {
        String result = "Smoke test : " + msg;
        return Response.status(200).entity(result).build();

    }
}
