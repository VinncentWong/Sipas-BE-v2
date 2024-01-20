package org.example.exception;

public class ForbiddenException extends RuntimeException{

    private String message;

    public ForbiddenException(String message) {
        this.message = message;
    }

    public ForbiddenException(String message, String message1) {
        super(message);
        this.message = message1;
    }
}
