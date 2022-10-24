package com.konsol.core.repository;

import com.konsol.core.domain.Pk;
import com.konsol.core.domain.SystemConfiguration;
import com.konsol.core.domain.enumeration.PkKind;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the SystemConfiguration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemConfigurationRepository extends MongoRepository<SystemConfiguration, String> {}
