package com.epda.facade;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.epda.model.Veternarian;

@Stateless
public class VeternarianFacade extends AbstractFacade<Veternarian> {

    @PersistenceContext(unitName = "epda")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VeternarianFacade() {
        super(Veternarian.class);
    }
}
