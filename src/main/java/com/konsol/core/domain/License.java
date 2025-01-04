package com.konsol.core.domain;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "license")
public class License {

    @Id
    private String id;

    @Field("license_key")
    private String licenseKey;

    @Field("client_id")
    private String clientId;

    @Field("hardware_id")
    private String hardwareId;

    @Field("issue_date")
    private Instant issueDate;

    @Field("expiry_date")
    private Instant expiryDate;

    @Field("active")
    private boolean active;

    @Field("encrypted_data")
    private String encryptedData;

    // Getters and setters with fluent interface
    public String getId() {
        return id;
    }

    public License setId(String id) {
        this.id = id;
        return this;
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public License setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public License setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public License setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
        return this;
    }

    public Instant getIssueDate() {
        return issueDate != null ? issueDate.truncatedTo(ChronoUnit.MILLIS) : null;
    }

    public License setIssueDate(Instant issueDate) {
        this.issueDate = issueDate != null ? issueDate.truncatedTo(ChronoUnit.MILLIS) : null;
        return this;
    }

    public Instant getExpiryDate() {
        return expiryDate != null ? expiryDate.truncatedTo(ChronoUnit.MILLIS) : null;
    }

    public License setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate != null ? expiryDate.truncatedTo(ChronoUnit.MILLIS) : null;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public License setActive(boolean active) {
        this.active = active;
        return this;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public License setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
        return this;
    }
}
