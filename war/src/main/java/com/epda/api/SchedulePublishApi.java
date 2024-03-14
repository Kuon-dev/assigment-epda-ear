package com.epda.api;

import com.epda.config.ServletExceptionConfig;
import com.epda.facade.ScheduleFacade;
import com.epda.facade.VeterinarianFacade;
import com.epda.facade.WorkingRotaFacade;
import com.epda.model.Schedule;
import com.epda.model.Veterinarian;
import com.epda.model.WorkingRota;
import com.epda.model.dto.WorkingRotaDTO;
import com.epda.model.enums.Expertise;
import com.epda.model.enums.TimeSlot;
import jakarta.ejb.EJB;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet("/api/managing-staff/schedules/publish")
public class SchedulePublishApi extends HttpServlet {

    @EJB
    private ScheduleFacade scheduleFacade;

    @EJB
    private VeterinarianFacade veterinarianFacade;

    @EJB
    private WorkingRotaFacade workingRotaFacade;

    @Override
    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        try (BufferedReader reader = request.getReader()) {
            Jsonb jsonb = JsonbBuilder.create();
            WorkingRotaDTO weekScheduleDTO;
            try {
                weekScheduleDTO = jsonb.fromJson(reader, WorkingRotaDTO.class);
            } catch (Exception e) {
                ServletExceptionConfig.sendError(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid request format."
                );
                return;
            }

            LocalDate startOfWeek = LocalDate.parse(
                weekScheduleDTO.getStartOfWeek()
            );
            LocalDate endOfWeek = LocalDate.parse(
                weekScheduleDTO.getEndOfWeek()
            );

            // Validate and publish schedules
            String publishResult = publishSchedules(startOfWeek, endOfWeek);

            if (publishResult.equals("valid")) {
                // set response status to 200 OK
                response.setStatus(HttpServletResponse.SC_OK);
                writeResponse(
                    response,
                    200,
                    "Schedules successfully published for the week."
                );
                // response.getWriter().write("Schedules successfully published for the week.");
            } else {
                ServletExceptionConfig.sendError(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    publishResult
                );
            }
        } catch (IOException e) {
            ServletExceptionConfig.sendError(
                response,
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "An error occurred while processing the request."
            );
        }
    }

    private String publishSchedules(
        LocalDate startOfWeek,
        LocalDate endOfWeek
    ) {
        List<Schedule> weekSchedules = scheduleFacade.findSchedulesForWeek(
            startOfWeek,
            endOfWeek
        );
        Map<LocalDate, List<Schedule>> schedulesByDate = weekSchedules
            .stream()
            .collect(Collectors.groupingBy(Schedule::getDate));

        for (LocalDate date : schedulesByDate.keySet()) {
            List<Schedule> dailySchedules = schedulesByDate.get(date);
            String validationResult = isValidDaySchedule(dailySchedules, date);
            if (!validationResult.equals("valid")) {
                return validationResult; // Returns the specific validation error
            }
        }

        WorkingRota workingRota = new WorkingRota();
        workingRota.setStartDate(startOfWeek);
        workingRota.setEndDate(endOfWeek);
        workingRota.setSchedules(weekSchedules);

        workingRotaFacade.create(workingRota);

        // Implement logic to mark schedules as published if needed

        return "valid"; // Success message
    }

    private String isValidDaySchedule(
        List<Schedule> dailySchedules,
        LocalDate date
    ) {
        long distinctVeterinarians = dailySchedules
            .stream()
            .map(Schedule::getVeterinarian)
            .distinct()
            .count();
        Set<Expertise> coveredExpertise = dailySchedules
            .stream()
            .flatMap(
                schedule -> schedule.getVeterinarian().getExpertises().stream()
            )
            .collect(Collectors.toSet());

        if (distinctVeterinarians < 3) {
            return String.format(
                "Failed to publish schedules for %s: Less than 3 distinct veterinarians are working.",
                date
            );
        }
        if (coveredExpertise.size() < 5) {
            return String.format(
                "Failed to publish schedules for %s: Less than 5 distinct expertise areas covered.",
                date
            );
        }

        return "valid"; // Indicates validation passed
    }

    private void writeResponse(
        HttpServletResponse response,
        int statusCode,
        String message
    ) throws IOException {
        // Jsonb jsonb = JsonbBuilder.create();
        // String result = jsonb.toJson(object);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // response.getWriter().write(result);
        String jsonResponse = String.format(
            "{\"status\":%d, \"message\":\"%s\"}",
            statusCode,
            message
        );
        response.getWriter().write(jsonResponse);
    }
}
