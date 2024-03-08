package com.epda.services;

import com.epda.factory.AppointmentFactory;
import com.epda.factory.CustomerFactory;
import com.epda.factory.ManagingStaffFactory;
import com.epda.factory.PetFactory;
import com.epda.factory.ReceptionistFactory;
import com.epda.factory.VeterinarianFactory;
import com.epda.model.Appointment;
import com.epda.model.Customer;
import com.epda.model.ManagingStaff;
import com.epda.model.Pet;
import com.epda.model.Receptionist;
import com.epda.model.Veterinarian;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Random;

@Stateless
public class DatabaseSeeder {

    @PersistenceContext(unitName = "epda")
    private EntityManager em;

    public void seedDatabase(
        int numberOfCustomers,
        int petsPerCustomer,
        int appointmentsPerPet
    ) {
        // Check if the database is empty before seeding
        if (isDatabaseEmpty()) {
            for (int i = 0; i < 10; i++) {
                ManagingStaff managingStaff =
                    ManagingStaffFactory.createManagingStaff();
                em.persist(managingStaff);

                Receptionist receptionist =
                    ReceptionistFactory.createReceptionist();
                em.persist(receptionist);

                Veterinarian veterinarian =
                    VeterinarianFactory.createVeterinarian(); // Assuming this factory exists
                em.persist(veterinarian);
            }

            for (int i = 0; i < numberOfCustomers; i++) {
                Customer customer = CustomerFactory.create();
                em.persist(customer);

                for (int j = 0; j < petsPerCustomer; j++) {
                    Pet pet = PetFactory.create();
                    pet.setCustomer(customer); // Assuming Pet class has a setCustomer method
                    em.persist(pet);

                    for (int k = 0; k < appointmentsPerPet; k++) {
                        Veterinarian veterinarian = getRandomVeterinarian(); // Implement this method
                        Appointment appointment = AppointmentFactory.create(
                            pet,
                            veterinarian
                        );
                        em.persist(appointment);
                    }
                }
            }
        }
    }

    private boolean isDatabaseEmpty() {
        // Check if any of the relevant tables are empty
        long customerCount = (long) em
            .createQuery("SELECT COUNT(c) FROM Customer c")
            .getSingleResult();
        long petCount = (long) em
            .createQuery("SELECT COUNT(p) FROM Pet p")
            .getSingleResult();
        long managingStaffCount = (long) em
            .createQuery("SELECT COUNT(m) FROM ManagingStaff m")
            .getSingleResult();
        long receptionistCount = (long) em
            .createQuery("SELECT COUNT(r) FROM Receptionist r")
            .getSingleResult();
        long veterinarianCount = (long) em
            .createQuery("SELECT COUNT(v) FROM Veterinarian v")
            .getSingleResult();
        long appointmentCount = (long) em
            .createQuery("SELECT COUNT(a) FROM Appointment a")
            .getSingleResult();

        return (
            customerCount == 0 &&
            petCount == 0 &&
            managingStaffCount == 0 &&
            receptionistCount == 0 &&
            veterinarianCount == 0 &&
            appointmentCount == 0
        );
    }

    private Veterinarian getRandomVeterinarian() {
        List<Veterinarian> veterinarians = em
            .createQuery("SELECT v FROM Veterinarian v", Veterinarian.class)
            .getResultList();
        if (veterinarians.isEmpty()) {
            return null; // or handle this case as appropriate, e.g., by creating a new Veterinarian
        }
        return veterinarians.get(new Random().nextInt(veterinarians.size()));
    }
}
