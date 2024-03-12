package com.epda.api;

import com.epda.config.ServletExceptionConfig;
import com.epda.facade.ScheduleFacade;
import com.epda.facade.VeterinarianFacade;
import com.epda.model.Schedule;
import com.epda.model.dto.ScheduleDTO;
import com.epda.model.enums.TimeSlot;
import jakarta.ejb.EJB;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/api/schedule/*")
public class ScheduleApi extends HttpServlet {

    @EJB
    private ScheduleFacade scheduleFacade;

    @EJB
    private VeterinarianFacade veterinarianFacade;

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // Fetch all schedules
            List<Schedule> schedules = scheduleFacade.findAll();
            writeResponse(response, schedules);
        } else {
            // Fetch a single schedule
            String[] splits = pathInfo.split("/");
            if (splits.length == 2) {
                try {
                    Long id = Long.parseLong(splits[1]);
                    Schedule schedule = scheduleFacade.find(id);
                    if (schedule != null) {
                        writeResponse(response, schedule);
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
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

    @Override
    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        // StringBuilder sb = new StringBuilder();
        // String line;
        try (BufferedReader reader = request.getReader()) {
            ScheduleDTO scheduleDTO;
            Jsonb jsonb = JsonbBuilder.create();
            try {
                scheduleDTO = jsonb.fromJson(reader, ScheduleDTO.class);
            } catch (Exception e) {
                ServletExceptionConfig.sendError(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid request format."
                );
                e.printStackTrace();
                return;
            }

            LocalDate date;
            Long vetId;
            try {
                date = LocalDate.parse(scheduleDTO.getDate());
                vetId = Long.parseLong(scheduleDTO.getVet_id());
            } catch (Exception e) {
                ServletExceptionConfig.sendError(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid date or veterinarian ID."
                );
                return;
            }

            List<Schedule> existingSchedules =
                scheduleFacade.findScheduleByVetAndDate(vetId, date);
            Schedule schedule;
            if (existingSchedules.isEmpty()) {
                // Create a new schedule
                schedule = new Schedule();
                schedule.setDate(date);
                try {
                    schedule.setShift(TimeSlot.valueOf(scheduleDTO.getShift()));
                    schedule.setVeterinarian(veterinarianFacade.find(vetId));
                } catch (IllegalArgumentException e) {
                    ServletExceptionConfig.sendError(
                        response,
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid shift type or veterinarian ID not found."
                    );
                    return;
                }
                scheduleFacade.create(schedule);
            } else {
                // Update existing schedule
                schedule = existingSchedules.get(0); // Assuming only one schedule per vet per day
                try {
                    schedule.setShift(TimeSlot.valueOf(scheduleDTO.getShift()));
                } catch (IllegalArgumentException e) {
                    ServletExceptionConfig.sendError(
                        response,
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid shift type."
                    );
                    return;
                }
                scheduleFacade.edit(schedule);
            }

            ScheduleDTO responseDTO = new ScheduleDTO(schedule);
            writeResponse(response, responseDTO);
        } catch (IOException e) {
            ServletExceptionConfig.sendError(
                response,
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "An error occurred while processing the request."
            );
        }
    }
}
