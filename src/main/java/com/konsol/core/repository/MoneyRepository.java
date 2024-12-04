package com.konsol.core.repository;

import com.konsol.core.domain.Money;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Money entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MoneyRepository extends MongoRepository<Money, String> {
    List<Money> findAllByIdIn(List<String> ids, Sort sort);

    /**
     * Find all money records for a specific bank.
     *
     * @param bankId the bank ID
     * @return list of money records
     */
    List<Money> findByBankId(String bankId);
}
