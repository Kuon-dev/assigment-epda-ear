package com.epda.facade;

import com.epda.model.Appointment;
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

    public List<Appointment> findAppointments(String sortBy, String order) {
        // Constructing the query string with dynamic sorting
        String queryString =
            "SELECT a FROM Appointment a ORDER BY a." + sortBy + " " + order;

        TypedQuery<Appointment> query = em.createQuery(
            queryString,
            Appointment.class
        );
        // query.setFirstResult((pageNumber - 1) * PAGE_SIZE);
        // query.setMaxResults(PAGE_SIZE);
        return query.getResultList();
    }

    // Inside AppointmentFacade
    public List<Appointment> searchAppointments(String customerName) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Appointment> cq = cb.createQuery(Appointment.class);
        Root<Appointment> appointmentRoot = cq.from(Appointment.class);
        Join<Appointment, Pet> petJoin = appointmentRoot.join("pet"); // Adjust "pet" to your actual attribute name
        Join<Pet, Customer> customerJoin = petJoin.join("customer"); // Adjust "customer" to your actual attribute name

        // Assuming the Customer entity has a name attribute
        cq.where(
            cb.like(
                cb.lower(customerJoin.get("name")),
                "%" + customerName.toLowerCase() + "%"
            )
        );
        cq.select(appointmentRoot);

        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<Object[]> findAppointmentStatusDistribution() {
        String sql =
            "SELECT a.status, COUNT(a.id) FROM Appointment a GROUP BY a.status ORDER BY COUNT(a.id) DESC";
        List<Object[]> results = em.createNativeQuery(sql).getResultList();
        return results;
    }

    public List<Object[]> findMonthlyAppointmentTrends() {
        String sql =
            "SELECT EXTRACT(YEAR FROM a.appointment_date) AS year, EXTRACT(MONTH FROM a.appointment_date) AS month, COUNT(a.id) " +
            "FROM Appointment a " +
            "GROUP BY EXTRACT(YEAR FROM a.appointment_date), EXTRACT(MONTH FROM a.appointment_date) " +
            "ORDER BY EXTRACT(YEAR FROM a.appointment_date), EXTRACT(MONTH FROM a.appointment_date)";
        List<Object[]> results = em.createNativeQuery(sql).getResultList();
        return results;
    }
}
