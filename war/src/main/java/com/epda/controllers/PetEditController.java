package com.epda.controllers;

import com.epda.facade.PetFacade;
import com.epda.model.Pet;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/pets/edit/*")
public class PetEditController extends HttpServlet {

    @EJB
    private PetFacade petFacade;

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo != null) {
            try {
                // Extract the pet ID from the URL path
                String[] pathParts = pathInfo.split("/");
                if (pathParts.length >= 2) {
                    Long petId = Long.parseLong(pathParts[1]);
                    Pet pet = petFacade.find(petId);

                    if (pet != null) {
                        request.setAttribute("pet", pet);
                        request
                            .getRequestDispatcher("/WEB-INF/pet-edit.jsp")
                            .forward(request, response);
                    } else {
                        response.sendError(
                            HttpServletResponse.SC_NOT_FOUND,
                            "Pet not found"
                        );
                    }
                }
            } catch (NumberFormatException e) {
                response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid pet ID format"
                );
            }
        } else {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Pet ID is required"
            );
        }
    }

    @Override
    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        // Handle update
        if (request.getPathInfo().startsWith("/update")) {
            Long petId = Long.parseLong(request.getParameter("petId"));
            Pet pet = petFacade.find(petId);

            // Update pet attributes from request parameters
            pet.setName(request.getParameter("name"));
            // Update other pet attributes

            petFacade.edit(pet); // Assuming there's an update method in your facade

            response.sendRedirect(request.getContextPath() + "/pets");
        }
        // handle other POST requests
    }
}
