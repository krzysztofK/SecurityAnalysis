package pl.edu.agh.security.financial.dep.service.client;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="registerTransaction")
@XmlType(name = "registerTransaction", namespace = "http://service.financial.deps.security.agh.edu.pl/")
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
