package com.konsol.core.service.impl;

import com.konsol.core.domain.Invoice;
import com.konsol.core.domain.InvoiceItem;
import com.konsol.core.domain.Item;
import com.konsol.core.domain.Store;
import com.konsol.core.domain.StoreItem;
import com.konsol.core.domain.enumeration.InvoiceKind;
import com.konsol.core.repository.InvoiceItemRepository;
import com.konsol.core.repository.InvoiceRepository;
import com.konsol.core.repository.MoneyRepository;
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
import com.konsol.core.web.rest.api.errors.StoreDeletionException;
import com.konsol.core.web.rest.api.errors.StoreNotFoundException;
import com.mongodb.client.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final MoneyRepository moneyRepository;

    public StoreServiceImpl(
        StoreRepository storeRepository,
        StoreItemRepository storeItemRepository,
        StoreMapper storeMapper,
        StoreItemMapper storeItemMapper,
        ItemService itemService,
        MongoTemplate mongoTemplate,
        InvoiceRepository invoiceRepository,
        InvoiceItemRepository invoiceItemRepository,
        MoneyRepository moneyRepository
    ) {
        this.storeRepository = storeRepository;
        this.storeItemRepository = storeItemRepository;
        this.storeMapper = storeMapper;
        this.storeItemMapper = storeItemMapper;
        this.itemService = itemService;
        this.mongoTemplate = mongoTemplate;
        this.invoiceRepository = invoiceRepository;
        this.invoiceItemRepository = invoiceItemRepository;
        this.moneyRepository = moneyRepository;
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
    public Optional<Store> findOneDomain(String id) {
        return storeRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Store : {}", id);
        if (invoiceRepository.existsByStoreId(id)) {
            throw new StoreDeletionException("Cannot delete store with ID " + id + ": There are associated invoices.");
        }
        if (!storeItemRepository.findAllByStoreId(id).isEmpty()) {
            throw new StoreDeletionException("Cannot delete store with ID " + id + ": There are associated StoreItems.");
        }

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
        if (item.isEmpty()) {
            throw new ItemNotFoundException(String.format("الصنف {0} غير متاح لتعديل سجلات المخازن", ItemId));
        }

        Item item1 = item.get();
        item1.qty(getItemQty(ItemId));
        itemService.save(item1);
    }

    @Override
    public boolean checkNotItemQtyAvailable(String ItemId, BigDecimal qty) {
        Optional<Item> isCheckQty = itemService.findOneById(ItemId);
        if (isQuantityCheckRequired(isCheckQty)) {
            return false;
        }
        return getItemQty(ItemId).compareTo(qty) < 0;
    }

    private boolean isQuantityCheckRequired(Optional<Item> optionalItem) {
        return optionalItem.isPresent() && !optionalItem.get().isCheckQty();
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

        if (checkNotItemQtyAvailable(ItemId, qty)) {
            throw new ItemQtyException("مشكلة ف كم��ة الصنف", "لا يوجد ما يكفي من الصنف ف المخازن");
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

    @Override
    public Optional<Store> findFirstByOrderById() {
        return storeRepository.findFirstByOrderById();
    }

    @Override
    public List<StoreItem> getStoresItemsForStore(String id, PaginationSearchModel paginationSearchModel) {
        // Validate store exists
        Optional<Store> store = storeRepository.findById(id);
        if (!store.isPresent()) {
            throw new StoreNotFoundException(String.format("Store with id %s not found", id));
        }

        Query query = new Query();
        Query countQuery = new Query();

        // Add store filter
        query.addCriteria(Criteria.where("store.$id").is(new ObjectId(id)));
        countQuery.addCriteria(Criteria.where("store.$id").is(new ObjectId(id)));

        // Add pagination
        if (paginationSearchModel.getPage() != null && paginationSearchModel.getSize() != null && paginationSearchModel.getSize() > 0) {
            Pageable pageable = PageRequest.of(paginationSearchModel.getPage(), paginationSearchModel.getSize());
            query.with(pageable);
        }

        // Add sorting
        if (paginationSearchModel.getSortField() != null && !paginationSearchModel.getSortField().isEmpty()) {
            if (paginationSearchModel.getSortOrder() != null && !paginationSearchModel.getSortOrder().isEmpty()) {
                switch (paginationSearchModel.getSortOrder().toLowerCase()) {
                    case "asc":
                        {
                            query.with(Sort.by(Sort.Direction.ASC, paginationSearchModel.getSortField()));
                            break;
                        }
                    case "desc":
                        {
                            query.with(Sort.by(Sort.Direction.DESC, paginationSearchModel.getSortField()));
                            break;
                        }
                }
            } else {
                query.with(Sort.by(Sort.Direction.DESC, paginationSearchModel.getSortField()));
            }
        } else {
            query.with(Sort.by(Sort.Direction.ASC, "item.$id"));
        }

        // Add search criteria
        if (paginationSearchModel.getSearchText() != null && !paginationSearchModel.getSearchText().isEmpty()) {
            ObjectId objectId = null;
            if (ObjectId.isValid(paginationSearchModel.getSearchText())) {
                objectId = new ObjectId(paginationSearchModel.getSearchText());
            }

            List<Criteria> searchCriteria = new ArrayList<>();

            if (objectId != null) {
                searchCriteria.add(Criteria.where("_id").is(objectId));
                searchCriteria.add(Criteria.where("item.$id").is(objectId));
            }

            searchCriteria.add(Criteria.where("qty").regex(paginationSearchModel.getSearchText(), "i"));

            // Add the search criteria to the query
            if (!searchCriteria.isEmpty()) {
                query.addCriteria(new Criteria().orOperator(searchCriteria.toArray(new Criteria[0])));
                countQuery.addCriteria(new Criteria().orOperator(searchCriteria.toArray(new Criteria[0])));
            }
        }

        log.debug("Executing query to get store items for store: {}", id);
        return mongoTemplate.find(query, StoreItem.class);
    }

    @Override
    public StoreItemDTO setStoreItem(StoreItemIdOnlyDTO storeItemIdOnlyDTO) {
        return setStoreItem(storeItemIdOnlyDTO, false);
    }

    /**
     * creates if not exsits or updates exsitsing store item  for selected item and store
     *
     * @param storeItemIdOnlyDTO store item contains item.id and store.id and qty required (required)
     * @return OK (status code 200)
     * @see StoresApi#setStoreItem
     */
    @Override
    public StoreItemDTO setStoreItem(StoreItemIdOnlyDTO storeItemIdOnlyDTO, boolean createAdjustInvoice) {
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
        StoreItem storeItem;
        BigDecimal oldQty = BigDecimal.ZERO;

        if (storeItemOptional.isPresent()) {
            // Update
            storeItem = storeItemOptional.get();
            oldQty = storeItem.getQty();
            storeItem.setQty(storeItemIdOnlyDTO.getQty());
            storeItem = storeItemRepository.save(storeItem);
        } else {
            // Create new
            storeItem = new StoreItem();
            storeItem.setItem(item.get());
            storeItem.setStore(store.get());
            storeItem.setQty(storeItemIdOnlyDTO.getQty());
            storeItem = storeItemRepository.save(storeItem);
        }

        // Create adjustment invoice if requested
        if (createAdjustInvoice) {
            BigDecimal diffQty = storeItemIdOnlyDTO.getQty().subtract(oldQty);

            if (diffQty.compareTo(BigDecimal.ZERO) != 0) {
                Invoice adjustInvoice = new Invoice();
                adjustInvoice.setKind(InvoiceKind.ADJUST);
                adjustInvoice.setStore(store.get());

                // Add adjustment details including user info
                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                String details = String.format(
                    "Quantity adjusted from %s to %s (diff: %s) by user: %s",
                    oldQty,
                    storeItemIdOnlyDTO.getQty(),
                    diffQty,
                    username
                );
                adjustInvoice.setDetails(details);

                // Create invoice item
                InvoiceItem invoiceItem = new InvoiceItem();
                invoiceItem.setItem(item.get());
                invoiceItem.setUnit("-");

                // Set quantities based on whether items were added or removed
                if (diffQty.compareTo(BigDecimal.ZERO) > 0) {
                    invoiceItem.setQtyIn(diffQty);
                    invoiceItem.setUnitQtyIn(diffQty.multiply(new BigDecimal(1)));
                    invoiceItem.setQtyOut(BigDecimal.ZERO);
                    invoiceItem.setUnitQtyOut(BigDecimal.ZERO);
                    invoiceItem.setUserQty(diffQty);
                } else {
                    invoiceItem.setQtyIn(BigDecimal.ZERO);
                    invoiceItem.setUnitQtyIn(BigDecimal.ZERO);
                    invoiceItem.setQtyOut(diffQty.abs());
                    invoiceItem.setUnitQtyOut(diffQty.abs().multiply(new BigDecimal(1)));
                    invoiceItem.setUserQty(diffQty.abs());
                }

                // Save the invoice first to get its ID
                adjustInvoice = invoiceRepository.save(adjustInvoice);

                // Link invoice item to invoice
                invoiceItem.setInvoiceId(adjustInvoice.getId());

                // Add invoice item to invoice's items set
                Set<InvoiceItem> invoiceItems = new HashSet<>();
                invoiceItems.add(invoiceItem);
                adjustInvoice.setInvoiceItems(invoiceItems);

                // Save both invoice item and updated invoice
                invoiceItemRepository.save(invoiceItem);
                invoiceRepository.save(adjustInvoice);
            }
        }
        // Update item QTY
        UpdateItemQty(storeItemIdOnlyDTO.getItemId());

        return storeItemMapper.toDto(storeItem);
    }
}
