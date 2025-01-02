package com.konsol.core.repository.VAR;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import com.konsol.core.domain.VAR.Takeaway;
import java.time.Instant;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Takeaway entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TakeawayRepository extends MongoRepository<Takeaway, String> {
    List<Takeaway> findAllByOrderByCreatedDateDesc();

    List<Takeaway> findTop20ByOrderByCreatedDateDesc();

    List<Takeaway> findAllByCreatedDateBetween(Instant startDate, Instant endDate);
}
