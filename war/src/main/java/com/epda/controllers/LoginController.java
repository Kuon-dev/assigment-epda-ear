package com.epda.controllers;

import com.epda.model.ManagingStaff;
import com.epda.model.Receptionist;
import com.epda.model.User;
import com.epda.model.Veterinarian;
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

            // Redirect based on user role
            String redirectUrl = getRoleBasedRedirectUrl(user, request);
            response.sendRedirect(redirectUrl);
        } else {
            // Authentication failed, redirect to login with an error message
            response.sendRedirect("login.jsp?error=true"); // Adjust as needed
        }
    }

    private String getRoleBasedRedirectUrl(
        User user,
        HttpServletRequest request
    ) {
        HttpSession session = request.getSession();
        if (user instanceof Receptionist) {
            session.setAttribute("userRole", "receptionist");
            return "/receptionist/edit-profile"; // Adjust the path as needed
        } else if (user instanceof ManagingStaff) {
            session.setAttribute("userRole", "managing-staff");
            return "/managing-staff/edit-profile"; // Adjust the path as needed
        } else if (user instanceof Veterinarian) {
            session.setAttribute("userRole", "veterinarian");
            return "/veterinarian/edit-profile"; // Adjust the path as needed
        } else {
            return "error.jsp"; // Fallback or general dashboard path if the role is not determined
        }
    }
}
