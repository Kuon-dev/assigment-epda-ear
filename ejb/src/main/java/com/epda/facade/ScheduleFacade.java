package com.epda.facade;

import com.epda.model.Schedule;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

@Stateless
public class ScheduleFacade extends AbstractFacade<Schedule> {

    @PersistenceContext(unitName = "epda")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ScheduleFacade() {
        super(Schedule.class);
    }

    // Method to retrieve schedules within a specific week.
    public List<Schedule> findSchedulesForWeek(
        LocalDate startOfWeek,
        LocalDate endOfWeek
    ) {
        TypedQuery<Schedule> query = em.createQuery(
            "SELECT s FROM Schedule s WHERE s.date >= :startOfWeek AND s.date <= :endOfWeek",
            Schedule.class
        );
        query.setParameter("startOfWeek", startOfWeek);
        query.setParameter("endOfWeek", endOfWeek);
        return query.getResultList();
    }
}
