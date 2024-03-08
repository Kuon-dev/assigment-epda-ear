package com.epda.facade;

import com.epda.model.Appointment;
// import com.epda.model.AbstractFacade;
// import com.epda.model.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class AppointmentFacade extends AbstractFacade<Appointment> {

    @PersistenceContext(unitName = "epda")
    private EntityManager em;

    public static final int PAGE_SIZE = 10; // This can be adjusted or made dynamic

    public List<Appointment> findAppointments(int pageNumber) {
        TypedQuery<Appointment> query = em.createQuery(
            "SELECT a FROM Appointment a ORDER BY a.appointmentDate DESC",
            Appointment.class
        );
        query.setFirstResult((pageNumber - 1) * PAGE_SIZE);
        query.setMaxResults(PAGE_SIZE);
        return query.getResultList();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AppointmentFacade() {
        super(Appointment.class);
    }
}
