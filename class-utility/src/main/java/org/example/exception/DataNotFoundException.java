package org.example.exception;

public class DataNotFoundException extends RuntimeException{

    private String message;

    public DataNotFoundException(String message) {
        this.message = message;
    }

    public DataNotFoundException(String message, String message1) {
        super(message);
        this.message = message1;
    }
}
