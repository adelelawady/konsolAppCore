package com.konsol.core.repository;

import com.konsol.core.domain.Invoice;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Invoice entity.
 */
@Repository
public interface InvoiceRepository extends MongoRepository<Invoice, String> {
    @Query("{}")
    Page<Invoice> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Invoice> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Invoice> findOneWithEagerRelationships(String id);

    List<Invoice> findAllByActiveIsFalseAndCreatedDateBefore(Instant dateTime);
}
