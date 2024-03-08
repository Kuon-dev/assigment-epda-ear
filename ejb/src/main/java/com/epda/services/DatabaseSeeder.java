package com.epda.services;

import com.epda.model.Customer;
import com.epda.model.Pet;
import com.epda.factory.CustomerFactory;
import com.epda.factory.PetFactory;
import com.epda.factory.ManagingStaffFactory;
import com.epda.factory.ReceptionistFactory;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class DatabaseSeeder {

    @PersistenceContext(unitName = "epda")
    private EntityManager em;

    public void seedDatabase(int numberOfCustomers, int petsPerCustomer) {
        for (int i = 0; i < numberOfCustomers; i++) {
            Customer customer = CustomerFactory.create();
            em.persist(customer);

            for (int j = 0; j < petsPerCustomer; j++) {
                Pet pet = PetFactory.create();
                pet.setCustomer(customer); // Assuming Pet class has a setCustomer method
                em.persist(pet);
            }
        }

        for (int i = 0; i < 10; i++) {
            em.persist(ManagingStaffFactory.createManagingStaff());
            em.persist(ReceptionistFactory.createReceptionist());
        }
    }
}
