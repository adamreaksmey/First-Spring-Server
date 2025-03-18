package com.first_spring.demo.exceptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

public class FilterExceptionHandler {
    // ✅ Handles 401 Unauthorized
    public static void handleUnauthorized(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", "You are not authorized to access this resource");

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
    }

    // ✅ Handles 403 Forbidden
    public static void handleForbidden(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_FORBIDDEN);
        body.put("error", "Forbidden");
        body.put("message", "You do not have permission to access this resource");

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
    }
}
