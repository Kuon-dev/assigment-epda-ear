package com.epda.middleware;

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
                // Assuming the user object has a getStatus() method to check the user's status
                Object user = session.getAttribute("user");
                // You'll need to adjust this to match how you can retrieve the status from your user object
                String status = extractUserStatus(user); // Implement this method based on your user object

                // Redirect to a pending page if the status is not approved
                if (!"approved".equalsIgnoreCase(status)) {
                    httpResponse.sendRedirect(
                        httpRequest.getContextPath() + "/pending-approval"
                    );
                    return; // Prevent processing any further in the filter chain
                }
            }
        }

        // Continue with the filter chain for all other requests
        chain.doFilter(request, response);
    }

    private String extractUserStatus(Object user) {
        // Placeholder implementation - adjust this to your user object
        // For example, if your user object is an instance of a class with a getStatus() method, cast and call it here.
        // return ((YourUserClass) user).getStatus();
        return "approved"; // Just a placeholder, replace it with actual implementation
    }

    @Override
    public void destroy() {
        // Cleanup resources if necessary
    }
}
