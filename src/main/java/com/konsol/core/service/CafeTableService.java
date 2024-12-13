package com.konsol.core.service;

import com.konsol.core.service.dto.CafeTableDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.konsol.core.domain.CafeTable}.
 */
public interface CafeTableService {
    /**
     * Save a cafeTable.
     *
     * @param cafeTableDTO the entity to save.
     * @return the persisted entity.
     */
    CafeTableDTO save(CafeTableDTO cafeTableDTO);

    /**
     * Updates a cafeTable.
     *
     * @param cafeTableDTO the entity to update.
     * @return the persisted entity.
     */
    CafeTableDTO update(CafeTableDTO cafeTableDTO);

    /**
     * Partially updates a cafeTable.
     *
     * @param cafeTableDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CafeTableDTO> partialUpdate(CafeTableDTO cafeTableDTO);

    /**
     * Get all the cafeTables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CafeTableDTO> findAll(Pageable pageable);

    /**
     * Get the "id" cafeTable.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CafeTableDTO> findOne(String id);

    /**
     * Delete the "id" cafeTable.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
