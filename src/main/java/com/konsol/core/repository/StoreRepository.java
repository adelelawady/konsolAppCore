package com.konsol.core.repository;

import com.konsol.core.domain.Store;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Store entity.
 */
@Repository
public interface StoreRepository extends MongoRepository<Store, String> {
    @Query("{}")
    Page<Store> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Store> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Store> findOneWithEagerRelationships(String id);

    List<Store> findAllByIdNotIn(List<String> ids);
}
