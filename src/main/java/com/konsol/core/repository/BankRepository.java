package com.konsol.core.repository;

import com.konsol.core.domain.Bank;
import com.konsol.core.domain.Settings;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Bank entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BankRepository extends MongoRepository<Bank, String> {
    Optional<Bank> findFirstByOrderById();
}
