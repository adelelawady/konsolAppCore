package com.konsol.core.repository;

import com.konsol.core.domain.Money;
import com.konsol.core.domain.enumeration.MoneyKind;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
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

    /**
     * Find money records between dates.
     *
     * @param startDate start date
     * @param endDate end date
     * @return list of money records
     */
    List<Money> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find money records by type and date range.
     *
     * @param kind money kind (INCOME/EXPENSE)
     * @param startDate start date
     * @param endDate end date
     * @return list of money records
     */
    List<Money> findByKindAndCreatedDateBetween(MoneyKind kind, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find money records by bank and date range.
     *
     * @param bankId bank ID
     * @param startDate start date
     * @param endDate end date
     * @return list of money records
     */
    List<Money> findByBankIdAndCreatedDateBetween(String bankId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find money records by account and date range.
     *
     * @param accountId account ID
     * @param startDate start date
     * @param endDate end date
     * @return list of money records
     */
    List<Money> findByAccountIdAndCreatedDateBetween(String accountId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find money records by type.
     *
     * @param kind money kind (INCOME/EXPENSE)
     * @return list of money records
     */
    List<Money> findByKind(MoneyKind kind);

    /**
     * Find money records by bank ID and type.
     *
     * @param bankId bank ID
     * @param kind money kind (INCOME/EXPENSE)
     * @return list of money records
     */
    List<Money> findByBankIdAndKind(String bankId, MoneyKind kind);
}
