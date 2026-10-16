package org.example.exception;

public class OrderReadException extends OrderProcessingException {
    public OrderReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
