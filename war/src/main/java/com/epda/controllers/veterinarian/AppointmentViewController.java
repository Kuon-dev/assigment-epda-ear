package com.epda.controllers.veterinarian;

import com.epda.facade.AppointmentFacade;
import com.epda.facade.WorkingRotaFacade;
import com.epda.model.Appointment;
import com.epda.model.User;
import com.epda.model.WorkingRota;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/veterinarian/appointments/view/*")
public class AppointmentViewController extends HttpServlet {

    @EJB
    private AppointmentFacade appointmentFacade;

    @EJB
    private WorkingRotaFacade workingRotaFacade;

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        Long rotaId = null;

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("/login");
            return;
        }

        Long vetId = user.getId();

        // Parsing currentPage from pathInfo
        if (pathInfo != null && !pathInfo.isEmpty()) {
            String[] splits = pathInfo.split("/");
            if (splits.length > 1) {
                try {
                    rotaId = Long.parseLong(splits[1]);
                } catch (NumberFormatException e) {
                    response.sendRedirect("/veterinarian/weekly-rota");
                }
            }
        }
        WorkingRota selectedRota = workingRotaFacade.find(rotaId);

        List<Appointment> appointmentList =
            appointmentFacade.findAppointmentsForVeterinarianAndDateRange(
                vetId,
                selectedRota.getStartDate(),
                selectedRota.getEndDate()
            );

        // get day of time from query param
        String dayOfWeek = request.getParameter("dayOfWeek").toLowerCase();
        // filter appointments by day, for example monday
        List<Appointment> appointmentsByDay = appointmentList
            .stream()
            .filter(
                appointment ->
                    appointment
                        .getAppointmentDate()
                        .getDayOfWeek()
                        .toString()
                        .toLowerCase()
                        .equals(dayOfWeek)
            )
            .collect(Collectors.toList());

        request.setAttribute("appointments", appointmentsByDay);
        request.setAttribute("dayOfWeek", dayOfWeek);
        request
            .getRequestDispatcher(
                "/WEB-INF/views/veterinarian/appointment-table.jsp"
            )
            .forward(request, response);
    }
}
