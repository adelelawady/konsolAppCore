package com.konsol.core.repository;

import com.konsol.core.domain.Invoice;
import com.konsol.core.domain.playstation.PlayStationSession;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
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

    List<PlayStationSession> findAllByDeviceIdAndActiveTrue(String deviceId);
    /**
     * Find all sessions where device category matches any of the provided categories.
     */

    Page<PlayStationSession> findAllByContainerIdAndEndTimeIsNotNullOrderByEndTimeDesc(Pageable page, String containerId);

    List<PlayStationSession> findByEndTimeBetween(Instant startDate, Instant endDate);

    List<PlayStationSession> findByStartTimeBetweenAndActiveIsFalse(Instant startDate, Instant endDate);

    Page<PlayStationSession> findAllByEndTimeIsNotNullOrderByEndTimeDesc(Pageable pageable);

    List<PlayStationSession> findByStartTimeBetween(Instant startTime, Instant endTime);
}
