package pl.edu.agh.security.store.state.objects.id;

import java.io.Serializable;

import javax.persistence.Embeddable;

import pl.edu.agh.security.store.state.objects.Product;
import pl.edu.agh.security.store.state.objects.Store;

@Embeddable
public class ProductCountInStoreId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -669847061607405505L;

	private Product product;

	private Store store;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
