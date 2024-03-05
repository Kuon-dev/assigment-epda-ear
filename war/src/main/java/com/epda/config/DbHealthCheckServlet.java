package com.epda.config; 

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.ejb.EJB;
import com.epda.model.UserFacade;

@WebServlet(name = "DbHealthCheckServlet", urlPatterns = { "/health-check" })
public class DbHealthCheckServlet extends HttpServlet {

    @EJB
    private UserFacade userFacade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String dbCheck = "Database Connection: Started";
      try {
          userFacade.findAll();
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
