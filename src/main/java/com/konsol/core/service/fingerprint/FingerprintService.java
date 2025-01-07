package com.konsol.core.service.fingerprint;

import java.util.List;
import java.util.Optional;

public interface FingerprintService {
    /**
     * Initialize connection with HikVision device
     * @param ipAddress Device IP address
     * @param port Device port
     * @param username Device username
     * @param password Device password
     * @return true if connection successful, false otherwise
     */
    boolean initializeDevice(String ipAddress, int port, String username, String password);

    /**
     * Capture fingerprint from device
     * @param userId User ID to associate with fingerprint
     * @return captured fingerprint template as byte array
     */
    byte[] captureFingerprintTemplate(String userId);

    /**
     * Enroll fingerprint for a user
     * @param userId User ID
     * @param fingerprintTemplate Fingerprint template data
     * @return true if enrollment successful
     */
    boolean enrollFingerprint(String userId, byte[] fingerprintTemplate);

    /**
     * Verify fingerprint against stored template
     * @param fingerprintTemplate Fingerprint template to verify
     * @param userId User ID to verify against
     * @return true if verification successful
     */
    boolean verifyFingerprint(byte[] fingerprintTemplate, String userId);

    /**
     * Delete stored fingerprint template
     * @param userId User ID whose fingerprint should be deleted
     * @return true if deletion successful
     */
    boolean deleteFingerprint(String userId);

    /**
     * Get list of enrolled fingerprints
     * @return List of user IDs with enrolled fingerprints
     */
    List<String> getEnrolledFingerprints();

    /**
     * Get device connection status
     * @return true if device is connected
     */
    boolean isDeviceConnected();

    /**
     * Disconnect from device
     */
    void disconnect();
} 