package pl.edu.agh.security.delivery.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import pl.edu.agh.security.delivery.entities.Delivery;

@Stateless
public class DeliveryDAO {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public Integer insertDelivery(Delivery delivery) {
        entityManager.persist(delivery);
        return delivery.getId();
    }
    
    public Delivery obtainDelivery(Integer id) {
        return entityManager.find(Delivery.class, id);
    }
}
