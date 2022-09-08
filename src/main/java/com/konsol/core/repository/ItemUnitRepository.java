package com.konsol.core.repository;

import com.konsol.core.domain.ItemUnit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the ItemUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemUnitRepository extends MongoRepository<ItemUnit, String> {}
