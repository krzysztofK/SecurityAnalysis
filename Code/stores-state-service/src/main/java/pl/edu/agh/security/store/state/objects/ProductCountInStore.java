package pl.edu.agh.security.store.state.objects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import pl.edu.agh.security.store.state.objects.id.ProductCountInStoreId;

@Entity
@IdClass(ProductCountInStoreId.class)
@Table(name = "PRODUCTCOUNTSINSTORE")
public class ProductCountInStore implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7262078233542673095L;

	@Id
	@ManyToOne
	@JoinColumn(name = "STOREID", nullable = false)
	private Store store;

	@Id
	@ManyToOne
	@JoinColumn(name = "PRODUCTID", nullable = false)
	private Product product;

	@Column(name = "AVAILABLECOUNT")
	private int count;

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
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
