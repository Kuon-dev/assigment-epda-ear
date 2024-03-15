package com.epda.api;

import com.epda.facade.ManagingStaffFacade;
import com.epda.model.ManagingStaff;
import jakarta.ejb.EJB;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/managing-staff/*")
public class ManagingStaffApi extends HttpServlet {

    @EJB
    private ManagingStaffFacade managingStaffFacade;

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // Fetch all managingStaffs
            List<ManagingStaff> managingStaffs = managingStaffFacade.findAll();
            writeResponse(response, managingStaffs);
        } else {
            // Fetch a single managingStaff
            String[] splits = pathInfo.split("/");
            if (splits.length == 2) {
                try {
                    Long id = Long.parseLong(splits[1]);
                    ManagingStaff managingStaff = managingStaffFacade.find(id);
                    if (managingStaff != null) {
                        writeResponse(response, managingStaff);
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
        ManagingStaff managingStaff = jsonb.fromJson(
            request.getReader(),
            ManagingStaff.class
        );

        if (managingStaff != null) {
            managingStaffFacade.create(managingStaff);
            response.setStatus(HttpServletResponse.SC_CREATED);
            writeResponse(response, managingStaff);
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
                    ManagingStaff managingStaff = managingStaffFacade.find(id);
                    if (managingStaff != null) {
                        managingStaffFacade.remove(managingStaff);
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
                ManagingStaff existingManagingStaff = managingStaffFacade.find(
                    id
                );
                if (existingManagingStaff != null) {
                    Jsonb jsonb = JsonbBuilder.create();
                    ManagingStaff updatedManagingStaff = jsonb.fromJson(
                        request.getReader(),
                        ManagingStaff.class
                    );

                    // Ensure that the ID in the URL matches the ID in the body or is not set in the body
                    if (
                        updatedManagingStaff.getId() == null ||
                        updatedManagingStaff
                            .getId()
                            .equals(existingManagingStaff.getId())
                    ) {
                        // Copy properties from updatedManagingStaff to existingManagingStaff (excluding ID if necessary)
                        updateExistingManagingStaff(
                            existingManagingStaff,
                            updatedManagingStaff
                        );

                        managingStaffFacade.edit(existingManagingStaff); // Implement the update logic in the facade
                        writeResponse(response, existingManagingStaff);
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

    private void updateExistingManagingStaff(
        ManagingStaff existing,
        ManagingStaff updated
    ) {
        // Copy properties from updated to existing. For example:
        existing.setName(updated.getName());
        existing.setPassword(updated.getPassword());
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());
    }

    private void writeResponse(HttpServletResponse response, Object object)
        throws IOException {
        Jsonb jsonb = JsonbBuilder.create();
        String result = jsonb.toJson(object);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(result);
    }
}
