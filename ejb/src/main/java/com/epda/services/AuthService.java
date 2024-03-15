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
import java.util.List;

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

    public boolean checkEmail(String email) {
        // Assuming Veterinarian, Receptionist, and ManagingStaff are separate entities without a shared superclass
        if (existsInEntity(email, Veterinarian.class)) return true;
        if (existsInEntity(email, Receptionist.class)) return true;
        return existsInEntity(email, ManagingStaff.class);
    }

    private <T> boolean existsInEntity(String email, Class<T> entityType) {
        String queryString =
            "SELECT u FROM " +
            entityType.getSimpleName() +
            " u WHERE u.email = :email";
        List<T> results = em
            .createQuery(queryString, entityType)
            .setParameter("email", email)
            .getResultList();
        return !results.isEmpty();
    }

    public <T extends User> boolean checkUserStatus(
        Long id,
        Class<T> entityType
    ) {
        return (isUserStatusActive(id, entityType));
    }

    public <T extends User> boolean checkUserPermission(
        Long id,
        Class<T> entityType,
        String path
    ) {
        // Determine if the path is allowed for the user's role
        if (
            entityType.equals(ManagingStaff.class) &&
            path.startsWith("/managing-staff/")
        ) {
            return true;
        } else if (
            entityType.equals(Receptionist.class) &&
            path.startsWith("/receptionist/")
        ) {
            return true;
        } else if (
            entityType.equals(Veterinarian.class) &&
            path.startsWith("/veterinarian/")
        ) {
            return true;
        }
        // Default to false if none of the conditions match
        return false;
    }

    private boolean isUserStatusActive(Long userId, Class<?> userType) {
        Object user = em.find(userType, userId);
        if (user != null && user instanceof User) {
            return "ACTIVE".equalsIgnoreCase(
                    ((User) user).getStatus().toString()
                );
        }
        return false;
    }
}
