package org.example.usermanagement.exception;

// Custom exception for user already exists
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
