package com.konsol.core.repository;

import com.konsol.core.domain.StoreItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the StoreItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StoreItemRepository extends MongoRepository<StoreItem, String> {}
