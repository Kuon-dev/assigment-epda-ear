package com.epda.api;

import com.epda.config.ServletExceptionConfig;
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
            List<Appointment> appointments =
                appointmentFacade.searchAppointments(searchQuery);
            writeResponse(response, appointments);
        } else if (pathInfo == null || pathInfo.equals("/")) {
            List<Appointment> appointments = appointmentFacade.findAll();
            writeResponse(response, appointments);
        } else {
            String[] splits = pathInfo.split("/");
            if (splits.length == 2) {
                try {
                    Long id = Long.parseLong(splits[1]);
                    Appointment appointment = appointmentFacade.find(id);
                    if (appointment != null) {
                        writeResponse(response, appointment);
                    } else {
                        ServletExceptionConfig.sendError(
                            response,
                            HttpServletResponse.SC_NOT_FOUND,
                            "Appointment not found"
                        );
                    }
                } catch (NumberFormatException e) {
                    ServletExceptionConfig.sendError(
                        response,
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid appointment ID format"
                    );
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
                ServletExceptionConfig.sendError(
                    response,
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
            ServletExceptionConfig.sendError(
                response,
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid JSON format"
            );
        } catch (Exception ex) {
            ServletExceptionConfig.sendError(
                response,
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
                        ServletExceptionConfig.sendError(
                            response,
                            HttpServletResponse.SC_NOT_FOUND,
                            "Appointment not found"
                        );
                    }
                } catch (NumberFormatException e) {
                    ServletExceptionConfig.sendError(
                        response,
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid appointment ID"
                    );
                }
            }
        } else {
            ServletExceptionConfig.sendError(
                response,
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid request"
            );
        }
    }

    @Override
    protected void doPut(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        Jsonb jsonb = JsonbBuilder.create();
        String pathInfo = request.getPathInfo();

        if (
            pathInfo == null ||
            pathInfo.equals("/") ||
            pathInfo.split("/").length < 2
        ) {
            ServletExceptionConfig.sendError(
                response,
                HttpServletResponse.SC_BAD_REQUEST,
                "Missing appointment ID"
            );
            return;
        }

        Long appointmentId;
        try {
            appointmentId = Long.parseLong(pathInfo.split("/")[1]);
        } catch (NumberFormatException e) {
            ServletExceptionConfig.sendError(
                response,
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
            updateAppointment(appointmentId, appointmentDTO, response);
        } catch (JsonbException ex) {
            ServletExceptionConfig.sendError(
                response,
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid JSON format"
            );
        } catch (Exception ex) {
            ServletExceptionConfig.sendError(
                response,
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
            ServletExceptionConfig.sendError(
                response,
                HttpServletResponse.SC_NOT_FOUND,
                "Appointment not found"
            );
            return;
        }

        Pet pet = petFacade.find(appointmentDTO.getPetId());
        Veterinarian veterinarian = veterinarianFacade.find(
            appointmentDTO.getVeterinarianId()
        );
        if (pet == null || veterinarian == null) {
            ServletExceptionConfig.sendError(
                response,
                HttpServletResponse.SC_BAD_REQUEST,
                "Pet or Veterinarian not found"
            );
            return;
        }

        // Now, find the customer associated with the pet to ensure they exist
        Customer customer = (pet.getCustomer() != null)
            ? pet.getCustomer()
            : null;
        if (customer == null) {
            ServletExceptionConfig.sendError(
                response,
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

        appointmentFacade.edit(existingAppointment);
        writeResponse(response, AppointmentDTO.fromEntity(existingAppointment));
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
