package com.konsol.core.repository.VAR;

import com.konsol.core.domain.VAR.Device;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Device entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeviceRepository extends MongoRepository<Device, String> {}
