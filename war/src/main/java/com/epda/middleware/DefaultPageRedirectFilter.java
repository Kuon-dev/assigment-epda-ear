package com.epda.middleware;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
// import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// import java.util.Arrays;
// import java.util.List;

@WebServlet("/") // Specifies the URL pattern to intercept
public class DefaultPageRedirectFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Filter initialization logic can go here
    }

    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Check if the request is the default page
        String requestURI = httpRequest.getRequestURI();
        if (requestURI.equals(httpRequest.getContextPath() + "/")) {
            // Redirect to /login
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
        } else {
            // For other requests, continue with the filter chain
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // Cleanup resources
    }
}
