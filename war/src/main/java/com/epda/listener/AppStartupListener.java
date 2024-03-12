package com.epda.listener;

import com.epda.services.DatabaseSeeder;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppStartupListener implements ServletContextListener {

    @EJB
    private DatabaseSeeder databaseSeeder;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        int numberOfCustomers = 50;
        int petsPerCustomer = 3;

        databaseSeeder.seedDatabase(numberOfCustomers, petsPerCustomer, 50, 30);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Handle context destruction if necessary
    }
}
