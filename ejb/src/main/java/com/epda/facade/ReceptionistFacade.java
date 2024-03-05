package com.epda.facade;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.epda.model.Receptionist;

@Stateless
public class ReceptionistFacade extends AbstractFacade<Receptionist> {

    @PersistenceContext(unitName = "epda")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ReceptionistFacade() {
        super(Receptionist.class);
    }
}
