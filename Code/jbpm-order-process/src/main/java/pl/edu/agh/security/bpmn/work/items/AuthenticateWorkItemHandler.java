package pl.edu.agh.security.bpmn.work.items;

import java.util.HashMap;
import java.util.Map;

import org.drools.process.instance.WorkItemHandler;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;
import org.picketlink.identity.federation.core.exceptions.ConfigurationException;
import org.picketlink.identity.federation.core.exceptions.ParsingException;
import org.picketlink.identity.federation.core.exceptions.ProcessingException;
import org.picketlink.identity.federation.core.saml.v2.util.DocumentUtil;
import org.w3c.dom.Element;

import pl.edu.agh.security.common.Utils;

public class AuthenticateWorkItemHandler implements WorkItemHandler {

	private static final String USER_PARAMETER = "User";
	private static final String PASSWORD_PARAMETER = "Password";

	private static final String STS_SERVICE_NAME = "PicketLinkSTS";
	private static final String STS_PORT = "PicketLinkSTSPort";
	private static final String STS_ENDPOINT_URI = "http://sts.security.agh.edu.pl:8080/picketlink-sts/PicketLinkSTS";

	@Override
	public void abortWorkItem(WorkItem arg0, WorkItemManager arg1) {

	}

	@Override
	public void executeWorkItem(WorkItem workItem,
			WorkItemManager workItemManager) {
		String user = (String) workItem.getParameter(USER_PARAMETER);
		String password = (String) workItem.getParameter(PASSWORD_PARAMETER);
		System.out.println(user + " " + password);

		Element samlAssertion;
		try {
			samlAssertion = Utils.retrieveSamlAssertion(STS_SERVICE_NAME,
					STS_PORT, STS_ENDPOINT_URI, user, password);
			String samlAssertionString = DocumentUtil
					.getNodeAsString(samlAssertion);
			Map<String, Object> results = new HashMap<String, Object>();
			results.put("Assertion", samlAssertionString);
			System.out.println(samlAssertionString);
			workItemManager.completeWorkItem(workItem.getId(), results);
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
