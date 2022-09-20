package com.konsol.core.service;

import com.konsol.core.service.api.dto.StoreItemDTO;
import com.konsol.core.web.api.StoresApi;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.konsol.core.domain.StoreItem}.
 */
public interface StoreItemService {
    /**
     * Save a storeItem.
     *
     * @param storeItemDTO the entity to save.
     * @return the persisted entity.
     */
    StoreItemDTO save(StoreItemDTO storeItemDTO);

    /**
     * Updates a storeItem.
     *
     * @param storeItemDTO the entity to update.
     * @return the persisted entity.
     */
    StoreItemDTO update(StoreItemDTO storeItemDTO);

    /**
     * Partially updates a storeItem.
     *
     * @param storeItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StoreItemDTO> partialUpdate(StoreItemDTO storeItemDTO);

    /**
     * Get all the storeItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StoreItemDTO> findAll(Pageable pageable);

    /**
     * Get the "id" storeItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StoreItemDTO> findOne(String id);

    /**
     * Delete the "id" storeItem.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
