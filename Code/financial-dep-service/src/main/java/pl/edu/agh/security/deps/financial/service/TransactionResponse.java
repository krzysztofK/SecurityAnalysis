package pl.edu.agh.security.deps.financial.service;

import java.util.Date;

public class TransactionResponse {

	private String invoiceIdentifier;

	private Date dueDate;

	public String getInvoiceIdentifier() {
		return invoiceIdentifier;
	}

	public void setInvoiceIdentifier(String invoiceIdentifier) {
		this.invoiceIdentifier = invoiceIdentifier;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

}
