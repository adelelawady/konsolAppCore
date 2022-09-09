package com.konsol.core.service;

import com.konsol.core.service.api.dto.ItemUnitDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.konsol.core.domain.ItemUnit}.
 */
public interface ItemUnitService {
    /**
     * Save a itemUnit.
     *
     * @param itemUnitDTO the entity to save.
     * @return the persisted entity.
     */
    ItemUnitDTO save(ItemUnitDTO itemUnitDTO);

    /**
     * Updates a itemUnit.
     *
     * @param itemUnitDTO the entity to update.
     * @return the persisted entity.
     */
    ItemUnitDTO update(ItemUnitDTO itemUnitDTO);

    /**
     * Partially updates a itemUnit.
     *
     * @param itemUnitDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ItemUnitDTO> partialUpdate(ItemUnitDTO itemUnitDTO);

    /**
     * Get all the itemUnits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ItemUnitDTO> findAll(Pageable pageable);

    /**
     * Get the "id" itemUnit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ItemUnitDTO> findOne(String id);

    /**
     * Delete the "id" itemUnit.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
