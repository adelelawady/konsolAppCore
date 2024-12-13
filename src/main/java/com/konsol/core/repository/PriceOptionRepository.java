package com.konsol.core.repository;

import com.konsol.core.domain.PriceOption;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the PriceOption entity.
 */
@Repository
public interface PriceOptionRepository extends MongoRepository<PriceOption, String> {}
