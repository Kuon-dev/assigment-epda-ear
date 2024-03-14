package com.epda.controllers.veterinarian;

import com.epda.facade.VeterinarianFacade;
import com.epda.model.User;
import com.epda.model.Veterinarian;
import com.epda.model.enums.AccountStatus;
import com.epda.model.enums.Expertise;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/veterinarian/edit-profile/*")
public class ProfileUpdateController extends HttpServlet {

    @EJB
    private VeterinarianFacade veterinarianFacade;

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        List<Veterinarian> users = veterinarianFacade.findAll();
        for (Veterinarian u : users) {
            if (u.getId() == user.getId()) {
                user = u;
                break;
            }
        }

        if (user == null) {
            response.sendRedirect("/login");
            return;
        }
        request.setAttribute("user", user);
        request.setAttribute("allExpertise", Arrays.asList(Expertise.values()));

        request
            .getRequestDispatcher("/WEB-INF/views/veterinarian/user-form.jsp")
            .forward(request, response);
    }

    @Override
    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        Long userId = null;
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo != null && !pathInfo.isEmpty()) {
                String[] splits = pathInfo.split("/");
                if (splits.length > 1) {
                    try {
                        userId = Long.parseLong(splits[1]);
                    } catch (NumberFormatException e) {
                        // Handle incorrect format gracefully, perhaps redirect to a default page or log
                    }
                }
            }

            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password"); // Ensure you hash the password in real applications
            String phone = request.getParameter("phone");

            User user = veterinarianFacade.find(userId);
            if (user != null) {
                user.setName(name);
                user.setEmail(email);
                user.setPassword(password); // Remember to hash passwords in real applications
                user.setPhone(phone);
                // check if the user is a managing staff, receptionist or veterinarian, then update the user

                String[] expertiseValues = request.getParameterValues(
                    "expertise"
                );
                List<Expertise> selectedExpertise = new ArrayList<>();
                if (expertiseValues != null) {
                    for (String expertiseValue : expertiseValues) {
                        try {
                            selectedExpertise.add(
                                Expertise.valueOf(expertiseValue.toUpperCase())
                            );
                        } catch (IllegalArgumentException e) {
                            // Handle the case where an invalid expertise is submitted
                        }
                    }
                    // Assuming Veterinarian has a method to set multiple expertise areas
                    ((Veterinarian) user).setExpertises(selectedExpertise);
                    veterinarianFacade.edit((Veterinarian) user);
                }

                // Redirect to avoid double posting
                request
                    .getSession()
                    .setAttribute(
                        "successMessage",
                        "User status updated successfully."
                    );
            } else {
                request
                    .getSession()
                    .setAttribute("errorMessage", "User not found.");
            }
        } catch (IllegalArgumentException e) {
            request
                .getSession()
                .setAttribute("errorMessage", "Invalid user ID or status.");
        } finally {
            response.sendRedirect(
                request.getContextPath() + "/veterinarian/edit-profile"
            );
        }
    }
}
