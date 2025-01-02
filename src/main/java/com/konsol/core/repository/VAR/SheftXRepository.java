package com.konsol.core.repository.VAR;

import com.konsol.core.domain.VAR.Sheft;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SheftXRepository extends MongoRepository<Sheft, String> {
    List<Sheft> findAllByEnd(String end);

    List<Sheft> findTop20ByOrderByCreatedDateDesc();

    Page<Sheft> findAllByOrderByCreatedDateDesc(Pageable pageable);
}
