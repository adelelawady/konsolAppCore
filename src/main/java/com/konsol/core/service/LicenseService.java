package com.konsol.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konsol.core.domain.License;
import com.konsol.core.repository.LicenseRepository;
import com.konsol.core.service.dto.LicenseDTO;
import com.konsol.core.service.dto.LicenseValidationResult;
import com.konsol.core.service.error.InvalidSystemLicenseException;
import com.konsol.core.service.util.HardwareIdentifier;
import com.konsol.core.service.util.LicenseEncryption;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import javax.crypto.Cipher;
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
    private final ObjectMapper objectMapper;

    // Private key for RSA decryption (this should be stored securely)
    private static final String PRIVATE_KEY =
        "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCcqYGbJozx72pljOpzEepPvfXz7GHfb7KjmECLRF2Vv2GsNjJXo1WCXOae59nKncUTFngVluPb0aQl/ronVyLXG5n6tMTAHM3jbvwnKzEVAUr/fmbZF2WtRL4oV81+NQ3qOx3AwYyEdW9Q40rqIoWJghEvnzJkjMI971IkBqQHLyXjeEADQSrotutq0/ZF+u+kXxAx5/XFtZ6SFwqYzAD/PivFwijO9gAiQcg0jLjX5cgknezHYeGrB9nKm9nq/fgopSHyERrKjNU4HKSoz3UnHZRkDGyPhg89YgQ8ZIGYzZJ4p4vJ2FssLj/2g0zaywwMpQpNH2KUMVG7kqFdmHsLAgMBAAECggEAHURvkaAwmaw8KM+cJzY0G4SmE9VX7YmtCnIRsNLrVuQDp8wSOJwrh+i6LvqqjfdOj4PVmFbxp6nxLCX7+giF2kDuUc9+qFNZRYGA+wJPL5jnvXaMWUdNOAMNMXVBzy+zM0yrAM1H5qEX1Dk6UCZ14xwZANgRboZ7bV2t70wMTcj+m5Q5cwb/aY5sF9RHNylPzBIUw30ukwRuGj302BSXFSHMuuwM+yFrncQjc+0wdu0k4tNiNIl3v61D05hvx9dnIVnefHUE67nU9Zotf/sMc06s9BsgLZTJ/xibSKUlejNIw+diqsEW4m9DngjHagCcjCbsFqiWEOQ6l59mDCQ3eQKBgQDXABQBCNS+sRlWhAYtOn3RYnGDN7b7MBTWYcg19AaJsMc4OiBU4RdHg4LONGXV/AGmFwcC7ykXcr/PvbkivfofdXN9jq4uTOrv7meauU9js28J/weH3cEQZ2d0r0bAplfntqUv8FfFN30qNHFsZcQoTtn2zOUKH/mUpQYCHZLNcwKBgQC6iXca+l9TEAG+S9bOgN8hWBVoJQD1ursoADdDq4FkqVPRCnXpEH9ZjZ28rvWFrOKkVokaNXupW7YlcbFve7vYxJHBBt0ebUlk6wyNMFaWB1wclbby6y+8C1YlGRU4YgEEITHMyXWtxU8H6Pkp0jUjr6t6QbojECwVCN/+TUM2CQKBgCOnS6nwQPdufvkt46hUbtDuNkzATIPTMWFrzbvEv+DNg/v7B6mLukSNtn9jiQ+3Pr7AStVdu767tLtKhw0P3A6jd4d2xcO/1aX8LwoleNjbxKovtJv/VijWPP3Ioz8HVsSCMVqFzMhTr1n0YGrNZX4ZtWOMDt9i9gyEYT6wkMupAoGAazpkLizoqQ3VgreDxndMTJWe0Jwc3iS2OztMiWjIW73+gXb66rviCO+gUftdBbepHQDllRxTdrpSjVm6vrQo+mWcx5ITf88g1pNLRsoXq+yVJZVmcmOBsB3wKikuCphDr0UJpIfWTsrSMGDbAhBlHZt4/jFUN52vSXQtlriBP0kCgYEAk8ZYGJTkBMQ6jDWnMuoaj6HEHHBc0/UR5iVgzaLYrmcrrS/wW+PDfLSnkUTFeRWvPGNlqmPup12CdT6q72ULRN8FotOr6a8Ap2cWtJIRiVRBDPZA+JJkRdhjfasaQGcPyS50ixnKx3HyGbodZGuHwypQPiDKUDXRTELZiBp611Y="; // Store this securely!

    // Public key for RSA encryption (this should be paired with the private key)
    private static final String PUBLIC_KEY =
        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnKmBmyaM8e9qZYzqcxHqT7318+xh32+yo5hAi0Rdlb9hrDYyV6NVglzmnufZyp3FExZ4FZbj29GkJf66J1ci1xuZ+rTEwBzN4278JysxFQFK/35m2RdlrUS+KFfNfjUN6jsdwMGMhHVvUONK6iKFiYIRL58yZIzCPe9SJAakBy8l43hAA0Eq6LbratP2RfrvpF8QMef1xbWekhcKmMwA/z4rxcIozvYAIkHINIy41+XIJJ3sx2HhqwfZypvZ6v34KKUh8hEayozVOBykqM91Jx2UZAxsj4YPPWIEPGSBmM2SeKeLydhbLC4/9oNM2ssMDKUKTR9ilDFRu5KhXZh7CwIDAQAB"; // Store this securely!

    public LicenseService(
        LicenseEncryption licenseEncryption,
        HardwareIdentifier hardwareIdentifier,
        LicenseRepository licenseRepository,
        ObjectMapper objectMapper
    ) {
        this.licenseEncryption = licenseEncryption;
        this.hardwareIdentifier = hardwareIdentifier;
        this.licenseRepository = licenseRepository;
        this.objectMapper = objectMapper;
        this.secureRandom = new SecureRandom();
    }

    /**
     * Process encrypted superAdmin license key
     * @param encryptedKey RSA encrypted license key containing license details
     * @return LicenseDTO if successful
     */
    public LicenseDTO processSuperAdminKey(String encryptedKey) {
        try {
            // Decrypt the key
            String decryptedJson = decryptRSA(encryptedKey);

            // Parse the JSON into AdminLicenseData
            AdminLicenseData licenseData = objectMapper.readValue(decryptedJson, AdminLicenseData.class);

            // Validate the admin license data
            validateAdminLicenseData(licenseData);

            // Generate new license using the provided data
            return generateLicense(licenseData.getClientId(), licenseData.getExpiryDate());
        } catch (Exception e) {
            log.error("Failed to process super admin key", e);
            throw new RuntimeException("Invalid license key");
        }
    }

    /**
     * Generate a new license with specific expiry date
     */
    public LicenseDTO generateLicense(String clientId, Instant expiryDate) {
        // Generate a unique license key
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String licenseKey = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        // Create license with provided expiration date
        License license = new License()
            .setClientId(clientId)
            .setLicenseKey(licenseKey)
            .setHardwareId(hardwareIdentifier.generateHardwareId())
            .setIssueDate(Instant.now())
            .setExpiryDate(expiryDate)
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

    /**
     * Decrypt RSA encrypted string using private key
     */
    public String decryptRSA(String encryptedData) throws Exception {
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);

        // Convert private key string to PrivateKey object
        byte[] privateKeyBytes = Base64.getDecoder().decode(PRIVATE_KEY);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        // Decrypt
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes);
    }

    /**
     * Create and encrypt admin license data
     * @param clientId Client identifier
     * @param expiryDate License expiration date
     * @return Encrypted license key string
     */
    public String createEncryptedAdminKey(String clientId, Instant expiryDate) {
        try {
            // Create admin license data
            AdminLicenseData licenseData = new AdminLicenseData();
            licenseData.setClientId(clientId);
            licenseData.setExpiryDate(expiryDate);

            // Optional: Add a signature
            String dataToSign = clientId + expiryDate.toString();
            licenseData.setSignature(generateSignature(dataToSign));

            // Convert to JSON
            String jsonData = objectMapper.writeValueAsString(licenseData);

            // Encrypt the JSON data
            return encryptRSA(jsonData);
        } catch (Exception e) {
            log.error("Failed to create encrypted admin key", e);
            throw new RuntimeException("Failed to create license key");
        }
    }

    /**
     * Encrypt string data using RSA public key
     */
    private String encryptRSA(String data) throws Exception {
        // Convert public key string to PublicKey object
        byte[] publicKeyBytes = Base64.getDecoder().decode(PUBLIC_KEY);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        // Encrypt
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Generate a signature for the license data
     * This is a simple example - you might want to use a more sophisticated signing mechanism
     */
    private String generateSignature(String data) throws Exception {
        // Create a signature using a hash of the data and a secret key
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest((data + PRIVATE_KEY).getBytes()); // Using private key as salt
        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Verify the signature of admin license data
     */
    private boolean verifySignature(AdminLicenseData licenseData) throws Exception {
        String dataToSign = licenseData.getClientId() + licenseData.getExpiryDate().toString();
        String expectedSignature = generateSignature(dataToSign);
        return expectedSignature.equals(licenseData.getSignature());
    }

    /**
     * Validate admin license data
     */
    private void validateAdminLicenseData(AdminLicenseData licenseData) {
        if (licenseData.getClientId() == null || licenseData.getClientId().trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid client ID");
        }

        if (licenseData.getExpiryDate() == null || licenseData.getExpiryDate().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Invalid expiry date");
        }

        try {
            if (!verifySignature(licenseData)) {
                throw new IllegalArgumentException("Invalid signature");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Signature verification failed", e);
        }
    }

    /**
     * Generate public and private keys
     */
    public void generatePublicAndPrivateKeys() {
        // Generate public and private keys
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Convert keys to Base64 strings
        String publicKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        String privateKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

        System.out.println("privateKeyBase64 " + privateKeyBase64);
        System.out.println("publicKeyBase64 " + publicKeyBase64);
        // Store in environment variables
        System.setProperty("PUBLIC_KEY", publicKeyBase64);
        System.setProperty("PRIVATE_KEY", privateKeyBase64);
    }

    /**
     * Data class for admin license information
     */
    public static class AdminLicenseData {

        private String clientId;
        private Instant expiryDate;
        private String signature; // Optional: Add signature for additional security

        // Getters and setters
        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public Instant getExpiryDate() {
            return expiryDate;
        }

        public void setExpiryDate(Instant expiryDate) {
            this.expiryDate = expiryDate;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        @Override
        public String toString() {
            return (
                "AdminLicenseData{" +
                "clientId='" +
                clientId +
                '\'' +
                ", expiryDate=" +
                expiryDate +
                ", signature='" +
                signature +
                '\'' +
                '}'
            );
        }
    }

    /**
     * Validate that the system has at least one valid license
     * @throws InvalidSystemLicenseException if no valid license is found
     */
    public void validateSystemLicense() {
        List<License> validLicenses = licenseRepository.findByActiveTrueAndExpiryDateAfter(Instant.now());

        if (validLicenses.isEmpty()) {
            log.error("No valid system license found");
            throw new InvalidSystemLicenseException();
        }

        // Additional validation if needed
        for (License license : validLicenses) {
            try {
                LicenseValidationResult result = validateLicense(license.getLicenseKey());
                if (result.isValid()) {
                    // Found a valid license, return successfully
                    return;
                }
            } catch (Exception e) {
                log.warn("Error validating license {}: {}", license.getLicenseKey(), e.getMessage());
                // Continue checking other licenses
            }
        }

        // If we get here, no valid license was found
        throw new InvalidSystemLicenseException();
    }

    /**
     * Validate that the system has at least one valid license
     * @throws InvalidSystemLicenseException if no valid license is found
     */
    public boolean validateSystemLicenseKeys() {
        List<License> validLicenses = licenseRepository.findByActiveTrueAndExpiryDateAfter(Instant.now());
        // Additional validation if needed
        for (License license : validLicenses) {
            try {
                LicenseValidationResult result = validateLicense(license.getLicenseKey());
                if (result.isValid()) {
                    // Found a valid license, return successfully
                    return true;
                }
            } catch (Exception e) {
                log.warn("Error validating license {}: {}", license.getLicenseKey(), e.getMessage());
                // Continue checking other licenses
            }
        }
        return false;
    }
}
