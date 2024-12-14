package com.konsol.core.repository;

import com.konsol.core.domain.playstation.PlaystationDeviceType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the PlaystationDeviceType entity.
 */
@Repository
public interface PlaystationDeviceTypeRepository extends MongoRepository<PlaystationDeviceType, String> {}
