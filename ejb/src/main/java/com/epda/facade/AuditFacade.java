package com.epda.facade;

import com.epda.model.AuditLog;
import com.epda.model.Customer;
import com.epda.model.Pet;
// import com.epda.model.AbstractFacade;
// import com.epda.model.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;

@Stateless
public class AuditFacade extends AbstractFacade<AuditLog> {

    @PersistenceContext(unitName = "epda")
    private EntityManager em;

    public static final int PAGE_SIZE = 10; // This can be adjusted or made dynamic

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AuditFacade() {
        super(AuditLog.class);
    }
}
