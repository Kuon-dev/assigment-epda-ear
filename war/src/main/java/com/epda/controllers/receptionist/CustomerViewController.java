package com.epda.controllers.receptionist;

import com.epda.facade.CustomerFacade;
import com.epda.model.Customer;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/receptionist/customers/view/*")
public class CustomerViewController extends HttpServlet {

    @EJB
    private CustomerFacade customerFacade;

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        int currentPage = 1;
        if (pathInfo != null && !pathInfo.isEmpty()) {
            String[] splits = pathInfo.split("/");
            if (splits.length > 1) {
                try {
                    currentPage = Integer.parseInt(splits[1]);
                } catch (NumberFormatException e) {
                    currentPage = 1;
                }
            }
        }

        String searchQuery = request.getParameter("search");
        String encodedSearchQuery = "";

        List<Customer> customers;
        int totalCustomers;

        if (searchQuery != null && !searchQuery.isEmpty()) {
            encodedSearchQuery = URLEncoder.encode(
                searchQuery,
                StandardCharsets.UTF_8.toString()
            );

            customers = customerFacade.findByEmail(searchQuery);
            totalCustomers = customers != null ? customers.size() : 0;
        } else {
            customers = customerFacade.findAll();
            totalCustomers = customerFacade.count();
        }

        customers = searchQuery != null && !searchQuery.isEmpty()
            ? customers
            : customers.subList(
                (currentPage - 1) * 10,
                Math.min(currentPage * 10, customers.size())
            );

        int totalPages = (int) Math.ceil((double) totalCustomers / 10);
        int maxPagesToShow = 5;
        int halfPagesToShow = maxPagesToShow / 2;
        int startPage = Math.max(currentPage - halfPagesToShow, 1);
        int endPage = Math.min(startPage + maxPagesToShow - 1, totalPages);
        if (endPage - startPage < maxPagesToShow - 1) {
            startPage = Math.max(endPage - (maxPagesToShow - 1), 1);
        }

        request.setAttribute("customers", customers);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);
        request.setAttribute("searchQuery", searchQuery);
        request
            .getRequestDispatcher(
                "/WEB-INF/views/receptionist/customer-table.jsp"
            )
            .forward(request, response);
    }
}
