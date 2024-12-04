package com.konsol.core.service.exception;

/**
 * Exception thrown when a bank is not found.
 */
public class BankNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BankNotFoundException(String message) {
        super(message);
    }

    public BankNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
