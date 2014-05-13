package pl.edu.agh.security.warehouse.client;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface IWarehouseService {

	@PUT
	@Path("/warehouse")
	public WarehouseResponse registerTransaction(
			WarehouseRequest transactionRequest);
}