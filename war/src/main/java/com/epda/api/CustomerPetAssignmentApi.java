package com.epda.api;

import com.epda.facade.CustomerFacade;
import com.epda.facade.PetFacade;
import com.epda.model.Customer;
import com.epda.model.Pet;
import jakarta.ejb.EJB;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/api/customers/*/pets")
public class CustomerPetAssignmentApi extends HttpServlet {

    @EJB
    private CustomerFacade customerFacade;

    @EJB
    private PetFacade petFacade;

    @Override
    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        // Extract customerId from the URL
        String pathInfo = request.getPathInfo();
        Long customerId = extractCustomerId(pathInfo);

        // Deserialize the Pet from the request body
        Pet pet = deserializePetFromRequest(request);

        if (customerId != null && pet != null) {
            boolean isAssigned = assignPetToCustomer(customerId, pet);

            if (isAssigned) {
                response
                    .getWriter()
                    .println("Pet assigned to customer successfully.");
            } else {
                response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Failed to assign pet to customer."
                );
            }
        } else {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid customer ID or pet data."
            );
        }
    }

    private Long extractCustomerId(String pathInfo) {
        try {
            String[] parts = pathInfo.split("/");
            return Long.parseLong(parts[1]); // Assuming the first part is the customer ID
        } catch (Exception e) {
            return null;
        }
    }

    private Pet deserializePetFromRequest(HttpServletRequest request)
        throws IOException {
        Jsonb jsonb = JsonbBuilder.create();
        try (BufferedReader reader = request.getReader()) {
            return jsonb.fromJson(reader, Pet.class);
        } catch (Exception e) {
            // Log the error or handle it as appropriate
            return null;
        }
    }

    private boolean assignPetToCustomer(Long customerId, Pet pet) {
        Customer customer = customerFacade.find(customerId);
        if (customer != null) {
            pet.setCustomer(customer);
            petFacade.edit(pet); // Ensure this method persists the pet if it's new or merges changes if it's existing
            return true;
        } else {
            return false;
        }
    }
}
