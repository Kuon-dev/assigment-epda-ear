package com.epda.controllers.receptionist;

import com.epda.facade.CustomerFacade;
import com.epda.model.Customer;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/receptionist/customers/new/*")
public class CustomerNewController extends HttpServlet {

    @EJB
    private CustomerFacade customerFacade;

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        request
            .getRequestDispatcher(
                "/WEB-INF/views/receptionist/customer-form.jsp"
            )
            .forward(request, response);
    }

    @Override
    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String ageString = request.getParameter("age");
        String phone = request.getParameter("phone");

        try {
            int age = Integer.parseInt(ageString);
            Customer newCustomer = new Customer();
            newCustomer.setName(name);
            newCustomer.setEmail(email);
            newCustomer.setAge(age);
            newCustomer.setPhone(phone);

            customerFacade.create(newCustomer);
            // Set success message
            request.setAttribute(
                "successMessage",
                "Customer created successfully."
            );
            request
                .getRequestDispatcher(
                    "/WEB-INF/views/receptionist/customer-form.jsp"
                )
                .forward(request, response);
        } catch (NumberFormatException e) {
            // Handling age parse failure
            request.setAttribute("errorMessage", "Invalid age provided.");
            request
                .getRequestDispatcher(
                    "/WEB-INF/views/receptionist/customer-form.jsp"
                )
                .forward(request, response);
        } catch (Exception e) {
            // General failure
            request.setAttribute(
                "errorMessage",
                "Failed to create customer: " + e.getMessage()
            );
            request
                .getRequestDispatcher(
                    "/WEB-INF/views/receptionist/customer-form.jsp"
                )
                .forward(request, response);
        }
    }
}
