package com.konsol.core.service;

import com.konsol.core.domain.Store;
import com.konsol.core.service.api.dto.StoreDTO;
import com.konsol.core.service.api.dto.StoreItemDTO;
import com.konsol.core.service.api.dto.StoreItemIdOnlyDTO;
import com.konsol.core.service.api.dto.StoreNameDTO;
import com.konsol.core.web.api.StoresApi;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

/**
 * Service Interface for managing {@link com.konsol.core.domain.Store}.
 */
public interface StoreService {
    /**
     * Save a store.
     *
     * @param storeDTO the entity to save.
     * @return the persisted entity.
     */
    StoreDTO save(StoreDTO storeDTO);

    /**
     * Partially updates a store.
     *
     * @param storeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StoreDTO> update(StoreDTO storeDTO);

    /**
     * Get all the stores.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StoreDTO> findAll(Pageable pageable);

    /**
     * Get all the stores with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StoreDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" store.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StoreDTO> findOne(String id);

    /**
     * Get the "id" store.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Store> findOneDomain(String id);

    /**
     * Delete the "id" store.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * get all stores names
     * @return List of StoresNameDTO contains id , name
     */
    List<StoreNameDTO> getAllStoresNames();

    /**
     * GET /stores/item/{id}/storeItems
     * return all store items qty in all stores item availabe in with item id
     *
     * @param id  (required)
     * @return OK (status code 200)
     * @see StoresApi#getAllStoresItemsForItem
     */
    List<StoreItemDTO> getAllStoresItemsForItem(String id);

    /**
     * return all store items qty in all stores with item id
     *
     * @param id  (required)
     * @return OK (status code 200)
     * @see StoresApi#getAllStoresItemsInAllStoresForItem
     */
    List<StoreItemDTO> getAllStoresItemsInAllStoresForItem(String id);

    /**
     * creates if not exsits or updates exsitsing store item  for selected item and store
     *
     * @param storeItemIdOnlyDTO store item contains item.id and store.id and qty required (required)
     * @return OK (status code 200)
     * @see StoresApi#setStoreItem
     */
    StoreItemDTO setStoreItem(StoreItemIdOnlyDTO storeItemIdOnlyDTO);

    /**
     * update item qty based on all store qty availabe for item
     * @param ItemId item id to update
     */
    void UpdateItemQty(String ItemId);

    boolean checkItemQtyAvailable(String ItemId, BigDecimal qty);

    BigDecimal getItemQty(String ItemId);

    void subtractItemQtyFromStores(String ItemId, BigDecimal qty);

    void addItemQtyToStores(String ItemId, BigDecimal qty, String storeId);

    /**
     * Get the first store ordered by id.
     *
     * @return the entity.
     */
    Optional<Store> findFirstByOrderById();
}
