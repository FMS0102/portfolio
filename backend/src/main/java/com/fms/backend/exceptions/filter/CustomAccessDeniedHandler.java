package com.fms.backend.exceptions.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fms.backend.exceptions.ExceptionResponse;
import com.fms.backend.exceptions.auth.CustomAccessDeniedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        CustomAccessDeniedException customEx = new CustomAccessDeniedException();

        ExceptionResponse errorResponse = new ExceptionResponse(
                new Date(),
                customEx.getMessage(),
                request.getRequestURI()
        );

        String json = new ObjectMapper().writeValueAsString(errorResponse);
        response.getWriter().write(json);
    }
}