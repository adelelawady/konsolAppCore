package com.konsol.core.repository;

import com.konsol.core.domain.Item;
import com.konsol.core.domain.playstation.PlaystationDevice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Item entity.
 */
@Repository
public interface ItemRepository extends MongoRepository<Item, String> {
    @Query("{}")
    Page<Item> findAllWithEagerRelationshipsAndBuildInIsFalse(Pageable pageable);

    @Query("{'id': ?0}")
    Optional<Item> findOneWithEagerRelationships(String id);

    Optional<Item> findOneByPk(int pk);

    List<Item> findAllByCategory(String category);

    long countByCategoryAndBuildInIsFalse(String category);
    long countByCategoryAndBuildInIsTrue(String category);

    Page<Item> findAllByBuildInIsFalse(Pageable pageable);

    Optional<Item> findByVarRefId(String id);
}
