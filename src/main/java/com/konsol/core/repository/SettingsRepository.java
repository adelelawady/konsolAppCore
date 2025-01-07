package com.konsol.core.repository;

import com.konsol.core.domain.Settings;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Settings entity.
 */
@Repository
public interface SettingsRepository extends MongoRepository<Settings, String> {


   
    Settings findFirstByOrderById();
}
