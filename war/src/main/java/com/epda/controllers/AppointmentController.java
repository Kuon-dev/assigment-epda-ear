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
        int page = 1;
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        List<Appointment> appointments = appointmentFacade.findAppointments(
            page
        );
        int totalAppointments = appointmentFacade.count();
        int totalPages = (int) Math.ceil(
            (double) totalAppointments / AppointmentFacade.PAGE_SIZE
        );

        request.setAttribute("appointments", appointments);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request
            .getRequestDispatcher("/appointments.jsp")
            .forward(request, response);
    }
}
