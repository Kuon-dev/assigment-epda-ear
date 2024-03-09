package com.epda.facade;

import com.epda.model.Pet;
// import com.epda.model.AbstractFacade;
// import com.epda.model.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class PetFacade extends AbstractFacade<Pet> {

    @PersistenceContext(unitName = "epda")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PetFacade() {
        super(Pet.class);
    }

    public List<Pet> findByCustomerId(Long customerId) {
        return em
            .createQuery(
                "SELECT p FROM Pet p WHERE p.customer.id = :customerId",
                Pet.class
            )
            .setParameter("customerId", customerId)
            .getResultList();
    }

    public List<Pet> findByCustomerName(String customerName) {
        TypedQuery<Pet> query = em.createQuery(
            "SELECT p FROM Pet p WHERE LOWER(p.customer.name) LIKE LOWER(:customerName)",
            Pet.class
        );
        query.setParameter("customerName", "%" + customerName + "%");
        return query.getResultList();
    }
}
