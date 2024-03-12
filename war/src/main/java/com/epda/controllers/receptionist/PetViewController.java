package com.epda.controllers.receptionist;

import com.epda.config.ServletExceptionConfig;
import com.epda.facade.PetFacade;
import com.epda.model.Pet;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/receptionist/pets/view/*")
public class PetViewController extends HttpServlet {

    @EJB
    private PetFacade petFacade;

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        Long customerId = null;
        if (pathInfo != null && !pathInfo.isEmpty()) {
            String[] splits = pathInfo.split("/");
            if (splits.length > 1) {
                try {
                    customerId = Long.parseLong(splits[1]);
                } catch (NumberFormatException e) {
                    // customer not found
                    ServletExceptionConfig.sendError(
                        response,
                        HttpServletResponse.SC_NOT_FOUND,
                        "Customer not found"
                    );
                }
            }
        }

        if (customerId == null) {
            // customer not found
            ServletExceptionConfig.sendError(
                response,
                HttpServletResponse.SC_NOT_FOUND,
                "Customer not found"
            );
        }

        String searchQuery = request.getParameter("search");
        List<Pet> pets;

        if (searchQuery != null && !searchQuery.isEmpty()) {
            pets = petFacade.findByPetName(searchQuery);
        } else {
            pets = petFacade.findByCustomerId(customerId);
        }

        request.setAttribute("pets", pets);
        request.setAttribute("searchQuery", searchQuery);
        request.setAttribute("customerId", customerId);
        request
            .getRequestDispatcher("/WEB-INF/views/receptionist/pet-table.jsp")
            .forward(request, response);
    }
}
