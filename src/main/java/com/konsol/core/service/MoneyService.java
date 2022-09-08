package com.konsol.core.service;

import com.konsol.core.service.dto.MoneyDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.konsol.core.domain.Money}.
 */
public interface MoneyService {
    /**
     * Save a money.
     *
     * @param moneyDTO the entity to save.
     * @return the persisted entity.
     */
    MoneyDTO save(MoneyDTO moneyDTO);

    /**
     * Updates a money.
     *
     * @param moneyDTO the entity to update.
     * @return the persisted entity.
     */
    MoneyDTO update(MoneyDTO moneyDTO);

    /**
     * Partially updates a money.
     *
     * @param moneyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MoneyDTO> partialUpdate(MoneyDTO moneyDTO);

    /**
     * Get all the monies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MoneyDTO> findAll(Pageable pageable);

    /**
     * Get the "id" money.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MoneyDTO> findOne(String id);

    /**
     * Delete the "id" money.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
