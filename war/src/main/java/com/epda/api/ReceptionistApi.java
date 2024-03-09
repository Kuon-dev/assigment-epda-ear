package com.epda.api;

import com.epda.facade.ReceptionistFacade;
import com.epda.model.Receptionist;
import jakarta.ejb.EJB;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/receptionist/*")
public class ReceptionistApi extends HttpServlet {

    @EJB
    private ReceptionistFacade receptionistFacade;

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // Fetch all receptionists
            List<Receptionist> receptionists = receptionistFacade.findAll();
            writeResponse(response, receptionists);
        } else {
            // Fetch a single receptionist
            String[] splits = pathInfo.split("/");
            if (splits.length == 2) {
                try {
                    Long id = Long.parseLong(splits[1]);
                    Receptionist receptionist = receptionistFacade.find(id);
                    if (receptionist != null) {
                        writeResponse(response, receptionist);
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
        Receptionist receptionist = jsonb.fromJson(
            request.getReader(),
            Receptionist.class
        );

        if (receptionist != null) {
            receptionistFacade.create(receptionist);
            response.setStatus(HttpServletResponse.SC_CREATED);
            writeResponse(response, receptionist);
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
                    Receptionist receptionist = receptionistFacade.find(id);
                    if (receptionist != null) {
                        receptionistFacade.remove(receptionist);
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
                Receptionist existingReceptionist = receptionistFacade.find(id);
                if (existingReceptionist != null) {
                    Jsonb jsonb = JsonbBuilder.create();
                    Receptionist updatedReceptionist = jsonb.fromJson(
                        request.getReader(),
                        Receptionist.class
                    );

                    // Ensure that the ID in the URL matches the ID in the body or is not set in the body
                    if (
                        updatedReceptionist.getId() == null ||
                        updatedReceptionist
                            .getId()
                            .equals(existingReceptionist.getId())
                    ) {
                        // Copy properties from updatedReceptionist to existingReceptionist (excluding ID if necessary)
                        updateExistingReceptionist(
                            existingReceptionist,
                            updatedReceptionist
                        );

                        receptionistFacade.edit(existingReceptionist); // Implement the update logic in the facade
                        writeResponse(response, existingReceptionist);
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

    private void updateExistingReceptionist(
        Receptionist existing,
        Receptionist updated
    ) {
        // Copy properties from updated to existing. For example:
        existing.setName(updated.getName());
        existing.setPassword(updated.getPassword());
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());
        // Add more fields as necessary
    }
}
