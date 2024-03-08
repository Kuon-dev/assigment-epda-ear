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
}
