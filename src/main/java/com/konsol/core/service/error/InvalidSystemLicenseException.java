package com.konsol.core.service.error;

public class InvalidSystemLicenseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidSystemLicenseException() {
        super("System license is invalid or expired. Please add a valid license to continue.");
    }
} 