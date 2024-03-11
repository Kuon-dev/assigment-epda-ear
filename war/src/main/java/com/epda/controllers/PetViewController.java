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
        int currentPage = 1;
        if (pathInfo != null && !pathInfo.isEmpty()) {
            String[] splits = pathInfo.split("/");
            if (splits.length > 1) {
                try {
                    currentPage = Integer.parseInt(splits[1]);
                } catch (NumberFormatException e) {
                    currentPage = 1;
                }
            }
        }

        String searchQuery = request.getParameter("search");
        List<Pet> pets;
        int totalPets;

        if (searchQuery != null && !searchQuery.isEmpty()) {
            pets = petFacade.findByPetName(searchQuery);
            totalPets = pets.size();
        } else {
            pets = petFacade.findAll();
            totalPets = petFacade.count();
        }

        int totalPages = (int) Math.ceil((double) totalPets / 10);
        int maxPagesToShow = 5;
        int halfPagesToShow = maxPagesToShow / 2;
        int startPage = Math.max(currentPage - halfPagesToShow, 1);
        int endPage = Math.min(startPage + maxPagesToShow - 1, totalPages);
        if (endPage - startPage < maxPagesToShow - 1) {
            startPage = Math.max(endPage - (maxPagesToShow - 1), 1);
        }

        request.setAttribute("pets", pets);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);
        request.setAttribute("searchQuery", searchQuery);
        request
            .getRequestDispatcher("/WEB-INF/views/receptionist/pet-table.jsp")
            .forward(request, response);
    }
}
