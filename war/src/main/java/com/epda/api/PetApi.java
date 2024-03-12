package com.epda.api;

import com.epda.config.ServletExceptionConfig;
import com.epda.facade.PetFacade;
import com.epda.model.Pet;
import jakarta.ejb.EJB;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/pet/*")
public class PetApi extends HttpServlet {

    @EJB
    private PetFacade petFacade;

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            List<Pet> pets = petFacade.findAll();
            writeResponse(response, pets);
        } else {
            String[] splits = pathInfo.split("/");
            if (splits.length == 2) {
                try {
                    Long id = Long.parseLong(splits[1]);
                    Pet pet = petFacade.find(id);
                    if (pet != null) {
                        writeResponse(response, pet);
                    } else {
                        ServletExceptionConfig.sendError(
                            response,
                            HttpServletResponse.SC_NOT_FOUND,
                            "Pet not found"
                        );
                    }
                } catch (NumberFormatException e) {
                    ServletExceptionConfig.sendError(
                        response,
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid pet ID format"
                    );
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
        try (BufferedReader reader = request.getReader()) {
            Pet pet = jsonb.fromJson(reader, Pet.class);
            petFacade.create(pet);
            response.setStatus(HttpServletResponse.SC_CREATED);
            writeResponse(response, pet);
        } catch (Exception ex) {
            ServletExceptionConfig.sendError(
                response,
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "An error occurred while creating the pet"
            );
        }
    }

    @Override
    protected void doPut(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        Jsonb jsonb = JsonbBuilder.create();
        String pathInfo = request.getPathInfo();

        if (
            pathInfo == null ||
            pathInfo.equals("/") ||
            pathInfo.split("/").length < 2
        ) {
            ServletExceptionConfig.sendError(
                response,
                HttpServletResponse.SC_BAD_REQUEST,
                "Missing pet ID"
            );
            return;
        }

        Long petId;
        try {
            petId = Long.parseLong(pathInfo.split("/")[1]);
        } catch (NumberFormatException e) {
            ServletExceptionConfig.sendError(
                response,
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid pet ID format"
            );
            return;
        }

        try (BufferedReader reader = request.getReader()) {
            Pet updatedPetInfo = jsonb.fromJson(reader, Pet.class);
            Pet existingPet = petFacade.find(petId);

            if (existingPet == null) {
                ServletExceptionConfig.sendError(
                    response,
                    HttpServletResponse.SC_NOT_FOUND,
                    "Pet not found"
                );
                return;
            }

            existingPet.setName(updatedPetInfo.getName());
            existingPet.setType(updatedPetInfo.getType());
            existingPet.setBreed(updatedPetInfo.getBreed());
            existingPet.setAge(updatedPetInfo.getAge());

            petFacade.edit(existingPet);
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String jsonSuccess = String.format(
                "{\"status\":%d, \"error\":\"%s\"}",
                200,
                "Update successful"
            );
            response.getWriter().write(jsonSuccess);
            // response.setStatus(HttpServletResponse.SC_OK);
            // ServletExceptionConfig.sendError(response, HttpServletResponse.SC_OK, "Update successful");
        } catch (Exception ex) {
            ServletExceptionConfig.sendError(
                response,
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "An error occurred while updating the pet"
            );
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
