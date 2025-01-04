package com.konsol.core.service.util;

import com.konsol.core.domain.License;
import java.security.SecureRandom;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LicenseEncryption {

    private final Logger log = LoggerFactory.getLogger(LicenseEncryption.class);
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ISO_INSTANT;

    @Value("${app.license.encryption.key}")
    private String encryptionKey;

    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;

    /**
     * Encrypt license data for tamper protection
     */
    public String encryptLicenseData(License license) {
        try {
            // Format timestamps consistently
            String sensitiveData = String.format(
                "%s|%s|%s|%s",
                license.getLicenseKey(),
                license.getHardwareId(),
                TIMESTAMP_FORMATTER.format(license.getIssueDate()),
                TIMESTAMP_FORMATTER.format(license.getExpiryDate())
            );

            log.debug("Encrypting data: {}", sensitiveData);

            // Generate a random IV
            byte[] iv = new byte[GCM_IV_LENGTH];
            new SecureRandom().nextBytes(iv);

            // Initialize cipher
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKey key = new SecretKeySpec(Base64.getDecoder().decode(encryptionKey), "AES");
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);

            // Encrypt the data
            byte[] cipherText = cipher.doFinal(sensitiveData.getBytes());

            // Combine IV and ciphertext
            byte[] encrypted = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, encrypted, 0, iv.length);
            System.arraycopy(cipherText, 0, encrypted, iv.length, cipherText.length);

            String result = Base64.getEncoder().encodeToString(encrypted);
            log.debug("Encrypted result: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Error encrypting license data", e);
            throw new RuntimeException("Error encrypting license data", e);
        }
    }

    /**
     * Verify the integrity of encrypted license data
     */
    public boolean verifyLicenseData(License license) {
        try {
            log.debug("Verifying license data for key: {}", license.getLicenseKey());

            byte[] encrypted = Base64.getDecoder().decode(license.getEncryptedData());

            // Extract IV
            byte[] iv = new byte[GCM_IV_LENGTH];
            System.arraycopy(encrypted, 0, iv, 0, GCM_IV_LENGTH);

            // Extract ciphertext
            byte[] cipherText = new byte[encrypted.length - GCM_IV_LENGTH];
            System.arraycopy(encrypted, GCM_IV_LENGTH, cipherText, 0, cipherText.length);

            // Decrypt
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKey key = new SecretKeySpec(Base64.getDecoder().decode(encryptionKey), "AES");
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);

            byte[] decrypted = cipher.doFinal(cipherText);
            String decryptedData = new String(decrypted);

            log.debug("Decrypted data: {}", decryptedData);

            // Verify decrypted data matches license data
            String[] parts = decryptedData.split("\\|");
            boolean isValid =
                parts[0].equals(license.getLicenseKey()) &&
                parts[1].equals(license.getHardwareId()) &&
                parts[2].equals(TIMESTAMP_FORMATTER.format(license.getIssueDate())) &&
                parts[3].equals(TIMESTAMP_FORMATTER.format(license.getExpiryDate()));

            log.debug("License validation result: {}", isValid);
            if (!isValid) {
                log.debug(
                    "Expected: key={}, hwid={}, issue={}, expiry={}",
                    license.getLicenseKey(),
                    license.getHardwareId(),
                    TIMESTAMP_FORMATTER.format(license.getIssueDate()),
                    TIMESTAMP_FORMATTER.format(license.getExpiryDate())
                );
                log.debug("Got: key={}, hwid={}, issue={}, expiry={}", parts[0], parts[1], parts[2], parts[3]);
            }

            return isValid;
        } catch (Exception e) {
            log.error("Error verifying license data", e);
            return false;
        }
    }

    /**
     * Generate a new encryption key
     */
    public static String generateEncryptionKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey key = keyGen.generateKey();
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
