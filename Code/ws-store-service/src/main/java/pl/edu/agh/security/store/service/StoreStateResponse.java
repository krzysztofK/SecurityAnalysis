package pl.edu.agh.security.store.service;

import pl.edu.agh.security.store.objects.Product;

/**
 * 
 * @author Krzysztof
 * 
 */
public class StoreStateResponse {

	private Product product;

	private int count;

	public StoreStateResponse() {
		super();
	}

	public StoreStateResponse(Product product, int count) {
		super();
		this.product = product;
		this.count = count;
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
