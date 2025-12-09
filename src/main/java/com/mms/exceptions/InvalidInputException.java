package com.mms.exceptions;

/**
 * Exception thrown when invalid input is provided.
 */
public class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
