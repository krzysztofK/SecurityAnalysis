package pl.edu.agh.security.order.process;

import org.picketlink.identity.federation.core.exceptions.ConfigurationException;
import org.picketlink.identity.federation.core.exceptions.ParsingException;
import org.picketlink.identity.federation.core.exceptions.ProcessingException;

public class ProcessMain {

	private static final String USER_NAME = "magister";
	private static final String PASSWORD = "inzynier";

	private static final String ORDERED_PRODUCT = "woda";
	private static final int COUNT = 3;
	private static final boolean INVOICE_REQUESTED = true;

	public static void main(String[] args) throws ConfigurationException,
			ProcessingException, ParsingException {
		OrderProcess orderProcess = new OrderProcess(USER_NAME, PASSWORD,
				ORDERED_PRODUCT, COUNT, INVOICE_REQUESTED);
		orderProcess.execute();
	}
}
