package com.konsol.core.service;

import com.konsol.core.service.api.dto.CategoryItem;
import com.konsol.core.service.api.dto.ItemDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

/**
 * Service Interface for managing {@link com.konsol.core.domain.Item}.
 */
public interface ItemService {
    /**
     * Save a item.
     *
     * @param itemDTO the entity to save.
     * @return the persisted entity.
     */
    ItemDTO save(ItemDTO itemDTO);

    /**
     * Updates a item.
     *
     * @param itemDTO the entity to update.
     * @return the persisted entity.
     */
    ItemDTO update(ItemDTO itemDTO);

    /**
     * Partially updates a item.
     *
     * @param itemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ItemDTO> partialUpdate(ItemDTO itemDTO);

    /**
     * Get all the items.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ItemDTO> findAll(Pageable pageable);

    /**
     * Get all the items with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ItemDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" item.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ItemDTO> findOne(String id);

    /**
     * Delete the "id" item.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * get all items categories in items list
     * @return list of string contains all categories distinct
     */
    List<CategoryItem> getAllItemCategories();

    /**
     * Get the "pk" item.
     *
     * @param pk the pk of the entity.
     * @return the entity.
     */
    Optional<ItemDTO> findOneByPk(String pk);

    Optional<ItemDTO> getItemBeforeItemById(String id);

    Optional<ItemDTO> getItemAfterItemById(String id);
}
