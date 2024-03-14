package com.epda.facade;

import com.epda.model.WorkingRota;
// import com.epda.model.AbstractFacade;
// import com.epda.model.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

@Stateless
public class WorkingRotaFacade extends AbstractFacade<WorkingRota> {

    @PersistenceContext(unitName = "epda")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public WorkingRotaFacade() {
        super(WorkingRota.class);
    }

    public List<WorkingRota> findFutureRotasForVeterinarian(
        Long vetId,
        LocalDate from
    ) {
        TypedQuery<WorkingRota> query = em.createQuery(
            "SELECT DISTINCT wr FROM WorkingRota wr JOIN wr.schedules s WHERE s.veterinarian.id = :vetId AND wr.startDate >= :fromDate",
            WorkingRota.class
        );
        query.setParameter("vetId", vetId);
        query.setParameter("fromDate", from);
        return query.getResultList();
    }

    public List<WorkingRota> findFutureRotas() {
        TypedQuery<WorkingRota> query = em.createQuery(
            "SELECT wr FROM WorkingRota wr WHERE wr.startDate >= :today ORDER BY wr.startDate ASC",
            WorkingRota.class
        );
        query.setParameter("today", LocalDate.now());
        return query.getResultList();
    }
}
