package com.konsol.core.repository.VAR;

import com.konsol.core.domain.VAR.DeviceType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the DeviceType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeviceTypeRepository extends MongoRepository<DeviceType, String> {}
