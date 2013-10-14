package pl.edu.agh.security.store.state.objects.id;

import java.io.Serializable;

import javax.persistence.Embeddable;

import pl.edu.agh.security.store.state.objects.Product;
import pl.edu.agh.security.store.state.objects.Store;

/**
 * 
 * @author Krzysztof
 *
 */
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + ((store == null) ? 0 : store.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductCountInStoreId other = (ProductCountInStoreId) obj;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (store == null) {
			if (other.store != null)
				return false;
		} else if (!store.equals(other.store))
			return false;
		return true;
	}

	
}
