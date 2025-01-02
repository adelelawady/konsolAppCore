package com.konsol.core.repository.VAR;

import com.konsol.core.domain.VAR.ShopsOrders;
import java.time.Instant;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the ShopsOrders entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShopsOrdersRepository extends MongoRepository<ShopsOrders, String> {
    List<ShopsOrders> findTop20ByOrderByCreatedDateDesc();

    List<ShopsOrders> findAllByCreatedDateBetween(Instant startDate, Instant endDate);
}
