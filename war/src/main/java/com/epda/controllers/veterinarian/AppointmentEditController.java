package com.epda.controllers.veterinarian;

import com.epda.facade.AppointmentFacade;
import com.epda.facade.PetFacade;
import com.epda.facade.VeterinarianFacade;
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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@WebServlet("/veterinarian/appointments/edit/*")
public class AppointmentEditController extends HttpServlet {

    @EJB
    private AppointmentFacade appointmentFacade;

    @EJB
    private PetFacade petFacade;

    @EJB
    private VeterinarianFacade veterinarianFacade;

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        // Early return for null pathInfo
        if (pathInfo == null) {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Appointment ID is required"
            );
            return;
        }

        try {
            // Extract and validate the appointment ID
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length < 2) {
                response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid appointment ID format"
                );
                return;
            }

            Long appointmentId = Long.parseLong(pathParts[1]);
            Appointment appointment = appointmentFacade.find(appointmentId);

            // Check if the appointment exists
            if (appointment == null) {
                response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Appointment not found"
                );
                return;
            }

            // Setting attributes for the request
            request.setAttribute("appointment", appointment);

            Pet pet = appointment.getPet();
            Customer customer = pet.getCustomer();
            request.setAttribute("pet", pet);
            request.setAttribute("customer", customer);
            // parse appointment date as string in yyyy-MM-dd format
            String formattedDate = appointment
                .getAppointmentDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            request.setAttribute("appointmentDate", formattedDate);

            // Handling veterinarian information
            Veterinarian currentVet = appointment.getVeterinarian();
            List<Veterinarian> vetList = veterinarianFacade.findByExpertise(
                pet.getType()
            );
            request.setAttribute("veterinarians", vetList);
            // Optionally set the selected veterinarian ID
            if (currentVet != null) {
                request.setAttribute("selectedVetId", currentVet.getId());
                request.setAttribute("selectedVetName", currentVet.getName());
            }

            // Setting additional attributes
            List<TimeSlot> timeSlots = new ArrayList<>(
                Arrays.asList(TimeSlot.values())
            );
            timeSlots.remove(TimeSlot.ALL_DAY);
            request.setAttribute("timeSlots", timeSlots);
            request.setAttribute(
                "appointmentStatuses",
                AppointmentStatus.values()
            );

            request.setAttribute("diagnosis", appointment.getDiagnosis());
            request.setAttribute("prognosis", appointment.getPrognosis());
            // Forwarding to the JSP
            request
                .getRequestDispatcher(
                    "/WEB-INF/views/veterinarian/appointment-form.jsp"
                )
                .forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid appointment ID format"
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
            e.printStackTrace();
            System.err.println(e.getMessage());
            // Handle other errors
            response.sendError(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "An error occurred while updating the appointment"
            );
        }
    }
}
