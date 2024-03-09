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
import java.util.List;

@WebServlet("/pets/view/*")
public class PetViewController extends HttpServlet {

    @EJB
    private PetFacade petFacade;

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        String customerName = request.getParameter("customer");
        List<Pet> pets;

        if (customerName != null && !customerName.isEmpty()) {
            pets = petFacade.findByCustomerName(customerName);
        } else {
            pets = petFacade.findAll();
        }

        request.setAttribute("pets", pets);
        request
            .getRequestDispatcher("/WEB-INF/pets-table.jsp")
            .forward(request, response);
    }
}
