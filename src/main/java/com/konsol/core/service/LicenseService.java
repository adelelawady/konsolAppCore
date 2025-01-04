package com.konsol.core.service;

import com.konsol.core.domain.License;
import com.konsol.core.repository.LicenseRepository;
import com.konsol.core.service.dto.LicenseDTO;
import com.konsol.core.service.dto.LicenseValidationResult;
import com.konsol.core.service.util.HardwareIdentifier;
import com.konsol.core.service.util.LicenseEncryption;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LicenseService {

    private final Logger log = LoggerFactory.getLogger(LicenseService.class);
    private final LicenseEncryption licenseEncryption;
    private final HardwareIdentifier hardwareIdentifier;
    private final LicenseRepository licenseRepository;
    private final SecureRandom secureRandom;

    public LicenseService(LicenseEncryption licenseEncryption, HardwareIdentifier hardwareIdentifier, LicenseRepository licenseRepository) {
        this.licenseEncryption = licenseEncryption;
        this.hardwareIdentifier = hardwareIdentifier;
        this.licenseRepository = licenseRepository;
        this.secureRandom = new SecureRandom();
    }

    /**
     * Generate a new license for a specific hardware ID
     */
    public LicenseDTO generateLicense(String clientId, int validityDays) {
        // Generate a unique license key
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String licenseKey = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        // Create license with expiration date
        License license = new License()
            .setClientId(clientId)
            .setLicenseKey(licenseKey)
            .setHardwareId(hardwareIdentifier.generateHardwareId())
            .setIssueDate(Instant.now())
            .setExpiryDate(Instant.now().plus(validityDays, ChronoUnit.DAYS))
            .setActive(true);

        // Encrypt sensitive license data
        String encryptedData = licenseEncryption.encryptLicenseData(license);
        license.setEncryptedData(encryptedData);

        // Save to database
        License savedLicense = licenseRepository.save(license);
        return convertToDTO(savedLicense);
    }

    /**
     * Validate a license key against hardware ID and expiration
     */
    public LicenseValidationResult validateLicense(String licenseKey) {
        try {
            // Find license in database
            Optional<License> licenseOpt = licenseRepository.findByLicenseKey(licenseKey);
            if (licenseOpt.isEmpty()) {
                return new LicenseValidationResult(false, "Invalid license key");
            }

            License license = licenseOpt.get();

            // Check if license is active
            if (!license.isActive()) {
                return new LicenseValidationResult(false, "License is inactive");
            }

            // Verify hardware ID
            String currentHardwareId = hardwareIdentifier.generateHardwareId();
            if (!license.getHardwareId().equals(currentHardwareId)) {
                return new LicenseValidationResult(false, "Hardware ID mismatch");
            }

            // Verify license hasn't expired
            if (Instant.now().isAfter(license.getExpiryDate())) {
                return new LicenseValidationResult(false, "License has expired");
            }

            // Verify encrypted data integrity
            boolean dataValid = licenseEncryption.verifyLicenseData(license);
            if (!dataValid) {
                return new LicenseValidationResult(false, "License data integrity check failed");
            }

            return new LicenseValidationResult(true, "License is valid");
        } catch (Exception e) {
            log.error("Error validating license", e);
            return new LicenseValidationResult(false, "Error validating license");
        }
    }

    /**
     * Revoke a license
     */
    public void revokeLicense(String licenseKey) {
        licenseRepository
            .findByLicenseKey(licenseKey)
            .ifPresent(license -> {
                license.setActive(false);
                licenseRepository.save(license);
            });
    }

    private LicenseDTO convertToDTO(License license) {
        return new LicenseDTO()
            .setLicenseKey(license.getLicenseKey())
            .setClientId(license.getClientId())
            .setIssueDate(license.getIssueDate())
            .setExpiryDate(license.getExpiryDate())
            .setActive(license.isActive());
    }
}
