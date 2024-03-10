// code for register customer controller
//
package com.epda.controllers;

import com.epda.model.User;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
    name = "RegisterCustomerController",
    urlPatterns = { "/receptionist/new" }
)
public class RegisterCustomerController extends HttpServlet {

    @Override
    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String a = request.getParameter("email");
        String b = request.getParameter("password");
        String c = request.getParameter("name");
        String d = request.getParameter("phone");

        User user = new User();
        user.setEmail(a);
        user.setPassword((b));
        user.setName(c);
        user.setPhone(d);
    }

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request
            .getRequestDispatcher("customer-register.jsp")
            .forward(request, response);
    }
}
