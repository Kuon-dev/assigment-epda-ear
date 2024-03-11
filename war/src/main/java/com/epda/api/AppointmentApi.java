package com.epda.api;

import com.epda.facade.AppointmentFacade;
import com.epda.facade.CustomerFacade;
import com.epda.facade.PetFacade;
import com.epda.facade.VeterinarianFacade;
import com.epda.model.Appointment;
// import com.epda.model.Receptionist;
import com.epda.model.Customer;
import com.epda.model.Pet;
import com.epda.model.Veterinarian;
import com.epda.model.dto.AppointmentDTO;
import com.epda.model.enums.AppointmentStatus;
import com.epda.model.enums.TimeSlot;
import jakarta.ejb.EJB;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@WebServlet("/api/appointment/*")
public class AppointmentApi extends HttpServlet {

    @EJB
    private AppointmentFacade appointmentFacade;

    @EJB
    private PetFacade petFacade;

    @EJB
    private CustomerFacade customerFacade;

    @EJB
    private VeterinarianFacade veterinarianFacade;

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
        try (BufferedReader reader = request.getReader()) {
            AppointmentDTO appointmentDTO = jsonb.fromJson(
                reader,
                AppointmentDTO.class
            );

            Pet pet = petFacade.find(appointmentDTO.getPetId());
            Veterinarian veterinarian = veterinarianFacade.find(
                appointmentDTO.getVeterinarianId()
            );
            if (pet == null || veterinarian == null) {
                response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Pet or Veterinarian not found"
                );
                return;
            }

            LocalDate date = LocalDate.parse(
                appointmentDTO.getAppointmentDate()
            );
            LocalDateTime appointmentDateTime = LocalDateTime.of(
                date,
                LocalTime.MIDNIGHT
            );

            Appointment appointment = new Appointment();
            appointment.setPet(pet);
            appointment.setVeterinarian(veterinarian);
            appointment.setTimeSlot(
                TimeSlot.valueOf(appointmentDTO.getTimeSlot())
            );
            appointment.setStatus(
                AppointmentStatus.valueOf(appointmentDTO.getStatus())
            );
            appointment.setDiagnosis(appointmentDTO.getDiagnosis());
            appointment.setPrognosis(appointmentDTO.getPrognosis());
            appointment.setAppointmentDate(appointmentDateTime);

            appointmentFacade.create(appointment);

            response.setStatus(HttpServletResponse.SC_CREATED);
            writeResponse(response, AppointmentDTO.fromEntity(appointment));
        } catch (JsonbException ex) {
            ex.printStackTrace();
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid JSON format"
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendError(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "An error occurred while creating the appointment"
            );
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

    @Override
    protected void doPut(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        Jsonb jsonb = JsonbBuilder.create();
        String pathInfo = request.getPathInfo();

        // Check if the pathInfo is valid and extract the appointment ID.
        if (
            pathInfo == null ||
            pathInfo.equals("/") ||
            pathInfo.split("/").length < 2
        ) {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Missing appointment ID"
            );
            return;
        }

        Long appointmentId;
        try {
            appointmentId = Long.parseLong(pathInfo.split("/")[1]);
        } catch (NumberFormatException e) {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid appointment ID format"
            );
            return;
        }

        try (BufferedReader reader = request.getReader()) {
            AppointmentDTO appointmentDTO = jsonb.fromJson(
                reader,
                AppointmentDTO.class
            );
            // Now we're working with a DTO, so no need to check if IDs match.
            // We already have the appointment ID from the URL path.

            updateAppointment(appointmentId, appointmentDTO, response);
        } catch (JsonbException ex) {
            ex.printStackTrace();
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid JSON format"
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendError(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "An error occurred while updating the appointment"
            );
        }
    }

    private void updateAppointment(
        Long appointmentId,
        AppointmentDTO appointmentDTO,
        HttpServletResponse response
    ) throws IOException {
        Appointment existingAppointment = appointmentFacade.find(appointmentId);

        if (existingAppointment == null) {
            response.sendError(
                HttpServletResponse.SC_NOT_FOUND,
                "Appointment not found"
            );
            return;
        }

        // Find the pet record to ensure it exists
        Pet pet = petFacade.find(appointmentDTO.getPetId());
        Veterinarian veterinarian = veterinarianFacade.find(
            appointmentDTO.getVeterinarianId()
        );
        // Now, find the customer associated with the pet to ensure they exist
        Customer customer = (pet.getCustomer() != null)
            ? pet.getCustomer()
            : null;
        if (customer == null) {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Pet does not have an associated customer"
            );
            return;
        }

        LocalDate date = LocalDate.parse(appointmentDTO.getAppointmentDate());
        LocalDateTime appointmentDateTime = LocalDateTime.of(
            date,
            LocalTime.MIDNIGHT
        );
        existingAppointment.setAppointmentDate(appointmentDateTime);

        // Proceed to update the existing appointment with the new details
        existingAppointment.setPet(pet);
        existingAppointment.setVeterinarian(veterinarian);
        existingAppointment.setTimeSlot(
            TimeSlot.valueOf(appointmentDTO.getTimeSlot())
        );
        existingAppointment.setStatus(
            AppointmentStatus.valueOf(appointmentDTO.getStatus())
        );
        existingAppointment.setDiagnosis(appointmentDTO.getDiagnosis());
        existingAppointment.setPrognosis(appointmentDTO.getPrognosis());

        // Save the changes
        appointmentFacade.edit(existingAppointment);
        AppointmentDTO updatedDTO = AppointmentDTO.fromEntity(
            existingAppointment
        );
        writeResponse(response, updatedDTO);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void writeResponse(HttpServletResponse response, Object object)
        throws IOException {
        Jsonb jsonb = JsonbBuilder.create();
        String result = jsonb.toJson(object);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(result);
    }

    private void writeResponse(
        HttpServletResponse response,
        AppointmentDTO appointmentDTO
    ) throws IOException {
        Jsonb jsonb = JsonbBuilder.create();
        String result = jsonb.toJson(appointmentDTO);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(result);
    }
}
