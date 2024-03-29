package com.epda.middleware;

import com.epda.model.User;
import com.epda.services.AuthService;
import jakarta.ejb.EJB;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*") // Apply this filter to all requests
public class RouterGuard implements Filter {

    @EJB
    private AuthService authService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Filter initialization can be placed here
    }

    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        String requestURI = httpRequest.getRequestURI();

        // Check if the request URI starts with any of the protected prefixes
        if (
            requestURI.startsWith(
                httpRequest.getContextPath() + "/receptionist/"
            ) ||
            requestURI.startsWith(
                httpRequest.getContextPath() + "/managing-staff/"
            ) ||
            requestURI.startsWith(
                httpRequest.getContextPath() + "/veterinarian/"
            ) ||
            requestURI.startsWith(httpRequest.getContextPath() + "/dashboard/")
        ) {
            // If there's no "user" attribute in session, redirect to login page
            if (session == null || session.getAttribute("user") == null) {
                httpResponse.sendRedirect(
                    httpRequest.getContextPath() + "/login"
                );
                return; // Important to return here to prevent further filter processing
            } else {
                // Assuming the user object is stored and has a getId() method
                User user = (User) session.getAttribute("user");
                Long userId = user.getId();
                Class<? extends User> userType = user.getClass();

                // Implement AccessControlService or similar to use checkUserStatusAndPermission
                boolean isActive = authService.checkUserStatus(
                    userId,
                    userType
                );
                boolean hasAccess = authService.checkUserPermission(
                    userId,
                    userType,
                    requestURI
                );
                if (!isActive) {
                    // Redirect or show an inactive user message based on your application's requirements
                    // set path to /inactive
                    request
                        .getRequestDispatcher(
                            "/WEB-INF/views/pending-approval.jsp"
                        )
                        .forward(request, response);
                    return;
                }

                if (!hasAccess) {
                    // Redirect or show an unauthorized access message based on your application's requirements
                    httpResponse.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "You do not have permission to access this resource."
                    );
                    return;
                }
            }
        }

        // Continue with the filter chain for all other requests
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup resources if necessary
    }
}
