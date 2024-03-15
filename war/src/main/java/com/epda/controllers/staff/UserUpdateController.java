package com.epda.controllers.staff;

import com.epda.facade.ManagingStaffFacade;
import com.epda.facade.ReceptionistFacade;
import com.epda.facade.VeterinarianFacade;
import com.epda.model.ManagingStaff;
import com.epda.model.Receptionist;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet("/managing-staff/users/edit/*")
public class UserUpdateController extends HttpServlet {

    @EJB
    private ReceptionistFacade receptionistFacade;

    @EJB
    private VeterinarianFacade veterinarianFacade;

    @EJB
    private ManagingStaffFacade managingStaffFacade;

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        Long userId = null;
        String pathInfo = request.getPathInfo();

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

        if (userId != null) {
            User user = findUserById(userId);
            if (user != null) {
                request.setAttribute("user", user);
            } else {
                request.setAttribute("errorMessage", "User not found.");
            }
        } else {
            request.setAttribute("errorMessage", "Invalid user ID.");
        }

        request.setAttribute(
            "allStatuses",
            Arrays.stream(AccountStatus.values())
                .filter(status -> status != AccountStatus.INACTIVE)
                .collect(Collectors.toList())
        );

        request.setAttribute("allExpertise", Arrays.asList(Expertise.values()));

        request
            .getRequestDispatcher("/WEB-INF/views/managing-staff/user-form.jsp")
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

            String statusStr = request.getParameter("status");
            AccountStatus status = AccountStatus.valueOf(
                statusStr.toUpperCase()
            );
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password"); // Ensure you hash the password in real applications
            String phone = request.getParameter("phone");

            User user = findUserById(userId);
            if (user != null) {
                user.setName(name);
                user.setEmail(email);
                user.setPassword(password); // Remember to hash passwords in real applications
                user.setPhone(phone);
                user.setStatus(status);
                // check if the user is a managing staff, receptionist or veterinarian, then update the user
                if (user instanceof ManagingStaff) {
                    managingStaffFacade.edit((ManagingStaff) user);
                } else if (user instanceof Receptionist) {
                    receptionistFacade.edit((Receptionist) user);
                } else if (user instanceof Veterinarian) {
                    String[] expertiseValues = request.getParameterValues(
                        "expertise"
                    );
                    List<Expertise> selectedExpertise = new ArrayList<>();
                    if (expertiseValues != null) {
                        for (String expertiseValue : expertiseValues) {
                            try {
                                selectedExpertise.add(
                                    Expertise.valueOf(
                                        expertiseValue.toUpperCase()
                                    )
                                );
                            } catch (IllegalArgumentException e) {
                                // Handle the case where an invalid expertise is submitted
                            }
                        }
                        // Assuming Veterinarian has a method to set multiple expertise areas
                        ((Veterinarian) user).setExpertises(selectedExpertise);
                        veterinarianFacade.edit((Veterinarian) user);
                    }

                    veterinarianFacade.edit((Veterinarian) user);
                }

                // Redirect to avoid double posting
                request
                    .getSession()
                    .setAttribute(
                        "operationMessage",
                        "User status updated successfully."
                    );
            } else {
                request
                    .getSession()
                    .setAttribute("operationMessage", "User not found.");
            }
        } catch (IllegalArgumentException e) {
            request
                .getSession()
                .setAttribute("operationMessage", "Invalid user ID or status.");
        } finally {
            response.sendRedirect(
                request.getContextPath() + "/managing-staff/users/view"
            );
        }
    }

    private User findUserById(Long userId) {
        User user = managingStaffFacade.find(userId);
        if (user == null) {
            user = receptionistFacade.find(userId);
        }
        if (user == null) {
            user = veterinarianFacade.find(userId);
        }
        return user;
    }
}
