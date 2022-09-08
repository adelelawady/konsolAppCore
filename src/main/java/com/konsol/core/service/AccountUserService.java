package com.konsol.core.service;

import com.konsol.core.service.dto.AccountUserDTO;
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

    /**
     * Updates a accountUser.
     *
     * @param accountUserDTO the entity to update.
     * @return the persisted entity.
     */
    AccountUserDTO update(AccountUserDTO accountUserDTO);

    /**
     * Partially updates a accountUser.
     *
     * @param accountUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AccountUserDTO> partialUpdate(AccountUserDTO accountUserDTO);

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

    /**
     * Delete the "id" accountUser.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
