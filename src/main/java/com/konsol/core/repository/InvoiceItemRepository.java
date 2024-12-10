package com.konsol.core.repository;

import com.carrotsearch.hppc.ByteContainer;
import com.konsol.core.domain.InvoiceItem;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the InvoiceItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceItemRepository extends MongoRepository<InvoiceItem, String> {
    List<InvoiceItem> findByInvoiceId(String invoiceId);

    @Query("{'_class': 'com.konsol.core.domain.InvoiceItem', 'createdDate': {$gte: ?0, $lte: ?1}}")
    List<InvoiceItem> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<InvoiceItem> findAllByItemId(String id);
}
