package com.epda.controllers;

import com.epda.facade.AppointmentFacade;
import com.epda.facade.PetFacade;
import com.epda.model.Appointment;
import com.epda.model.Customer;
import com.epda.model.Pet;
import com.epda.model.Veterinarian;
import com.epda.model.enums.AppointmentStatus;
import com.epda.model.enums.TimeSlot;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/appointments/edit/*")
public class AppointmentEditController extends HttpServlet {

    @EJB
    private AppointmentFacade appointmentFacade;

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
                // Extract the appointment ID from the URL path
                String[] pathParts = pathInfo.split("/");
                if (pathParts.length >= 2) {
                    Long appointmentId = Long.parseLong(pathParts[1]);
                    Appointment appointment = appointmentFacade.find(
                        appointmentId
                    );

                    if (appointment != null) {
                        request.setAttribute("appointment", appointment);
                        Long petId = appointment.getPet().getId();
                        Pet pet = petFacade.find(petId);
                        Customer customer = pet.getCustomer();
                        Veterinarian veterinarian =
                            appointment.getVeterinarian();
                        request.setAttribute("customer", customer);
                        request.setAttribute("pet", pet);
                        request
                            .getRequestDispatcher(
                                "/WEB-INF/appointment-form.jsp"
                            )
                            .forward(request, response);
                    } else {
                        response.sendError(
                            HttpServletResponse.SC_NOT_FOUND,
                            "Appointment not found"
                        );
                    }
                }
            } catch (NumberFormatException e) {
                response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid appointment ID format"
                );
            }
        } else {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Appointment ID is required"
            );
        }
    }

    @Override
    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        try {
            // Extract information from the request
            Long appointmentId = Long.parseLong(
                request.getParameter("appointmentId")
            );
            LocalDateTime appointmentDate = LocalDateTime.parse(
                request.getParameter("appointmentDate")
            );
            // Add more fields as needed

            // Find the existing appointment
            Appointment appointment = appointmentFacade.find(appointmentId);
            if (appointment != null) {
                // Update the appointment with new values
                appointment.setAppointmentDate(appointmentDate);
                // Continue updating other fields as necessary

                appointmentFacade.edit(appointment); // Assuming you have an update method in your facade

                // Redirect or forward after updating
                response.sendRedirect(
                    request.getContextPath() + "/appointments"
                ); // Redirect to the appointments list or another appropriate page
            } else {
                // Handle case where appointment is not found
                response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Appointment not found"
                );
            }
        } catch (NumberFormatException e) {
            // Handle invalid format errors
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid format for appointment ID or date"
            );
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace(); // Consider logging this error
            response.sendError(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "An error occurred while updating the appointment"
            );
        }
    }
}
