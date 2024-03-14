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
        // search customer by email
        TypedQuery<Customer> query = em.createQuery(
            "SELECT c FROM Customer c WHERE c.email LIKE :email",
            Customer.class
        );
        query.setParameter("email", email + "%");
        List<Customer> customers = query.getResultList();

        if (!customers.isEmpty()) {
            return customers; // Return the first customer found
        } else {
            return null; // No customer found with the given email
        }
    }

    public List<Object[]> findCustomerAgeDistribution() {
        // Example SQL, adjust based on actual age groups you want to use
        String sql =
            "SELECT CASE " +
            "WHEN age BETWEEN 0 AND 18 THEN '0-18' " +
            "WHEN age BETWEEN 19 AND 30 THEN '19-30' " +
            "WHEN age BETWEEN 31 AND 45 THEN '31-45' " +
            "WHEN age > 45 THEN '46+' " +
            "END AS ageGroup, COUNT(id) " +
            "FROM Customer " +
            "GROUP BY ageGroup " +
            "ORDER BY ageGroup";
        List<Object[]> results = em.createNativeQuery(sql).getResultList();
        return results;
    }
}
