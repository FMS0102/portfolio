package com.fms.backend.exceptions.auth;

public class CustomAccessDeniedException extends RuntimeException {

    public CustomAccessDeniedException() {
        super("You do not have permission to access this resource.");
    }

    public CustomAccessDeniedException(String message) {
        super(message);
    }
}
