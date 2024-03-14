package com.epda.controllers.veterinarian;

import com.epda.facade.VeterinarianFacade;
import com.epda.model.User;
import com.epda.model.Veterinarian;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/veterinarian/edit-profile")
public class ViewDashboardController extends HttpServlet {

    @EJB
    private VeterinarianFacade veterinarianFacade;

    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
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
        request
            .getRequestDispatcher("/WEB-INF/views/veterinarian/dashboard.jsp")
            .forward(request, response);
    }
}
