package com.epda.controllers;

import com.epda.facade.ScheduleFacade;
import com.epda.facade.VeterinarianFacade;
import com.epda.model.Schedule;
import com.epda.model.Veterinarian;
import com.epda.model.enums.TimeSlot;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@WebServlet("/ScheduleController")
public class ScheduleController extends HttpServlet {

    @EJB
    private ScheduleFacade scheduleFacade;

    @EJB
    private VeterinarianFacade veterinarianFacade;

    protected void processRequest(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        int weekAdjust = parseOrDefault(request.getParameter("weekAdjust"), 0);

        LocalDate startOfWeek = LocalDate.now()
            .with(WeekFields.of(Locale.getDefault()).getFirstDayOfWeek())
            .plusWeeks(weekAdjust)
            .with(
                TemporalAdjusters.previousOrSame(
                    WeekFields.of(Locale.getDefault()).getFirstDayOfWeek()
                )
            );
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        // Convert LocalDate to java.util.Date
        Date startOfWeekDate = Date.from(
            startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant()
        );
        Date endOfWeekDate = Date.from(
            endOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant()
        );

        List<Veterinarian> veterinarians = veterinarianFacade.findAll();
        List<Schedule> schedules = scheduleFacade.findSchedulesForWeek(
            startOfWeek,
            endOfWeek
        ); // Your logic to fetch schedules
        List<Date> weekDates = new ArrayList<>();

        for (int i = 0; i <= 6; i++) {
            LocalDate date = startOfWeek.plusDays(i);
            Date dateUtil = Date.from(
                date.atStartOfDay(ZoneId.systemDefault()).toInstant()
            );
            weekDates.add(dateUtil);
        }

        List<LocalDate> weekLocalDates = weekDates
            .stream()
            .map(
                date ->
                    date
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
            )
            .collect(Collectors.toList());

        request.setAttribute("weekLocalDates", weekLocalDates);

        request.setAttribute("currentWeekAdjust", weekAdjust);
        request.setAttribute("shifts", TimeSlot.values());
        request.setAttribute("veterinarians", veterinarians);
        request.setAttribute("weekDates", weekDates);
        request.setAttribute("schedules", schedules);
        request.setAttribute("startOfWeek", startOfWeekDate);
        request.setAttribute("endOfWeek", endOfWeekDate);

        request
            .getRequestDispatcher("/WEB-INF/test/schedule_table.jsp")
            .forward(request, response);
    }

    private int parseOrDefault(String number, int defaultVal) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        processRequest(request, response);
    }
}
