package com.konsol.core.service.dto;

import java.io.Serializable;

public class LicenseValidationResult implements Serializable {

    private final boolean valid;
    private final String message;

    public LicenseValidationResult(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    public boolean isValid() {
        return valid;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "LicenseValidationResult{" + "valid=" + valid + ", message='" + message + '\'' + '}';
    }
}
