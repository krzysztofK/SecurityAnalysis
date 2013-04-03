package pl.edu.agh.secutiy.deps.financial.service.rest;


public class TransactionRequest {

	private Product product;

	private int count;

	private boolean isInvoiceRequested;

	public TransactionRequest() {
		System.out.println("jazda");
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
