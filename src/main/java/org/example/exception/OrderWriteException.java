package org.example.exception;

public class OrderWriteException extends OrderProcessingException {
    public OrderWriteException(String message, Throwable cause) {
        super(message, cause);
    }
}
