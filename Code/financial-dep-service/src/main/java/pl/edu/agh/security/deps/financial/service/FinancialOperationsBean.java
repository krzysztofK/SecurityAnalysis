package pl.edu.agh.security.deps.financial.service;

import java.util.Calendar;
import java.util.Random;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;

import org.jboss.ejb3.annotation.SecurityDomain;

@Stateless
@WebService(name = "FinancialOperations", serviceName = "FinancialOperationsService")
@SOAPBinding(parameterStyle = ParameterStyle.BARE)
@SecurityDomain("ws-ldap-saml")
@HandlerChain(file = "/META-INF/handlers.xml")
public class FinancialOperationsBean {

	private static final int DAYS_TO_DUE_DATE = 7;

	@RolesAllowed({ "magister" })
	@WebMethod
	public TransactionResponse registerTransaction(
			TransactionRequest transactionRequest) {
		TransactionResponse transactionResponse = new TransactionResponse();
		if (transactionRequest.isInvoiceRequested()) {
			transactionResponse.setInvoiceIdentifier(new Long(new Random()
					.nextLong()).toString());
		}
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DATE, DAYS_TO_DUE_DATE);
		transactionResponse.setDueDate(now.getTime());
		return transactionResponse;
	}
}
