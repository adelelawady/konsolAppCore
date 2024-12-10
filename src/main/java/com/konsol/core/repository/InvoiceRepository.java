package com.konsol.core.repository;

import com.konsol.core.domain.Invoice;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Invoice entity.
 */
@Repository
public interface InvoiceRepository extends MongoRepository<Invoice, String> {
    List<Invoice> findAllByActiveIsFalseAndCreatedDateBefore(Instant dateTime);

    /**
     * Find all invoices for a specific bank.
     *
     * @param bankId the bank ID
     * @return list of invoices
     */
    List<Invoice> findByBankId(String bankId);

    /**
     * Find all invoices between two dates.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @return list of invoices
     */
    List<Invoice> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find all invoices for a specific store between two dates.
     *
     * @param storeId the store ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of invoices
     */
    List<Invoice> findByStoreIdAndCreatedDateBetween(String storeId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find all invoices for a specific account between two dates.
     *
     * @param accountId the account ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of invoices
     */
    List<Invoice> findByAccountIdAndCreatedDateBetween(String accountId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find all deferred invoices between two dates.
     *
     * @param deferred whether the invoice is deferred
     * @param startDate the start date
     * @param endDate the end date
     * @return list of invoices
     */
    List<Invoice> findByDeferredAndCreatedDateBetween(boolean deferred, LocalDateTime startDate, LocalDateTime endDate);

    @Query("{ 'store.id': ?0, 'createdDate': { $gte: ?1, $lte: ?2 } }")
    List<Invoice> findInvoicesByStoreAndCreatedDateRange(String storeId, LocalDateTime startDate, LocalDateTime endDate);

    List<Invoice> findByCreatedDateBetweenAndDeferred(LocalDateTime startDate, LocalDateTime endDate, boolean b);

    boolean existsByStoreId(String id);
}
