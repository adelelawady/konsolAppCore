package com.konsol.core.repository;

import com.konsol.core.domain.playstation.PlayStationSession;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import org.bson.Document;
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

    /**
     * Find all sessions where device category matches any of the provided categories.
     * Uses Document-based aggregation for better type safety and query construction.
     */
    @Aggregation(pipeline = {
        "#{new Document('$match', " +
            "new Document('device', new Document('$exists', true))" +
            ".append('device.category', new Document('$in', ?0))" +
        ")}"
    })
    List<PlayStationSession> findSessionsByCategories(List<String> categories);
}
