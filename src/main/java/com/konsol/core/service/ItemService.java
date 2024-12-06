package com.konsol.core.service;

import com.konsol.core.domain.Item;
import com.konsol.core.domain.ItemUnit;
import com.konsol.core.service.api.dto.*;
import com.konsol.core.service.dto.ItemAnalysisDTO;
import com.konsol.core.service.mapper.ItemMapper;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    ItemDTO save(Item item);

    /**
     * create a item.
     *
     * @param itemDTO the entity to create.
     * @return the persisted entity.
     */
    ItemDTO create(ItemDTO itemDTO);

    /**
     * checks all items unit created for this new item and make sure
     * it has only one basic itemUnit created
     *
     * @param itemDTO new item to check for
     */
    void validateNewItemUnits(ItemDTO itemDTO);

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
     * Get all the items.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */

    /**
     * Get all the items. with search sort pagination options
     * @param paginationSearchModel the pagination search and sort  information.
     * @return ItemViewDTOContainer contains list of ViewDTO
     */
    ItemViewDTOContainer findAllItemsViewDTO(PaginationSearchModel paginationSearchModel);

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
     * Get the "id" item.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Item> findOneById(String id);

    ItemMapper getMapper();

    /**
     * Delete the "id" item.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * Delete the units created for item.
     *
     * @param id the id of the entity.
     */
    void deleteItemUnits(String id);

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

    /**
     * Delete the "id" itemUnit.
     *
     * @param id the id of the entity.
     */
    void deleteUnitItemById(String id);

    Optional<ItemUnit> getUnitItemById(String id);

    /**
     * get all item available units
     * @param id item id
     * @return list of {@link ItemUnitDTO} found for this item
     */
    List<ItemUnitDTO> getItemUnits(String id);
    /***
     * TODO advanced options for single item :  round total , math , store options unlimited store warn etc...
     */

    ItemAnalysisDTO analyzeItem(String itemId, String storeId, Instant startDate, Instant endDate);

    ChartDataContainer getSalesChartData(String itemId, Instant startDate, Instant endDate);
}
