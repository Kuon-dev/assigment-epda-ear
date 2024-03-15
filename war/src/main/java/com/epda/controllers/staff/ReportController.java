package com.epda.controllers.staff;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/managing-staff/report/view")
public class ReportController extends HttpServlet {

    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        String reportType = request.getParameter("reportType");
        String path = "/WEB-INF/views/managing-staff/report-table.jsp"; // Base JSP that includes specific report JSPs
        if (reportType != null && !reportType.isEmpty()) {
            request.setAttribute("reportType", reportType);
        } else {
            // Optionally handle the case where no report type is selected or set a default
        }
        request.getRequestDispatcher(path).forward(request, response);
    }

    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        doGet(request, response);
    }
}
