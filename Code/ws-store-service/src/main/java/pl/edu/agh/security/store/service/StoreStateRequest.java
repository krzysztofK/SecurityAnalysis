package pl.edu.agh.security.store.service;

import pl.edu.agh.security.store.objects.Product;

/**
 * 
 * @author Krzysztof
 * 
 */
public class StoreStateRequest {

	private Product product;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
