package com.epda.controllers.staff;

import com.epda.facade.AuditFacade;
import com.epda.model.AuditLog;
import com.epda.model.ManagingStaff;
import com.epda.model.Receptionist;
import com.epda.model.User;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/managing-staff/audit-log/view")
public class ViewAuditLog extends HttpServlet {

    @EJB
    private AuditFacade auditFacade;

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user instanceof ManagingStaff) {
            List<AuditLog> auditLogs = auditFacade.findAll();
            // new hashmap for formatted dates
            Map<Long, String> formattedDates = new HashMap<>();
            auditLogs.forEach(a -> {
                formattedDates.put(
                    a.getId(),
                    a
                        .getTimestamp()
                        .format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                );
            });

            request.setAttribute("auditLogs", auditLogs);
            request.setAttribute("formattedDates", formattedDates);
            request
                .getRequestDispatcher(
                    "/WEB-INF/views/managing-staff/audit-log.jsp"
                )
                .forward(request, response);
        } else if (user instanceof Receptionist) {
            request.setAttribute(
                "authError",
                "You do not have permission to view this page"
            );
            response.sendRedirect(
                "login.jsp?error=You do not have permission to view this page"
            );
        } else {
            request.setAttribute(
                "authError",
                "You do not have permission to view this page"
            );
            response.sendRedirect(
                "login.jsp?error=You do not have permission to view this page"
            );
        }
    }
}
