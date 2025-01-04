package com.konsol.core.repository;

import com.konsol.core.domain.PlaystationContainer;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the PlaystationContainer entity.
 */
@Repository
public interface PlaystationContainerRepository extends MongoRepository<PlaystationContainer, String> {
    Optional<PlaystationContainer> findByCategory(@NotNull String category);
}
