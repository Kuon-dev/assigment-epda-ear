package com.epda.controllers;

import com.epda.facade.CustomerFacade;
import com.epda.model.Customer;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/create-customer")
public class CustomerController extends HttpServlet {

    @EJB
    private CustomerFacade customerFacade;

    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        // This could be used to show the form to create a new customer
        request
            .getRequestDispatcher("/create-customer-form.jsp")
            .forward(request, response);
    }

    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        try {
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            Customer customer = new Customer();
            customer.setName(name);
            customer.setEmail(email);

            customerFacade.create(customer);
            // Redirect to a confirmation page or back to the form with a success message
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
