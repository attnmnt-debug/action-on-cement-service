package org.example.exception;

public class OrderReadException extends RuntimeException {
    public OrderReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
