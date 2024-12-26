package com.konsol.core.repository;

import com.konsol.core.domain.playstation.PlayStationSession;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the PlayStationSession entity.
 */
@Repository
public interface PlayStationSessionRepository extends MongoRepository<PlayStationSession, String> {
    /**
     * Find active session for a device.
     *
     * @param deviceId the id of the device
     * @return the active session if exists
     */
    Optional<PlayStationSession> findByDeviceIdAndActiveTrue(String deviceId);

    List<PlayStationSession> findAllByDeviceCategoryIn(List<String> device_category);
}
