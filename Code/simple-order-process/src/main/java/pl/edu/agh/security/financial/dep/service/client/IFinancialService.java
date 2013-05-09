package pl.edu.agh.security.financial.dep.service.client;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface IFinancialService {

	@PUT
	@Path("/transactions")
	public TransactionResponse registerTransaction(
			TransactionRequest transactionRequest);
}