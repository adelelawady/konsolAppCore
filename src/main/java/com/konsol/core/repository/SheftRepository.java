package com.konsol.core.repository;

import com.konsol.core.domain.Sheft;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Sheft entity.
 */
@Repository
public interface SheftRepository extends MongoRepository<Sheft, String> {}
