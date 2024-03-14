package com.epda.services;

import com.epda.model.ManagingStaff;
import com.epda.model.Receptionist;
import com.epda.model.User;
import com.epda.model.Veterinarian;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Stateless
public class AuthService {

    @PersistenceContext(unitName = "epda")
    private EntityManager em;

    public User login(String email, String password) {
        User user = null;

        // Attempt to authenticate against ManagingStaff
        user = authenticate(email, password, ManagingStaff.class);
        if (user != null) return user;

        // Attempt to authenticate against Receptionist
        user = authenticate(email, password, Receptionist.class);
        if (user != null) return user;

        user = authenticate(email, password, Veterinarian.class);
        if (user != null) return user;

        return null; // Authentication failed
    }

    private <T extends User> T authenticate(
        String email,
        String password,
        Class<T> userClass
    ) {
        String queryString = String.format(
            "SELECT u FROM %s u WHERE u.email = :email",
            userClass.getSimpleName()
        );
        try {
            T user = em
                .createQuery(queryString, userClass)
                .setParameter("email", email)
                .getSingleResult();

            if (checkPassword(password, user.getPassword())) {
                return user;
            }
        } catch (NoResultException e) {
            // No user found with the given email (email) in this table
            e.printStackTrace();
        }
        return null; // Authentication failed or user not found in this role
    }

    private boolean checkPassword(String rawPassword, String storedPassword) {
        return rawPassword.equals(storedPassword);
    }
}
