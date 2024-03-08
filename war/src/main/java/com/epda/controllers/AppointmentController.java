package com.epda.controllers;

import com.epda.facade.AppointmentFacade;
import com.epda.model.Appointment;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/appointments")
public class AppointmentController extends HttpServlet {

    @EJB
    private AppointmentFacade appointmentFacade;

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        String pageParam = request.getParameter("page");
        System.out.println(pageParam);
        int currentPage = 1;
        String searchQuery = request.getParameter("search");
        String encodedSearchQuery = "";

        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                currentPage = 1; // Default to page 1 if parsing fails
            }
        } else {
            currentPage = 1; // Default to page 1 if no page parameter is provided
        }

        int totalAppointments;
        List<Appointment> appointments;
        if (searchQuery != null && !searchQuery.isEmpty()) {
            // Perform search operation and get appointments based on the search query
            encodedSearchQuery = URLEncoder.encode(
                searchQuery,
                StandardCharsets.UTF_8.toString()
            );
            appointments = appointmentFacade.searchAppointments(searchQuery);
            totalAppointments = appointments.size();
        } else {
            // No search query provided, fetch all appointments or appointments for the current page
            appointments = appointmentFacade.findAll(); // Or use pagination logic if implemented
            totalAppointments = appointmentFacade.count();
        }
        int maxPagesToShow = 5;

        int totalPages = (int) Math.ceil((double) totalAppointments / 10);
        int halfPagesToShow = maxPagesToShow / 2;
        int startPage = Math.max(currentPage - halfPagesToShow, 1);

        int endPage = Math.min(startPage + maxPagesToShow - 1, totalPages);
        if (endPage - startPage < maxPagesToShow - 1) {
            startPage = Math.max(endPage - (maxPagesToShow - 1), 1);
        }

        System.out.println(currentPage);
        // if there is a search query, don't paginate
        // appointments = searchQuery != null && !searchQuery.isEmpty()
        //   ? appointments :
        //     appointments.subList((currentPage - 1) * 10, Math.min(currentPage * 10, appointments.size()));
        appointments = appointments.subList(
            (currentPage - 1) * 10,
            Math.min(currentPage * 10, appointments.size())
        );

        request.setAttribute("encodedSearchQuery", encodedSearchQuery);

        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("appointments", appointments);
        request
            .getRequestDispatcher("/appointments.jsp")
            .forward(request, response);
    }
}
