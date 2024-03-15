package com.epda.controllers.receptionist;

// import com.epda.facade.AppointmentFacade;
// import com.epda.facade.CustomerFacade;
import com.epda.facade.PetFacade;
import com.epda.facade.VeterinarianFacade;
// import com.epda.model.Appointment;
import com.epda.model.Customer;
import com.epda.model.Pet;
import com.epda.model.Veterinarian;
import com.epda.model.enums.AppointmentStatus;
import com.epda.model.enums.TimeSlot;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
// import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/receptionist/appointments/new")
public class AppointmentNewController extends HttpServlet {

    @EJB
    private PetFacade petFacade;

    @EJB
    private VeterinarianFacade veterinarianFacade;

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        String petIdParam = request.getParameter("petId");

        try {
            Long petId = Long.parseLong(petIdParam);

            // Fetch the pet from the database
            Pet pet = petFacade.find(petId);

            // Check if the pet exists
            if (pet == null) {
                response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Pet not found"
                );
                return;
            }

            // Fetch the customer associated with the pet
            Customer customer = pet.getCustomer(); // Assuming a direct relationship in your Pet entity

            // Ensure the customer exists
            if (customer == null) {
                response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Associated customer not found"
                );
                return;
            }

            // Set attributes for prefilling the form with customer and pet information
            request.setAttribute("pet", pet);
            request.setAttribute("customerName", customer.getName()); // Assume Customer entity has a getName() method
            request.setAttribute("customerEmail", customer.getEmail()); // Assume Customer entity has an getEmail() method
            request.setAttribute("petName", pet.getName()); // Assume Pet entity has a getName() method
            List<Veterinarian> vetList = veterinarianFacade.findByExpertise(
                pet.getType()
            );
            request.setAttribute("veterinarians", vetList);

            // Setting additional attributes for creating a new appointment
            request.setAttribute("timeSlots", TimeSlot.values());
            request.setAttribute(
                "appointmentStatuses",
                AppointmentStatus.values()
            );

            // Forward to the JSP page for the new appointment form
            request
                .getRequestDispatcher(
                    "/WEB-INF/views/receptionist/appointment-new.jsp"
                )
                .forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid pet ID format"
            );
        }
    }
}
