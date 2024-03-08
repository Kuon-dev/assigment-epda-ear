package com.epda.api;

import com.epda.facade.AppointmentFacade;
import com.epda.model.Appointment;
import jakarta.ejb.EJB;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/appointment/*")
public class AppointmentApi extends HttpServlet {

    @EJB
    private AppointmentFacade appointmentFacade;

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String searchQuery = request.getParameter("search");

        if (searchQuery != null && !searchQuery.isEmpty()) {
            // Search for appointments using the provided query parameter
            List<Appointment> appointments =
                appointmentFacade.searchAppointments(searchQuery);
            writeResponse(response, appointments);
        } else if (pathInfo == null || pathInfo.equals("/")) {
            // Fetch all appointments
            List<Appointment> appointments = appointmentFacade.findAll();
            writeResponse(response, appointments);
        } else {
            // Fetch a single appointment by ID
            String[] splits = pathInfo.split("/");
            if (splits.length == 2) {
                try {
                    Long id = Long.parseLong(splits[1]);
                    Appointment appointment = appointmentFacade.find(id);
                    if (appointment != null) {
                        writeResponse(response, appointment);
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        }
    }

    @Override
    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        Jsonb jsonb = JsonbBuilder.create();
        Appointment appointment = jsonb.fromJson(
            request.getReader(),
            Appointment.class
        );

        if (appointment != null) {
            appointmentFacade.create(appointment);
            response.setStatus(HttpServletResponse.SC_CREATED);
            writeResponse(response, appointment);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            String[] splits = pathInfo.split("/");
            if (splits.length == 2) {
                try {
                    Long id = Long.parseLong(splits[1]);
                    Appointment appointment = appointmentFacade.find(id);
                    if (appointment != null) {
                        appointmentFacade.remove(appointment);
                        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void writeResponse(HttpServletResponse response, Object object)
        throws IOException {
        Jsonb jsonb = JsonbBuilder.create();
        String result = jsonb.toJson(object);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(result);
    }
}
