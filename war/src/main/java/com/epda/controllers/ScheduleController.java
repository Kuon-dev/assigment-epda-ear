package com.epda.controllers;

import com.epda.facade.ScheduleFacade;
import com.epda.model.Schedule;
import com.epda.model.Veterinarian;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

@WebServlet("/ScheduleController")
public class ScheduleController extends HttpServlet {

    @EJB
    private ScheduleFacade scheduleFacade;

    protected void processRequest(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        // Retrieve week adjustment parameter, default is current week
        int weekAdjust = parseOrDefault(request.getParameter("weekAdjust"), 0);
        // Calculate the start of the week
        LocalDate startOfWeek = LocalDate.now()
            .with(WeekFields.of(Locale.getDefault()).getFirstDayOfWeek())
            .plusWeeks(weekAdjust)
            .with(
                TemporalAdjusters.previousOrSame(
                    WeekFields.of(Locale.getDefault()).getFirstDayOfWeek()
                )
            );
        // Calculate the end of the week
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        List<Schedule> schedules = scheduleFacade.findAll(); // Fetch all or filtered schedules for the week

        request.setAttribute("schedules", schedules);
        request.setAttribute("startOfWeek", startOfWeek);
        request.setAttribute("endOfWeek", endOfWeek);
        request
            .getRequestDispatcher("/WEB-INF/test/schedule_table.jsp")
            .forward(request, response);
    }

    // Helper method to parse integers with default value
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
        // Handle form submission for schedule updates
        // You would have logic here to handle the updates to the schedules based on form inputs
    }
}
