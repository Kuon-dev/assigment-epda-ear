package com.epda.facade;

import com.epda.model.Appointment;
import com.epda.model.Customer;
import com.epda.model.Pet;
// import com.epda.model.AbstractFacade;
import com.epda.model.Veterinarian;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public List<Appointment> findAppointmentsForVeterinarianAndDateRange(
        Long vetId,
        LocalDate startDate,
        LocalDate endDate
    ) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Appointment> cq = cb.createQuery(Appointment.class);
        Root<Appointment> appointmentRoot = cq.from(Appointment.class);

        // Assuming an Appointment directly references a Veterinarian (adjust if through another entity)
        Join<Appointment, Veterinarian> vetJoin = appointmentRoot.join(
            "veterinarian"
        );
        // Convert LocalDate to LocalDateTime at the start and end of day
        LocalDateTime startDateTime = startDate.atStartOfDay(); // 00:00:00 of startDate
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX); // 23:59:59.999999999 of endDate
        // Constructing the date range and veterinarian ID criteria
        Predicate vetPredicate = cb.equal(vetJoin.get("id"), vetId);
        Predicate dateRangePredicate = cb.between(
            appointmentRoot.get("appointmentDate"),
            startDateTime,
            endDateTime
        );

        // Combine predicates with AND
        cq.where(cb.and(vetPredicate, dateRangePredicate));
        cq.select(appointmentRoot);

        return getEntityManager().createQuery(cq).getResultList();
    }
}
