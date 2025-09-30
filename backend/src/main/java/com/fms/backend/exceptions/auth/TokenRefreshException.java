package com.fms.backend.exceptions.auth;

public class TokenRefreshException extends RuntimeException {
    public TokenRefreshException() {
        super("You do not have permission to access this resource.");
    }

    public TokenRefreshException(String message) {
        super(message);
    }
}
