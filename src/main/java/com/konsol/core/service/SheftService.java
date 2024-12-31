package com.konsol.core.service;

import com.konsol.core.domain.Sheft;
import com.konsol.core.service.api.dto.SheftDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.konsol.core.domain.Sheft}.
 */
public interface SheftService {
    /**
     * Save a sheft.
     *
     * @param sheftDTO the entity to save.
     * @return the persisted entity.
     */
    SheftDTO save(SheftDTO sheftDTO);

    /**
     * Updates a sheft.
     *
     * @param sheftDTO the entity to update.
     * @return the persisted entity.
     */
    SheftDTO update(SheftDTO sheftDTO);

    /**
     * Partially updates a sheft.
     *
     * @param sheftDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SheftDTO> partialUpdate(SheftDTO sheftDTO);

    /**
     * Get all the shefts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SheftDTO> findAll(Pageable pageable);

    /**
     * Get the "id" sheft.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SheftDTO> findOne(String id);

    /**
     * Delete the "id" sheft.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    Sheft startSheft();

    SheftDTO getCurrentSheft();

    void endSheft();

    void calculateSheft(String id);
}
