package com.epda.controllers.receptionist;

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
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/receptionist/appointments/view/*")
public class AppointmentViewController extends HttpServlet {

    @EJB
    private AppointmentFacade appointmentFacade;

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
        String searchQuery = request.getParameter("search");
        String encodedSearchQuery = "";

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
            // appointments = appointmentFacade.findAll(); // Or use pagination logic if implemented
            appointments = appointmentFacade.findAppointments(
                "updatedAt",
                "DESC"
            );
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

        // if there is a search query, don't paginate
        appointments = searchQuery != null && !searchQuery.isEmpty()
            ? appointments
            : appointments.subList(
                (currentPage - 1) * 10,
                Math.min(currentPage * 10, appointments.size())
            );

        Map<Long, String> formattedDates = new HashMap<>();
        appointments.forEach(appointment -> {
            formattedDates.put(
                appointment.getId(),
                appointment
                    .getAppointmentDate()
                    .format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
            );
        });
        request.setAttribute("formattedDates", formattedDates);
        request.setAttribute("encodedSearchQuery", encodedSearchQuery);

        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("appointments", appointments);
        request
            .getRequestDispatcher(
                "/WEB-INF/views/receptionist/appointment-table.jsp"
            )
            .forward(request, response);
    }
}
