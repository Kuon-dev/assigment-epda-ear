package com.epda.facade;

import com.epda.model.Customer;
// import com.epda.model.AbstractFacade;
// import com.epda.model.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class CustomerFacade extends AbstractFacade<Customer> {

    @PersistenceContext(unitName = "epda")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CustomerFacade() {
        super(Customer.class);
    }

    public List<Customer> findByEmail(String email) {
        TypedQuery<Customer> query = em.createQuery(
            "SELECT c FROM Customer c WHERE c.email = :email",
            Customer.class
        );
        query.setParameter("email", email);
        List<Customer> customers = query.getResultList();

        if (!customers.isEmpty()) {
            return customers; // Return the first customer found
        } else {
            return null; // No customer found with the given email
        }
    }
}
