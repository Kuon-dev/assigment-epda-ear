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
import java.util.List;

@WebServlet("/receptionist/customers/new/*")
public class CustomerNewController extends HttpServlet {

    @EJB
    private CustomerFacade customerFacade;

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        int customerId;
        if (pathInfo != null && !pathInfo.isEmpty()) {
            String[] splits = pathInfo.split("/");
            if (splits.length > 1) {
                try {
                    customerId = Integer.parseInt(splits[1]);
                } catch (NumberFormatException e) {
                    // return page with error
                    throw new ServletException("Invalid customer ID");
                }
            }
        }
        request
            .getRequestDispatcher(
                "/WEB-INF/views/receptionist/customer-form.jsp"
            )
            .forward(request, response);
    }
}
