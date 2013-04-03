package pl.edu.agh.secutiy.deps.financial.service.rest;

import java.util.Date;

public class TransactionResponse {

	private String invoiceIdentifier;

	private Date dueDate;

	public TransactionResponse() {
		System.out.println("jazda");
	}

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
