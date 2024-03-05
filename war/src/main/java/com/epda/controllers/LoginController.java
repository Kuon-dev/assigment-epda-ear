package com.epda.controllers;

import com.epda.model.User;
import com.epda.model.UserFacade;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
// import jakarta.enterprise.context.RequestScoped;
// import jakarta.inject.Named;

// @Named
// @RequestScoped
@WebServlet(name = "LoginController", urlPatterns = { "/login" })
public class LoginController extends HttpServlet {

    @EJB
    private UserFacade userFacade;
    private String authError;

    // user auth method

    protected void processRequest(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String a = request.getParameter("email");
        String b = request.getParameter("password");
        System.out.println(a);
        System.out.println(b);
        System.out.println(response);

        try (PrintWriter out = response.getWriter()) {
            System.out.println("Test");
            try {
                User found = userFacade.find(a);
                if (found == null) {
                    throw new Exception();
                }
                int x = Integer.parseInt(b);
                if (x != found.getPassword()) {
                    throw new Exception();
                }
                HttpSession s = request.getSession();
                s.setAttribute("user", found);
                request
                    .getRequestDispatcher("link.jsp")
                    .include(request, response);
            } catch (Exception e) {
                request.setAttribute("authError", "Invalid credentials provided.");
                request
                    .getRequestDispatcher("login.jsp")
                    .include(request, response);
                System.out.println(e);
            }
        }
    }

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        processRequest(request, response);
    }
}
