package com.konsol.core.repository;

import com.konsol.core.domain.License;
import java.util.List;
import java.util.Optional;
import java.time.Instant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LicenseRepository extends MongoRepository<License, String> {
    /**
     * Find a license by its license key
     * @param licenseKey the license key
     * @return the license if found
     */
    Optional<License> findByLicenseKey(String licenseKey);

    /**
     * Find all licenses for a specific client
     * @param clientId the client ID
     * @return list of licenses
     */
    List<License> findByClientId(String clientId);

    /**
     * Find active licenses by hardware ID
     * @param hardwareId the hardware ID
     * @return list of active licenses
     */
    List<License> findByHardwareIdAndActiveTrue(String hardwareId);

    /**
     * Check if a license key exists
     * @param licenseKey the license key
     * @return true if exists
     */
    boolean existsByLicenseKey(String licenseKey);

    /**
     * Find all active licenses with expiry date after specified date
     * @param date the reference date
     * @return list of valid licenses
     */
    List<License> findByActiveTrueAndExpiryDateAfter(Instant date);
}
