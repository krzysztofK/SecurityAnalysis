package pl.edu.agh.security.order.process;

import org.picketlink.identity.federation.core.exceptions.ConfigurationException;
import org.picketlink.identity.federation.core.exceptions.ParsingException;
import org.picketlink.identity.federation.core.exceptions.ProcessingException;

public class MaliciousOrderProcess extends OrderProcess {

	public MaliciousOrderProcess(String userName, String password,
			String orderedProduct, int count, boolean invoiceRequested,
			boolean openshift) {
		super(userName, password, orderedProduct, count, invoiceRequested, openshift);
	}
	
	public static void main(String[] args) throws ConfigurationException, ProcessingException, ParsingException {
		MaliciousOrderProcess orderProcess = new MaliciousOrderProcess(USER_NAME, PASSWORD, ORDERED_PRODUCT, COUNT, INVOICE_REQUESTED, true);
        orderProcess.execute();
    }

}
