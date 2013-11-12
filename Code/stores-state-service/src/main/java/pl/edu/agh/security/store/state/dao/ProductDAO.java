package pl.edu.agh.security.store.state.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import pl.edu.agh.security.store.state.objects.Product;

/**
 * 
 * @author Krzysztof
 * 
 */
@Stateless
public class ProductDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Product findByName(String name) {
        return (Product) this.entityManager
                .createQuery("select product from Product product where product.name = :name")
                .setParameter("name", name).getSingleResult();
    }
}
