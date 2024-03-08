package com.epda.controllers;

import com.epda.model.User;
import com.epda.services.AuthService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// import jakarta.enterprise.context.RequestScoped;
// import jakarta.inject.Named;

@WebServlet(name = "LoginController", urlPatterns = { "/login" })
public class LoginController extends HttpServlet {

    @EJB
    private AuthService authService;

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        // This could be used to show the login form
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = authService.login(email, password);

        if (user != null) {
            // Authentication successful, create a session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            // Redirect or forward to a success page or the user's dashboard
            response.sendRedirect("dashboard.jsp"); // Adjust as needed
        } else {
            response.sendRedirect("login.jsp?error=true"); // Adjust as needed
        }
    }
}
