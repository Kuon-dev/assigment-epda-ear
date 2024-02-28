package com.epda.config; 

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "DbHealthCheckServlet", urlPatterns = { "/health-check" })
public class DbHealthCheckServlet extends HttpServlet {

    @PersistenceContext(unitName = "epda")
    private EntityManager entityManager;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String dbCheck = "Database Connection: Started";
      try {
          entityManager.createNativeQuery("SELECT 1").getResultList();
          dbCheck = "Database Connection: Finished";
          request.setAttribute("dbStatus", "Database Connection: Successful");
      } catch (Exception e) {
          request.setAttribute("dbStatus", "Database Connection: Failed - " + e.getMessage());
      }

      try {
          Class.forName("org.postgresql.Driver");
          System.out.println("PostgreSQL JDBC Driver is available.");
      } catch (ClassNotFoundException e) {
          System.out.println("PostgreSQL JDBC Driver is missing in classpath!");
          e.printStackTrace();
      }
      request.setAttribute("dbCheck", dbCheck);
      RequestDispatcher dispatcher = request.getRequestDispatcher("health-check.jsp");
      dispatcher.forward(request, response);
    }
}
