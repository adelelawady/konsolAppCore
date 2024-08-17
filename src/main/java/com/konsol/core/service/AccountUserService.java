package com.konsol.core.service;

import com.konsol.core.domain.AccountUser;
import com.konsol.core.service.api.dto.*;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.konsol.core.domain.AccountUser}.
 */
public interface AccountUserService {
    /**
     * Save a accountUser.
     *
     * @param accountUserDTO the entity to save.
     * @return the persisted entity.
     */
    AccountUserDTO save(AccountUserDTO accountUserDTO);
    AccountUser save(AccountUser accountUser);

    /**
     * create a accountUser.
     *
     * @param createAccountUserDTO the entity to save.
     * @return the persisted entity.
     */
    AccountUserDTO create(CreateAccountUserDTO createAccountUserDTO);

    /**
     * Partially updates a accountUser.
     *
     * @param accountUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AccountUserDTO> update(AccountUserDTO accountUserDTO);

    /**
     * Get all the accountUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccountUserDTO> findAll(Pageable pageable);

    /**
     * Get the "id" accountUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AccountUserDTO> findOne(String id);

    Optional<AccountUser> findOneDomain(String id);

    /**
     * Delete the "id" accountUser.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * search accountUser daftar
     * @param accountUserSearchModel model holds all search model fields
     * @return AccountUserContainer List of Account User [result]
]     */
    AccountUserContainer accountUserSearchPaginate(AccountUserSearchModel accountUserSearchModel);

    void addAccountBalance(String accountId, BigDecimal value);

    void subtractAccountBalance(String accountId, BigDecimal value);
}
