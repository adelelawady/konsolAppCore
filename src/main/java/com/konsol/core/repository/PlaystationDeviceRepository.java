package com.konsol.core.repository;

import com.konsol.core.domain.playstation.PlaystationDevice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the PlaystationDevice entity.
 */
@Repository
public interface PlaystationDeviceRepository extends MongoRepository<PlaystationDevice, String> {}
