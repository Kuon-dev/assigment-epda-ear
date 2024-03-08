package com.epda.api;

import com.epda.facade.VeterinarianFacade;
import com.epda.model.Veterinarian;
import jakarta.ejb.EJB;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/veterinarian/*")
public class VeterinarianApi extends HttpServlet {

    @EJB
    private VeterinarianFacade veterinarianFacade;

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // Fetch all veterinarians
            List<Veterinarian> veterinarians = veterinarianFacade.findAll();
            writeResponse(response, veterinarians);
        } else {
            // Fetch a single veterinarian
            String[] splits = pathInfo.split("/");
            if (splits.length == 2) {
                try {
                    Long id = Long.parseLong(splits[1]);
                    Veterinarian veterinarian = veterinarianFacade.find(id);
                    if (veterinarian != null) {
                        writeResponse(response, veterinarian);
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        }
    }

    @Override
    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        Jsonb jsonb = JsonbBuilder.create();
        Veterinarian veterinarian = jsonb.fromJson(
            request.getReader(),
            Veterinarian.class
        );

        if (veterinarian != null) {
            veterinarianFacade.create(veterinarian);
            response.setStatus(HttpServletResponse.SC_CREATED);
            writeResponse(response, veterinarian);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            String[] splits = pathInfo.split("/");
            if (splits.length == 2) {
                try {
                    Long id = Long.parseLong(splits[1]);
                    Veterinarian veterinarian = veterinarianFacade.find(id);
                    if (veterinarian != null) {
                        veterinarianFacade.remove(veterinarian);
                        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void writeResponse(HttpServletResponse response, Object object)
        throws IOException {
        Jsonb jsonb = JsonbBuilder.create();
        String result = jsonb.toJson(object);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(result);
    }

    @Override
    protected void doPut(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.split("/").length == 2) {
            String[] splits = pathInfo.split("/");
            try {
                Long id = Long.parseLong(splits[1]);
                Veterinarian existingVeterinarian = veterinarianFacade.find(id);
                if (existingVeterinarian != null) {
                    Jsonb jsonb = JsonbBuilder.create();
                    Veterinarian updatedVeterinarian = jsonb.fromJson(
                        request.getReader(),
                        Veterinarian.class
                    );

                    // Ensure that the ID in the URL matches the ID in the body or is not set in the body
                    if (
                        updatedVeterinarian.getId() == null ||
                        updatedVeterinarian
                            .getId()
                            .equals(existingVeterinarian.getId())
                    ) {
                        // Copy properties from updatedVeterinarian to existingVeterinarian (excluding ID if necessary)
                        updateExistingVeterinarian(
                            existingVeterinarian,
                            updatedVeterinarian
                        );

                        veterinarianFacade.edit(existingVeterinarian); // Implement the update logic in the facade
                        writeResponse(response, existingVeterinarian);
                    } else {
                        response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "ID in URL does not match ID in body"
                        );
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    // Existing writeResponse method...

    private void updateExistingVeterinarian(
        Veterinarian existing,
        Veterinarian updated
    ) {
        // Copy properties from updated to existing. For example:
        existing.setName(updated.getName());
        existing.setPassword(updated.getPassword());
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());
        // Add more fields as necessary
    }
}
