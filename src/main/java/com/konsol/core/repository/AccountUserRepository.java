package com.konsol.core.repository;

import com.konsol.core.domain.AccountUser;
import com.konsol.core.domain.Money;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the AccountUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountUserRepository extends MongoRepository<AccountUser, String> {
    List<AccountUser> findAllByIdIn(List<String> ids);
}
