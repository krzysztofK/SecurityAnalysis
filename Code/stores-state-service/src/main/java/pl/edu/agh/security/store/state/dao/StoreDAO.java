package pl.edu.agh.security.store.state.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import pl.edu.agh.security.store.state.objects.Store;

/**
 * 
 * @author Krzysztof
 * 
 */
@Stateless
public class StoreDAO {

    private static final String FIND_STORE_QUERY = "select productCountInStore.store from ProductCountInStore ProductCountInStore"
            + " where productCountInStore.count >= :count and productCountInStore.product.name = :name";

    @PersistenceContext
    private EntityManager entityManager;

    public Store findStoreByRequestedProduct(String productName, int count) {
        @SuppressWarnings("unchecked")
        List<Store> stores = (List<Store>) entityManager.createQuery(FIND_STORE_QUERY).setParameter("count", count)
                .setParameter("name", productName).getResultList();
        if (stores.size() > 0) {
            return stores.get(0);
        }
        return null;
    }
}
