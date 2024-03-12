package com.epda.controllers.receptionist;

import com.epda.facade.PetFacade;
import com.epda.model.Pet;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/receptionist/pets/edit/*")
public class PetEditController extends HttpServlet {

    @EJB
    private PetFacade petFacade;

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid pet ID"
            );
            return;
        }

        try {
            Long petId = Long.parseLong(pathInfo.substring(1));
            Pet pet = petFacade.find(petId);
            if (pet == null) {
                response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Pet not found"
                );
                return;
            }
            request.setAttribute("pet", pet);
            request
                .getRequestDispatcher(
                    "/WEB-INF/views/receptionist/pet-form.jsp"
                )
                .forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid pet ID format"
            );
        }
    }

    @Override
    protected void doPut(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        // This method assumes you have configured a way to support PUT requests with form data in your project.
        // Normally, HTML forms do not support PUT directly, so you might need to use JavaScript to send a PUT request or use a hidden input field to determine the action in your doPost method.
    }
}
