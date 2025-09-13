package com.fms.backend.exceptions.handler;

import com.fms.backend.exceptions.ExceptionResponse;
import com.fms.backend.exceptions.auth.CustomAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestController
@RestControllerAdvice
public class GlobalResponseHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomAuthenticationException.class)
    public final ResponseEntity<ExceptionResponse> handleUnauthorizedExceptions(CustomAuthenticationException ex, WebRequest request) {
        String message = ex.isGeneric() ? "Unauthorized access." : ex.getMessage();
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                message,
                request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }


}
