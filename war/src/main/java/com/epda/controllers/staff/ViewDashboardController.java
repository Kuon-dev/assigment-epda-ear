package com.epda.controllers.staff;

import com.epda.facade.ManagingStaffFacade;
import com.epda.model.ManagingStaff;
import com.epda.model.User;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/managing-staff/edit-profile")
public class ViewDashboardController extends HttpServlet {

    @EJB
    private ManagingStaffFacade staffFacade;

    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        // Assuming the user ID is available in the session or passed as a parameter
        User user = (User) session.getAttribute("user");
        List<ManagingStaff> users = staffFacade.findAll();
        if (user == null) {
            response.sendRedirect("/login");
            return;
        }

        for (ManagingStaff u : users) {
            if (u.getId() == user.getId()) {
                user = u;
                break;
            }
        }
        request.setAttribute("user", user);
        request
            .getRequestDispatcher("/WEB-INF/views/managing-staff/dashboard.jsp")
            .forward(request, response);
    }
}
