package com.konsol.core.repository;

import com.konsol.core.domain.Item;
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
    Page<Item> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Item> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Item> findOneWithEagerRelationships(String id);

    List<Item> findAllDistinctByCategoryNotIn(List<String> categories);

    Optional<Item> findOneByPk(int pk);

    List<Item> findAllByCategory(String category);
}
