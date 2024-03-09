package com.epda.facade;

import com.epda.model.Veterinarian;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
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

    public List<Veterinarian> findByExpertise(String expertise) {
        TypedQuery<Veterinarian> query = em.createQuery(
            "SELECT v FROM Veterinarian v WHERE v.expertise = :expertise",
            Veterinarian.class
        );
        query.setParameter("expertise", expertise);
        return query.getResultList();
    }
}
