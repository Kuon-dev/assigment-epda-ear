package com.epda.controllers.veterinarian;

import com.epda.facade.AppointmentFacade;
import com.epda.facade.ScheduleFacade;
import com.epda.facade.VeterinarianFacade;
import com.epda.facade.WorkingRotaFacade;
import com.epda.model.Appointment;
import com.epda.model.Schedule;
import com.epda.model.User;
import com.epda.model.Veterinarian;
import com.epda.model.WorkingRota;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet("/veterinarian/weekly-rota")
public class ViewWorkingRotaController extends HttpServlet {

    @EJB
    private WorkingRotaFacade workingRotaFacade;

    @EJB
    private VeterinarianFacade veterinarianFacade;

    @EJB
    private ScheduleFacade scheduleFacade;

    @EJB
    private AppointmentFacade appointmentFacade;

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        Logger log = Logger.getLogger(getClass().getName());
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        List<Veterinarian> users = veterinarianFacade.findAll();
        if (user == null) {
            response.sendRedirect("/login");
            return;
        }

        for (Veterinarian u : users) {
            if (u.getId() == user.getId()) {
                user = u;
                break;
            }
        }
        request.setAttribute("user", user);
        final Long vetId = user.getId();

        if (vetId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        LocalDate now = LocalDate.now();
        List<WorkingRota> futureRotas =
            workingRotaFacade.findFutureRotasForVeterinarian(vetId, now);
        // sort rota by date
        futureRotas.sort(
            (r1, r2) -> r1.getStartDate().compareTo(r2.getStartDate())
        );
        request.setAttribute("futureRotas", futureRotas);

        // Handling week selection
        String selectedRotaIdStr = request.getParameter("selectedRotaId");
        // default selected rota to the first rota
        if (selectedRotaIdStr == null) {
            if (!futureRotas.isEmpty()) {
                selectedRotaIdStr = futureRotas.get(0).getId().toString();
            }
        }

        if (selectedRotaIdStr != null) {
            try {
                Long selectedRotaId = Long.parseLong(selectedRotaIdStr);
                WorkingRota selectedRota = workingRotaFacade.find(
                    selectedRotaId
                );

                // Assuming schedules are directly linked to working rota, no need to filter by date
                List<Schedule> schedulesForSelectedRota = selectedRota
                    .getSchedules()
                    .stream()
                    .filter(
                        schedule ->
                            schedule.getVeterinarian() != null &&
                            schedule.getVeterinarian().getId().equals(vetId)
                    )
                    // sort schedules by date
                    .sorted((s1, s2) -> s1.getDate().compareTo(s2.getDate()))
                    // remove duplicates
                    .distinct()
                    .collect(Collectors.toList());

                // format date to yyyy-MM-dd using hashmap
                Map<Long, String> formattedDate = new HashMap<>();
                for (Schedule schedule : schedulesForSelectedRota) {
                    formattedDate.put(
                        schedule.getId(),
                        schedule.getDate().toString()
                    );
                }

                // set day of week in a new hashmap
                Map<Long, String> dayOfWeek = new HashMap<>();
                for (Schedule schedule : schedulesForSelectedRota) {
                    dayOfWeek.put(
                        schedule.getId(),
                        schedule.getDate().getDayOfWeek().toString()
                    );
                }

                // log the schedules
                log.info(
                    "Schedules for selected rota: " + schedulesForSelectedRota
                );

                request.setAttribute(
                    "schedulesForSelectedRota",
                    schedulesForSelectedRota
                );
                request.setAttribute("selectedRota", selectedRota);
                request.setAttribute("formattedDate", formattedDate);
                request.setAttribute("dayOfWeek", dayOfWeek);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid rota selection.");
            }
        }

        request
            .getRequestDispatcher(
                "/WEB-INF/views/veterinarian/working-rota-table.jsp"
            )
            .forward(request, response);
    }
}
