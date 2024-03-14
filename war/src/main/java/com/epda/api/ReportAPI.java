package com.epda.api;

import com.epda.facade.PetFacade;
import jakarta.ejb.EJB;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/managing-staff/report/*")
public class ReportAPI extends HttpServlet {

    @EJB
    private PetFacade petFacade;

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        String pathInfo = request.getPathInfo();

        if ("/pet-distribution".equals(pathInfo)) {
            generatePetDistributionReport(response);
        } else {
            // Handle other paths or set an appropriate error response
            response.sendError(
                HttpServletResponse.SC_NOT_FOUND,
                "Resource not found"
            );
        }
    }

    private void generatePetDistributionReport(HttpServletResponse response)
        throws IOException {
        List<Object[]> petTypeDistribution =
            petFacade.findPetTypeDistribution();
        Jsonb jsonb = JsonbBuilder.create();
        String result = jsonb.toJson(petTypeDistribution);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(result);
    }
}
