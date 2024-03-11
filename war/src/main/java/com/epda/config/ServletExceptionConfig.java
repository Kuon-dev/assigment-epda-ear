package com.epda.config;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServletExceptionConfig {

    public static void sendError(
        HttpServletResponse response,
        int statusCode,
        String errorMessage
    ) {
        try {
            response.setStatus(statusCode);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String jsonError = String.format(
                "{\"status\":%d, \"error\":\"%s\"}",
                statusCode,
                errorMessage
            );
            response.getWriter().write(jsonError);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
