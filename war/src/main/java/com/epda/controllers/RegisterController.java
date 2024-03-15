package com.epda.controllers;

import com.epda.facade.ManagingStaffFacade;
import com.epda.facade.ReceptionistFacade;
import com.epda.facade.VeterinarianFacade;
import com.epda.model.ManagingStaff;
import com.epda.model.Receptionist;
import com.epda.model.User;
import com.epda.model.Veterinarian;
import com.epda.model.enums.AccountStatus;
import com.epda.services.AuthService;
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

    @EJB
    private ManagingStaffFacade managingStaffFacade;

    @EJB
    private AuthService authService;

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException, ServletException {
        String pathInfo = request.getPathInfo();
        String path = (pathInfo != null && pathInfo.length() > 1)
            ? pathInfo.substring(1).toLowerCase()
            : "";
        // Check for the specific roles and forward to the correct page
        if (
            "receptionist".equals(path) ||
            "veterinarian".equals(path) ||
            "managing-staff".equals(path)
        ) {
            request
                .getRequestDispatcher("/register.jsp")
                .forward(request, response);
        } else {
            request
                .getRequestDispatcher("/register-role.jsp")
                .forward(request, response);
        }
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

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm-password");

        if (!password.equals(confirmPassword)) {
            request
                .getSession()
                .setAttribute("authError", "Passwords do not match");
            doGet(request, response);
            return;
        }

        if (
            name == null || email == null || phone == null || password == null
        ) {
            request
                .getSession()
                .setAttribute("authError", "All fields are required");
            doGet(request, response);
            return;
        }

        // if email exists
        if (authService.checkEmail(email)) {
            request
                .getSession()
                .setAttribute("authError", "Email already exists");
            doGet(request, response);
            return;
        }

        try {
            switch (role) {
                case "receptionist":
                    registerReceptionist(name, email, password, phone);
                    break;
                case "veterinarian":
                    registerVeterinarian(name, email, password, phone);
                    break;
                case "managing-staff":
                    registerManagingStaff(name, email, password, phone);
                    break;
                default:
                    request
                        .getSession()
                        .setAttribute("authError", "Unknown Role");
                    doGet(request, response);
                    return;
            }
        } catch (Exception e) {
            request
                .getSession()
                .setAttribute("authError", "Error registering user");
            doGet(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute(
            "registrationSuccess",
            "Successfully registered as " + role
        );
        request
            .getRequestDispatcher("/WEB-INF/views/register-success.jsp")
            .forward(request, response);
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
        receptionist.setStatus(AccountStatus.INACTIVE);
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
        veterinarian.setStatus(AccountStatus.INACTIVE);
        // Persist the veterinarian
        veterinarianFacade.create(veterinarian);
    }

    private void registerManagingStaff(
        String name,
        String email,
        String password,
        String phone
    ) {
        ManagingStaff managingStaff = new ManagingStaff();
        populateUserFields(managingStaff, name, email, password, phone);
        managingStaff.setStatus(AccountStatus.INACTIVE);
        managingStaffFacade.create(managingStaff);
    }
}
