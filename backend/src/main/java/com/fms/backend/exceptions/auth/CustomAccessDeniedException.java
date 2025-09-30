package com.fms.backend.exceptions.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class CustomAccessDeniedException extends RuntimeException {

    public CustomAccessDeniedException() {
        super("You do not have permission to access this resource.");
    }

    public CustomAccessDeniedException(String message) {
        super(message);
    }
}
