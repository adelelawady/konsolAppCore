package com.konsol.core.service.dto;

import java.io.Serializable;
import java.time.Instant;

public class LicenseDTO implements Serializable {

    private String licenseKey;
    private String clientId;
    private Instant issueDate;
    private Instant expiryDate;
    private boolean active;

    // Constructors
    public LicenseDTO() {}

    // Getters and setters with fluent interface
    public String getLicenseKey() {
        return licenseKey;
    }

    public LicenseDTO setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public LicenseDTO setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public Instant getIssueDate() {
        return issueDate;
    }

    public LicenseDTO setIssueDate(Instant issueDate) {
        this.issueDate = issueDate;
        return this;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public LicenseDTO setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public LicenseDTO setActive(boolean active) {
        this.active = active;
        return this;
    }

    @Override
    public String toString() {
        return (
            "LicenseDTO{" +
            "licenseKey='" +
            licenseKey +
            '\'' +
            ", clientId='" +
            clientId +
            '\'' +
            ", issueDate=" +
            issueDate +
            ", expiryDate=" +
            expiryDate +
            ", active=" +
            active +
            '}'
        );
    }
}
