package pl.edu.agh.security.delivery;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import pl.edu.agh.security.common.services.Delivery;
import pl.edu.agh.security.common.services.DeliveryState;
import pl.edu.agh.security.common.services.IDeliveryService;

@Path("/delivery")
public class DeliveryService implements IDeliveryService {

	@Override
	@PUT
	public DeliveryState putDelivery(Delivery body) {
		DeliveryState deliveryState = new DeliveryState();
		deliveryState.setEta(Calendar.getInstance().getTime());
		deliveryState.setPrice(new BigDecimal(body.getWeight() * 5.0));
		return deliveryState;
	}
}
