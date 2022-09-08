package com.konsol.core.repository;

import com.konsol.core.domain.Money;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Money entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MoneyRepository extends MongoRepository<Money, String> {}
