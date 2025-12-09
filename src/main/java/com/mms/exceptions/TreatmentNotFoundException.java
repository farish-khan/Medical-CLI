package com.mms.exceptions;

/**
 * Exception thrown when a treatment is not found in the system
 */
public class TreatmentNotFoundException extends Exception {
    public TreatmentNotFoundException(String message) {
        super(message);
    }

    public TreatmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
