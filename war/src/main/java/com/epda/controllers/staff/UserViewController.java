package com.epda.controllers.staff;

import com.epda.facade.ManagingStaffFacade;
import com.epda.facade.ReceptionistFacade;
import com.epda.facade.VeterinarianFacade;
import com.epda.model.ManagingStaff;
import com.epda.model.Receptionist;
import com.epda.model.User;
import com.epda.model.Veterinarian;
import com.epda.model.enums.AccountStatus;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/managing-staff/users/view/*")
public class UserViewController extends HttpServlet {

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
        String pathInfo = request.getPathInfo();
        int currentPage = 1;

        // Parsing currentPage from pathInfo
        if (pathInfo != null && !pathInfo.isEmpty()) {
            String[] splits = pathInfo.split("/");
            if (splits.length > 1) {
                try {
                    currentPage = Integer.parseInt(splits[1]);
                } catch (NumberFormatException e) {
                    // Handle incorrect format gracefully, perhaps redirect to a default page or log
                    currentPage = 1;
                }
            }
        }

        int pageSize = 10; // Number of records per page
        String searchQuery = request.getParameter("search");

        List<User> users = new ArrayList<>();
        users.addAll(receptionistFacade.findAll());
        users.addAll(managingStaffFacade.findAll());
        users.addAll(veterinarianFacade.findAll());

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            String searchLower = searchQuery.toLowerCase().trim();
            users = users
                .stream()
                .filter(u -> u.getEmail().toLowerCase().contains(searchLower))
                .collect(Collectors.toList());
        }

        int totalUsers = users.size();
        int totalPages = (int) Math.ceil((double) totalUsers / pageSize);
        int fromIndex = Math.max((currentPage - 1) * pageSize, 0);
        int toIndex = Math.min(fromIndex + pageSize, totalUsers);
        List<User> pageUsers = users.subList(fromIndex, toIndex);

        Map<Long, String> userRoles = new HashMap<>();
        users.forEach(user -> {
            if (user instanceof ManagingStaff) {
                userRoles.put(user.getId(), "Managing Staff");
            } else if (user instanceof Receptionist) {
                userRoles.put(user.getId(), "Receptionist");
            } else if (user instanceof Veterinarian) {
                userRoles.put(user.getId(), "Veterinarian");
            }
        });

        request.setAttribute("userRoles", userRoles);

        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("users", pageUsers);
        request.setAttribute("searchQuery", searchQuery);
        // request.setAttribute("allStatuses", AccountStatus.values());
        // remove INACTIVE status from the list of statuses
        request.setAttribute(
            "allStatuses",
            Arrays.stream(AccountStatus.values())
                .filter(status -> status != AccountStatus.INACTIVE)
                .collect(Collectors.toList())
        ); // request.setAttribute("allStatuses", AccountStatus.values());

        // Forward to JSP page
        request
            .getRequestDispatcher(
                "/WEB-INF/views/managing-staff/user-table.jsp"
            )
            .forward(request, response);
    }

    @Override
    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        try {
            Long userId = Long.parseLong(request.getParameter("userId"));
            String statusStr = request.getParameter("status");
            AccountStatus status = AccountStatus.valueOf(
                statusStr.toUpperCase()
            );

            User user = findUserById(userId);
            if (user != null) {
                user.setStatus(status);
                // check if the user is a managing staff, receptionist or veterinarian, then update the user
                if (user instanceof ManagingStaff) {
                    managingStaffFacade.edit((ManagingStaff) user);
                } else if (user instanceof Receptionist) {
                    receptionistFacade.edit((Receptionist) user);
                } else if (user instanceof Veterinarian) {
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
