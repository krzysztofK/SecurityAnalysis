package pl.edu.agh.security.store.state.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import pl.edu.agh.security.store.state.objects.Store;

@Stateless
public class StoreDAO {

//	@PersistenceContext
	private EntityManager entityManager;

	public Store findStoreByRequestedProduct(String productName, int count) {
		Store store = new Store();
		store.setServiceUrl("krakow.store.security.agh.edu.pl");
		return store;
	}
}
