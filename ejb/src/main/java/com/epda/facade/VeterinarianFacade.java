package com.epda.facade;

import com.epda.model.Veterinarian;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

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
}
