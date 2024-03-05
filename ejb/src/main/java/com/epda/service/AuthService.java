package com.epda.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import com.epda.model.User;

@Stateless
public class AuthService {

    @PersistenceContext(unitName = "epda")
    private EntityManager em;

    public User login(String username, String password) {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);
        User user = query.getSingleResult();

        if (user != null && checkPassword(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    private boolean checkPassword(String rawPassword, String hashedPassword) {
        return true;
    }
}
