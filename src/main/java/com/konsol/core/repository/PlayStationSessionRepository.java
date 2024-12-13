package com.konsol.core.repository;

import com.konsol.core.domain.PlayStationSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the PlayStationSession entity.
 */
@Repository
public interface PlayStationSessionRepository extends MongoRepository<PlayStationSession, Long> {}
