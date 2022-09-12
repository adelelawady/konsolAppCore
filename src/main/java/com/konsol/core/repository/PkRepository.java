package com.konsol.core.repository;

import com.konsol.core.domain.Pk;
import com.konsol.core.domain.enumeration.PkKind;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Pk entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PkRepository extends MongoRepository<Pk, String> {
    Optional<Pk> findOneByKind(PkKind pkKind);
}
