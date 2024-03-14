package com.epda.facade;

import com.epda.model.Veterinarian;
import com.epda.model.enums.Expertise;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

@Stateless
public class VeterinarianFacade extends AbstractFacade<Veterinarian> {

    @PersistenceContext(unitName = "epda")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VeterinarianFacade() {
        super(Veterinarian.class);
    }

    public List<Veterinarian> findByExpertise(String expertiseString) {
        // Convert the string to the Expertise enum
        Expertise expertise;
        try {
            expertise = Expertise.valueOf(expertiseString.toUpperCase());
        } catch (IllegalArgumentException e) {
            // If the expertise string does not match any enum constants, return an empty list or handle as needed
            return Collections.emptyList();
        }

        // Note the change in the query string to accommodate a collection of expertises
        TypedQuery<Veterinarian> query = em.createQuery(
            "SELECT v FROM Veterinarian v JOIN v.expertises e WHERE e = :expertise",
            Veterinarian.class
        );
        query.setParameter("expertise", expertise); // Pass the enum here
        return query.getResultList();
    }

    public List<Object[]> findVeterinarianExpertiseDistribution() {
        String sql =
            "SELECT ve.expertise, COUNT(ve.veterinarian_id) FROM veterinarian_expertise ve GROUP BY ve.expertise ORDER BY COUNT(ve.veterinarian_id) DESC";
        List<Object[]> results = em.createNativeQuery(sql).getResultList();
        return results;
    }
}
