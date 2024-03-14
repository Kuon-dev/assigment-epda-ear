package com.epda.api;

import com.epda.facade.AppointmentFacade;
import com.epda.facade.CustomerFacade;
import com.epda.facade.PetFacade;
import com.epda.facade.VeterinarianFacade;
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

    @EJB
    private AppointmentFacade appointmentFacade;

    @EJB
    private CustomerFacade customerFacade;

    @EJB
    private VeterinarianFacade veterinarianFacade;

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        String pathInfo = request.getPathInfo();

        if ("/pet-distribution".equals(pathInfo)) {
            generatePetDistributionReport(response);
        } else if ("/appointment-status-distribution".equals(pathInfo)) {
            generateAppointmentStatusDistributionReport(response);
        } else if ("/veterinarian-expertise-distribution".equals(pathInfo)) {
            generateVeterinarianExpertiseDistributionReport(response);
        } else if ("/customer-age-distribution".equals(pathInfo)) {
            generateCustomerAgeDistributionReport(response);
        } else if ("/monthly-appointment-trends".equals(pathInfo)) {
            generateMonthlyAppointmentTrendsReport(response);
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

    private void generateAppointmentStatusDistributionReport(
        HttpServletResponse response
    ) throws IOException {
        List<Object[]> appointmentStatusDistribution =
            appointmentFacade.findAppointmentStatusDistribution();
        Jsonb jsonb = JsonbBuilder.create();
        String result = jsonb.toJson(appointmentStatusDistribution);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(result);
    }

    private void generateCustomerAgeDistributionReport(
        HttpServletResponse response
    ) throws IOException {
        List<Object[]> ageDistribution =
            customerFacade.findCustomerAgeDistribution();
        Jsonb jsonb = JsonbBuilder.create();
        String result = jsonb.toJson(ageDistribution);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(result);
    }

    private void generateMonthlyAppointmentTrendsReport(
        HttpServletResponse response
    ) throws IOException {
        List<Object[]> monthlyTrends =
            appointmentFacade.findMonthlyAppointmentTrends();
        Jsonb jsonb = JsonbBuilder.create();
        String result = jsonb.toJson(monthlyTrends);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(result);
    }

    private void generateVeterinarianExpertiseDistributionReport(
        HttpServletResponse response
    ) throws IOException {
        List<Object[]> expertiseDistribution =
            veterinarianFacade.findVeterinarianExpertiseDistribution();
        Jsonb jsonb = JsonbBuilder.create();
        String result = jsonb.toJson(expertiseDistribution);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(result);
    }
}
