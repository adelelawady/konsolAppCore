package com.konsol.core.repository;

import com.konsol.core.domain.playstation.PlaystationDevice;
import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the PlaystationDevice entity.
 */
@Repository
public interface PlaystationDeviceRepository extends MongoRepository<PlaystationDevice, String> {
    String DEVICES_BY_CATEGORY = "devicesByCategory";
    String DEVICE_BY_DEVICE_ID = "deviceByDeviceId";

    @Cacheable(cacheNames = DEVICE_BY_DEVICE_ID)
    Optional<PlaystationDevice> findById(String id);

    @Cacheable(cacheNames = DEVICES_BY_CATEGORY)
    List<PlaystationDevice> findAllByCategory(String category);
}
