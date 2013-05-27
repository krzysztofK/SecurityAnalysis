package pl.edu.agh.security.bpmn.work.items;

import org.drools.process.instance.WorkItemHandler;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;

public class RegisterFinancialTransactionWorkItemHandler implements
		WorkItemHandler {

	private static final String INVOICE_REQUESTED_PARAMETER = "InvoiceRequested";

	@Override
	public void abortWorkItem(WorkItem arg0, WorkItemManager arg1) {
	}

	@Override
	public void executeWorkItem(WorkItem workItem,
			WorkItemManager workItemManager) {

		boolean invoiceRequested = (Boolean) workItem
				.getParameter(INVOICE_REQUESTED_PARAMETER);
		String product = (String) workItem
				.getParameter(StoreStateRequestWorkItemHandler.PRODUCT_PARAMETER);
		int count = (Integer) workItem
				.getParameter(StoreStateRequestWorkItemHandler.COUNT_PARAMETER);
		System.out.println(product + " " + count + " " + invoiceRequested);
	}
}
