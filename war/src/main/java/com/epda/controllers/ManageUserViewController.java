package com.epda.controllers;

import com.epda.facade.ManagingStaffFacade;
import com.epda.facade.ReceptionistFacade;
import com.epda.facade.VeterinarianFacade;
import com.epda.model.ManagingStaff;
import com.epda.model.Receptionist;
import com.epda.model.User;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/managing-staff/users/view/*")
public class ManageUserViewController extends HttpServlet {

    @EJB
    private ReceptionistFacade receptionistFacade;

    @EJB
    private VeterinarianFacade veterinarianFacade;

    @EJB
    private ManagingStaffFacade managingStaffFacade;

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        int currentPage = 1;

        // Parsing currentPage from pathInfo
        if (pathInfo != null && !pathInfo.isEmpty()) {
            String[] splits = pathInfo.split("/");
            if (splits.length > 1) {
                try {
                    currentPage = Integer.parseInt(splits[1]);
                } catch (NumberFormatException e) {
                    // Handle incorrect format gracefully, perhaps redirect to a default page or log
                    currentPage = 1;
                }
            }
        }

        int pageSize = 10; // Number of records per page
        String searchQuery = request.getParameter("search");

        List<User> users = new ArrayList<>();
        users.addAll(receptionistFacade.findAll());
        users.addAll(managingStaffFacade.findAll());
        users.addAll(veterinarianFacade.findAll());

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            String searchLower = searchQuery.toLowerCase().trim();
            users = users
                .stream()
                .filter(u -> u.getEmail().toLowerCase().contains(searchLower))
                .collect(Collectors.toList());
        }

        int totalUsers = users.size();
        int totalPages = (int) Math.ceil((double) totalUsers / pageSize);
        int fromIndex = Math.max((currentPage - 1) * pageSize, 0);
        int toIndex = Math.min(fromIndex + pageSize, totalUsers);
        List<User> pageUsers = users.subList(fromIndex, toIndex);

        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("users", pageUsers);
        request.setAttribute("searchQuery", searchQuery);

        // Forward to JSP page
        request
            .getRequestDispatcher("/WEB-INF/managing-staff-table.jsp")
            .forward(request, response);
    }
}
