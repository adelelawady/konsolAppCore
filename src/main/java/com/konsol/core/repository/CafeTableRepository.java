package com.konsol.core.repository;

import com.konsol.core.domain.CafeTable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the CafeTable entity.
 */
@Repository
public interface CafeTableRepository extends MongoRepository<CafeTable, String> {}
