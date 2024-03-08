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
        int currentPage;
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                currentPage = 1; // Default to page 1 if parsing fails
            }
        } else {
            currentPage = 1; // Default to page 1 if no page parameter is provided
        }

        List<Appointment> appointments = appointmentFacade.findAppointments(
            currentPage
        );
        int totalAppointments = appointmentFacade.count();
        int maxPagesToShow = 5;
        int totalPages = (int) Math.ceil((double) totalAppointments / 10);
        int halfPagesToShow = maxPagesToShow / 2;

        int startPage = Math.max(currentPage - halfPagesToShow, 1);
        int endPage = Math.min(startPage + maxPagesToShow - 1, totalPages);

        if (endPage - startPage < maxPagesToShow - 1) {
            startPage = Math.max(endPage - (maxPagesToShow - 1), 1);
        }

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
