package com.konsol.core.repository;

import com.konsol.core.domain.Sheft;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Sheft entity.
 */
@Repository
public interface SheftRepository extends MongoRepository<Sheft, String> {
    Optional<Sheft> findByActiveTrue();



    Page<Sheft> findAllByOrderByEndTimeDesc(Pageable pageable);

    Page<Sheft> findAllByOrderByStartTimeDesc(Pageable pageable);



}
