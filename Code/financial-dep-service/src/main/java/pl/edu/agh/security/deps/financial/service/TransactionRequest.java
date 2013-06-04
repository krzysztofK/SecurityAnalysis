package pl.edu.agh.security.deps.financial.service;

import pl.edu.agh.security.deps.financial.objects.Product;

public class TransactionRequest {

	private Product product;

	private int count;

	private boolean isInvoiceRequested;

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

	@Override
	public String toString() {
		return product.getName() + " - " + count;
	}
}
