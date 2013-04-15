package pl.edu.agh.security.deps.financial.service.rest;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "registerTransactionResponse")
@XmlType(name = "registerTransactionResponse", namespace = "http://service.financial.deps.security.agh.edu.pl/")
public class TransactionResponse {

	private String invoiceIdentifier;

	private Date dueDate;

	public TransactionResponse() {
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
