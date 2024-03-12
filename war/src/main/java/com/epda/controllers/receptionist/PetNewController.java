package com.epda.controllers.receptionist;

import com.epda.facade.CustomerFacade;
import com.epda.facade.PetFacade;
import com.epda.model.Customer;
import com.epda.model.Pet;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/receptionist/pets/new/*")
public class PetNewController extends HttpServlet {

    @EJB
    private PetFacade petFacade;

    @EJB
    private CustomerFacade customerFacade;

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        String[] pathParts = request.getRequestURI().split("/");
        String customerIdString = pathParts[pathParts.length - 1];

        request.setAttribute("customerId", customerIdString);
        request
            .getRequestDispatcher("/WEB-INF/views/receptionist/pet-form.jsp")
            .forward(request, response);
    }

    @Override
    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        // get customer id from path
        String[] pathParts = request.getRequestURI().split("/");
        String customerIdString = pathParts[pathParts.length - 1];
        Long customerId = Long.parseLong(customerIdString);

        // get customer data
        Customer customer = customerFacade.find(customerId);

        if (customer == null) {
            request.setAttribute("errorMessage", "Customer not found.");
            request
                .getRequestDispatcher(
                    "/WEB-INF/views/receptionist/pet-form.jsp"
                )
                .forward(request, response);
            return;
        }

        String name = request.getParameter("name");
        String breed = request.getParameter("breed");
        String ageString = request.getParameter("age");
        String type = request.getParameter("type");

        try {
            int age = Integer.parseInt(ageString);
            Pet newPet = new Pet();
            newPet.setCustomer(customer);
            newPet.setName(name);
            newPet.setAge(age);
            newPet.setType(type);
            newPet.setBreed(breed);

            petFacade.create(newPet);
            // Set success message
            request.setAttribute("successMessage", "Pet created successfully.");
            request
                .getRequestDispatcher(
                    "/WEB-INF/views/receptionist/pet-form.jsp"
                )
                .forward(request, response);
        } catch (NumberFormatException e) {
            // Handling age parse failure
            request.setAttribute("errorMessage", "Invalid age provided.");
            request
                .getRequestDispatcher(
                    "/WEB-INF/views/receptionist/pet-form.jsp"
                )
                .forward(request, response);
        } catch (Exception e) {
            // General failure
            request.setAttribute(
                "errorMessage",
                "Failed to create pet: " + e.getMessage()
            );
            request
                .getRequestDispatcher(
                    "/WEB-INF/views/receptionist/pet-form.jsp"
                )
                .forward(request, response);
        }
    }
}
