package com.epda.controllers;

import com.epda.facade.ReceptionistFacade;
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
import java.util.List;

@WebServlet("/receptionist/edit-profile")
public class ReceptionistController extends HttpServlet {

    @EJB
    private ReceptionistFacade receptionistFacade;

    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        // Assuming the user ID is available in the session or passed as a parameter
        User user = (User) session.getAttribute("user");
        List<Receptionist> users = receptionistFacade.findAll();
        if (user == null) {
            response.sendRedirect("/login");
            return;
        }

        for (Receptionist u : users) {
            if (u.getId() == user.getId()) {
                user = u;
                break;
            }
        }
        request.setAttribute("user", user);
        request
            .getRequestDispatcher("/WEB-INF/views/receptionist-dashboard.jsp")
            .forward(request, response);
    }
}
