package com.epda.controllers;

import com.epda.facade.ReceptionistFacade;
import com.epda.facade.VeterinarianFacade;
import com.epda.model.Receptionist;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/register/*")
public class RegisterController extends HttpServlet {

    @EJB
    private VeterinarianFacade veterinarianFacade;

    @EJB
    private ReceptionistFacade receptionistFacade;

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException, ServletException {
        request
            .getRequestDispatcher("/register.jsp")
            .forward(request, response);
    }

    @Override
    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException, ServletException {
        String pathInfo = request.getPathInfo();
        String role = pathInfo != null && pathInfo.length() > 1
            ? pathInfo.substring(1).toLowerCase()
            : "";
        List<String> errorMsgs = new ArrayList<>();

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm-password");

        switch (role) {
            case "receptionist":
                registerReceptionist(name, email, password, phone);
                break;
            case "veterinarian":
                registerVeterinarian(name, email, password, phone);
                break;
            default:
                errorMsgs.add("Unknown role");
                request.setAttribute("errorMsgs", errorMsgs);
                doGet(request, response);
                return;
        }

        HttpSession session = request.getSession();
        session.setAttribute(
            "registrationSuccess",
            "Successfully registered as " + role
        );
        response.sendRedirect(
            request.getContextPath() + "/registerSuccess.jsp"
        ); // Redirect to a success page
    }

    private void populateUserFields(
        User user,
        String name,
        String email,
        String password,
        String phone
    ) {
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);
    }

    private void registerReceptionist(
        String name,
        String email,
        String password,
        String phone
    ) {
        Receptionist receptionist = new Receptionist();
        populateUserFields(receptionist, name, email, password, phone);
        receptionistFacade.create(receptionist);
    }

    private void registerVeterinarian(
        String name,
        String email,
        String password,
        String phone
    ) {
        Veterinarian veterinarian = new Veterinarian();
        // Populate Veterinarian fields
        veterinarian.setName(name);
        veterinarian.setEmail(email);
        veterinarian.setPassword(password);
        veterinarian.setPhone(phone);
        // Persist the veterinarian
        veterinarianFacade.create(veterinarian);
    }
}
