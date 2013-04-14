package pl.edu.agh.security.deps.financial.service.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(name ="transactionRequest")
//@XmlAccessorType(XmlAccessType.FIELD)
public class TransactionRequest {

	private Product product;

	private int count;

	private boolean isInvoiceRequested;

	public TransactionRequest() {
	}

	public boolean isInvoiceRequested() {
		return isInvoiceRequested;
	}

	public void setInvoiceRequested(boolean invoiceRequested) {
		this.isInvoiceRequested = invoiceRequested;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
