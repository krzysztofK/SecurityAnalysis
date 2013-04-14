package pl.edu.agh.security.deps.financial.service.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/financial-service")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface IFinancialService {

	@PUT
	@Path("/transactions")
	public TransactionResponse registerTransaction(
			TransactionRequest transactionRequest);
}