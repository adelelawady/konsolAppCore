package com.konsol.core.service.impl;

import com.konsol.core.domain.Item;
import com.konsol.core.domain.Store;
import com.konsol.core.domain.StoreItem;
import com.konsol.core.repository.StoreItemRepository;
import com.konsol.core.repository.StoreRepository;
import com.konsol.core.service.ItemService;
import com.konsol.core.service.StoreService;
import com.konsol.core.service.api.dto.*;
import com.konsol.core.service.mapper.StoreItemMapper;
import com.konsol.core.service.mapper.StoreMapper;
import com.konsol.core.web.api.StoresApi;
import com.konsol.core.web.rest.api.errors.ItemNotFoundException;
import com.konsol.core.web.rest.api.errors.ItemQtyException;
import com.konsol.core.web.rest.api.errors.StoreNotFoundException;
import com.mongodb.client.*;
import com.mongodb.client.FindIterable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.bson.Document;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Store}.
 */
@Service
public class StoreServiceImpl implements StoreService {

    private final Logger log = LoggerFactory.getLogger(StoreServiceImpl.class);

    private final StoreRepository storeRepository;

    private final StoreItemRepository storeItemRepository;
    private final StoreMapper storeMapper;
    private final StoreItemMapper storeItemMapper;

    private final ItemService itemService;

    private final MongoTemplate mongoTemplate;

    public StoreServiceImpl(
        StoreRepository storeRepository,
        StoreItemRepository storeItemRepository,
        StoreMapper storeMapper,
        StoreItemMapper storeItemMapper,
        ItemService itemService,
        MongoTemplate mongoTemplate
    ) {
        this.storeRepository = storeRepository;
        this.storeItemRepository = storeItemRepository;
        this.storeMapper = storeMapper;
        this.storeItemMapper = storeItemMapper;
        this.itemService = itemService;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public StoreDTO save(StoreDTO storeDTO) {
        log.debug("Request to save Store : {}", storeDTO);
        Store store = storeMapper.toEntity(storeDTO);
        store = storeRepository.save(store);
        return storeMapper.toDto(store);
    }

    @Override
    public Optional<StoreDTO> update(StoreDTO storeDTO) {
        log.debug("Request to partially update Store : {}", storeDTO);

        return storeRepository
            .findById(storeDTO.getId())
            .map(existingStore -> {
                storeMapper.partialUpdate(existingStore, storeDTO);

                return existingStore;
            })
            .map(storeRepository::save)
            .map(storeMapper::toDto);
    }

    @Override
    public Page<StoreDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Stores");
        return storeRepository.findAll(pageable).map(storeMapper::toDto);
    }

    public Page<StoreDTO> findAllWithEagerRelationships(Pageable pageable) {
        return storeRepository.findAllWithEagerRelationships(pageable).map(storeMapper::toDto);
    }

    @Override
    public Optional<StoreDTO> findOne(String id) {
        log.debug("Request to get Store : {}", id);
        return storeRepository.findOneWithEagerRelationships(id).map(storeMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Store : {}", id);
        storeRepository.deleteById(id);
    }

    /**
     * get all stores names
     *
     * @return List of StoresNameDTO contains id , name
     */
    @Override
    public List<StoreNameDTO> getAllStoresNames() {
        return storeRepository.findAll().stream().map(storeMapper::toStoreNameDTO).collect(Collectors.toList());
    }

    /**
     * GET /stores/item/{id}/storeItems
     * return all store items qty in all stores item availabe in with item id
     *
     * @param id (required)
     * @return OK (status code 200)
     * @see StoresApi#getAllStoresItemsForItem
     */
    @Override
    public List<StoreItemDTO> getAllStoresItemsForItem(String id) {
        return storeItemRepository.findAllByItemId(id).stream().map(storeItemMapper::toDto).collect(Collectors.toList());
    }

    /**
     * GET /stores/item/{id}/storeItems/all
     * return all store items qty in all stores with item id
     *
     * @param id (required)
     * @return OK (status code 200)
     * @see StoresApi#getAllStoresItemsInAllStoresForItem
     */
    @Override
    public List<StoreItemDTO> getAllStoresItemsInAllStoresForItem(String id) {
        List<StoreItemDTO> foundList = storeItemRepository
            .findAllByItemId(id)
            .stream()
            .map(storeItemMapper::toDto)
            .collect(Collectors.toList());

        /**
         * add missing store items for other stores with qty 0
         */

        Set<String> storeIdsFound = foundList.stream().map(storeItem -> storeItem.getStore().getId()).collect(Collectors.toSet());

        List<Store> notIncludedStores = storeRepository.findAllByIdNotIn(new ArrayList<>(storeIdsFound));

        Item mainItem = itemService.findOneById(id).orElse(null);

        notIncludedStores.forEach(storeNotIncluded -> {
            StoreItemDTO storeItemDTO = new StoreItemDTO();
            storeItemDTO.setItem(itemService.getMapper().toSimpleDTO(mainItem));
            storeItemDTO.setStore(storeMapper.toStoreNameDTO(storeNotIncluded));
            storeItemDTO.setQty(new BigDecimal(0));
            storeItemDTO.setId(null);
            foundList.add(storeItemDTO);
        });
        return foundList;
    }

    /**
     * update item qty based on all store qty availabe for item
     *
     * @param ItemId item id to update
     */
    @Override
    public void UpdateItemQty(String ItemId) {
        /**
         * get and check item
         */

        Optional<Item> item = itemService.findOneById(ItemId);
        if (!item.isPresent()) {
            throw new ItemNotFoundException(String.format("الصنف {0} غير متاح لتعديل سجلات المخازن", ItemId));
        }

        Item item1 = item.get();
        item1.qty(getItemQty(ItemId));
        itemService.save(item1);
    }

    @Override
    public boolean checkItemQtyAvailable(String ItemId, BigDecimal qty) {
        return getItemQty(ItemId).compareTo(qty) >= 0;
        //  return itemService.findOneById(ItemId).orElseGet(null).getQty().compareTo(qty)<1;
    }

    @Override
    public BigDecimal getItemQty(String ItemId) {
        MongoCollection<Document> collection = mongoTemplate.getCollection("store_items");

        AggregateIterable<Document> result = collection.aggregate(
            Arrays.asList(
                new Document("$match", new Document("item.$id", new ObjectId(ItemId))),
                new Document(
                    "$group",
                    new Document("_id", "$id")
                        .append(
                            "total",
                            new Document(
                                "$sum",
                                new Document(
                                    "$convert",
                                    new Document("input", "$qty").append("to", "decimal").append("onError", "").append("onNull", "")
                                )
                            )
                        )
                        .append("count", new Document("$sum", 1L))
                )
            )
        );

        try {
            MongoCursor<Document> iterator = result.iterator();
            org.bson.types.Decimal128 total = (org.bson.types.Decimal128) iterator.next().get("total");
            return total.bigDecimalValue();
        } catch (Exception e) {
            return new BigDecimal(0);
        }
    }

    @Override
    public void subtractItemQtyFromStores(String ItemId, BigDecimal qty) {
        Optional<Item> item = itemService.findOneById(ItemId);
        if (item.isEmpty()) {
            throw new ItemNotFoundException(String.format("الصنف {0} غير متاح لتعديل سجلات المخازن", ItemId));
        }

        if (!checkItemQtyAvailable(ItemId, qty)) {
            throw new ItemQtyException("مشكلة ف كمية الصنف", "لا يوجد ما يكفي من الصنف ف المخازن");
        }

        for (StoreItemDTO storeItemDTO : this.getAllStoresItemsForItem(ItemId)) {
            if (storeItemDTO.getQty().compareTo(qty) >= 0) {
                storeItemDTO.setQty(storeItemDTO.getQty().subtract(qty));

                this.setStoreItem(storeItemMapper.toStoreItemIdOnlyDTO(storeItemDTO));
                break;
            } else {
                qty = qty.subtract(storeItemDTO.getQty());
                storeItemDTO.setQty(new BigDecimal(0));
                this.setStoreItem(storeItemMapper.toStoreItemIdOnlyDTO(storeItemDTO));
            }
        }
    }

    /**
     * @param ItemId
     * @param qty
     * @param storeId
     */
    @Override
    public void addItemQtyToStores(String ItemId, BigDecimal qty, String storeId) {
        Optional<Item> item = itemService.findOneById(ItemId);
        if (!item.isPresent()) {
            throw new ItemNotFoundException(String.format("الصنف {0} غير متاح لتعديل سجلات المخازن", ItemId));
        }

        if (storeId != null) {
            Optional<Store> storeOptional = storeRepository.findById(ItemId);

            if (storeOptional.isPresent()) {
                Optional<StoreItem> storeItemOptional = storeItemRepository.findOneByItemIdAndStoreId(
                    item.get().getId(),
                    storeOptional.get().getId()
                );

                if (storeItemOptional.isPresent()) {
                    StoreItem storeItem = storeItemOptional.get();

                    StoreItemIdOnlyDTO storeItemDTO = new StoreItemIdOnlyDTO();
                    storeItemDTO.setItemId(item.get().getId());
                    storeItemDTO.setStoreId(storeId);
                    // storeItemDTO.setQty(storeItem.getQty().add(qty));
                    BigDecimal qtyToSave = storeItemDTO.getQty().add(qty, MathContext.UNLIMITED);
                    storeItemDTO.setQty(qtyToSave);
                    this.setStoreItem(storeItemDTO);
                    return;
                }
            }
        }

        for (StoreItemDTO storeItemDTO : this.getAllStoresItemsInAllStoresForItem(ItemId)) {
            BigDecimal qtyToSave = storeItemDTO.getQty().add(qty, MathContext.UNLIMITED);
            storeItemDTO.setQty(qtyToSave);
            this.setStoreItem(storeItemMapper.toStoreItemIdOnlyDTO(storeItemDTO));
            break;
        }
    }

    /**
     * creates if not exsits or updates exsitsing store item  for selected item and store
     *
     * @param storeItemIdOnlyDTO store item contains item.id and store.id and qty required (required)
     * @return OK (status code 200)
     * @see StoresApi#setStoreItem
     */
    @Override
    public StoreItemDTO setStoreItem(StoreItemIdOnlyDTO storeItemIdOnlyDTO) {
        /**
         * get and check item
         */

        Optional<Item> item = itemService.findOneById(storeItemIdOnlyDTO.getItemId());
        if (!item.isPresent()) {
            throw new ItemNotFoundException(String.format("الصنف {0} غير متاح لتعديل سجلات المخازن", storeItemIdOnlyDTO.getItemId()));
        }

        /**
         * get and check store
         */
        Optional<Store> store = this.storeRepository.findById(storeItemIdOnlyDTO.getStoreId());

        if (!store.isPresent()) {
            throw new StoreNotFoundException(String.format("المخزن {0} غير متاح لتعديل سجلاته", storeItemIdOnlyDTO.getStoreId()));
        }
        /**
         * get and check store item
         */
        Optional<StoreItem> storeItemOptional = storeItemRepository.findOneByItemIdAndStoreId(item.get().getId(), store.get().getId());
        if (storeItemOptional.isPresent()) {
            /**
             * update
             */
            StoreItem storeItem = storeItemOptional.get();
            storeItem.setQty(storeItemIdOnlyDTO.getQty());
            storeItem = storeItemRepository.save(storeItem);

            /**
             * Update item QTY
             */
            UpdateItemQty(storeItemIdOnlyDTO.getItemId());

            return storeItemMapper.toDto(storeItem);
        } else {
            /**
             * create
             */
            StoreItem storeItem = new StoreItem();
            storeItem.setItem(item.get());
            storeItem.setStore(store.get());
            storeItem.setQty(storeItemIdOnlyDTO.getQty());
            storeItem = storeItemRepository.save(storeItem);

            /**
             * Update item QTY
             */
            UpdateItemQty(storeItemIdOnlyDTO.getItemId());

            return storeItemMapper.toDto(storeItem);
        }
    }
}
