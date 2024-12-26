package com.konsol.core.service.impl;

import com.konsol.core.domain.Item;
import com.konsol.core.domain.ItemPriceOptions;
import com.konsol.core.domain.ItemUnit;
import com.konsol.core.domain.Pk;
import com.konsol.core.domain.enumeration.PkKind;
import com.konsol.core.repository.InvoiceItemRepository;
import com.konsol.core.repository.ItemRepository;
import com.konsol.core.repository.ItemUnitRepository;
import com.konsol.core.service.ItemService;
import com.konsol.core.service.ItemUnitService;
import com.konsol.core.service.PkService;
import com.konsol.core.service.api.dto.*;
import com.konsol.core.service.dto.ChartDataDTO;
import com.konsol.core.service.dto.ItemAnalysisDTO;
import com.konsol.core.service.mapper.ItemMapper;
import com.konsol.core.service.mapper.ItemUnitMapper;
import com.konsol.core.web.rest.api.errors.AccountDeletionException;
import com.konsol.core.web.rest.api.errors.ItemDeletionException;
import com.konsol.core.web.rest.api.errors.ItemNotFoundException;
import com.konsol.core.web.rest.api.errors.ItemUnitException;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
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
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Item}.
 */
@Service
public class ItemServiceImpl implements ItemService {

    private final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    private final ItemUnitRepository itemUnitRepository;

    private final ItemUnitMapper itemUnitMapper;

    private final MongoTemplate mongoTemplate;

    private final PkService pkService;

    private final ItemUnitService itemUnitService;
    private final InvoiceItemRepository invoiceItemRepository;

    public ItemServiceImpl(
        ItemRepository itemRepository,
        ItemMapper itemMapper,
        ItemUnitRepository itemUnitRepository,
        MongoTemplate mongoTemplate,
        PkService pkService,
        ItemUnitService itemUnitService,
        ItemUnitMapper itemUnitMapper,
        InvoiceItemRepository invoiceItemRepository
    ) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.itemUnitRepository = itemUnitRepository;
        this.mongoTemplate = mongoTemplate;
        this.pkService = pkService;
        this.itemUnitService = itemUnitService;
        this.itemUnitMapper = itemUnitMapper;
        this.invoiceItemRepository = invoiceItemRepository;
    }

    @Override
    public ItemDTO save(ItemDTO itemDTO) {
        Optional<Item> itemToUpdate = itemRepository.findById(itemDTO.getId());
        if (!itemToUpdate.isPresent()) {
            return null;
        }
        log.debug("Request to save Item : {}", itemDTO);
        Item item = itemMapper.toEntity(itemDTO);
        item.setItemUnits(itemToUpdate.get().getItemUnits());
        item = itemRepository.save(item);
        return itemMapper.toDto(item);
    }

    /**
     * @param item
     * @return
     */
    @Override
    public ItemDTO save(Item item) {
        return itemMapper.toDto(itemRepository.save(item));
    }

    /**
     * Creates a new item based on the provided ItemDTO.
     *
     * @param itemDTO The data transfer object containing information about the item to be created
     * @return The data transfer object representing the created item
     */
    @Override
    public ItemDTO create(ItemDTO itemDTO) {
        log.debug("Request to create Item : {}", itemDTO);

        this.validateNewItemUnits(itemDTO);

        /**
         * map itemDTO to item and saves it
         */
        Item item = itemMapper.toEntity(itemDTO);
        item = itemRepository.save(item);

        /**
         * create new pk for current item
         */
        Pk pk = pkService.generatePkEntity(PkKind.ITEM);
        item.setPk(String.valueOf(pk.getValue().intValue()));
        item = itemRepository.save(item);

        /**
         * create and add items units
         */

        if (itemDTO.getItemUnits() == null || itemDTO.getItemUnits().isEmpty()) {
            item.setItemUnits(new HashSet<>());
        } else {
            for (ItemUnitDTO itemUnitDTO : itemDTO.getItemUnits()) {
                ItemUnit itemUnit = itemUnitService.saveDomain(itemUnitDTO);
                item.getItemUnits().add(itemUnit);
            }
        }

        item = itemRepository.save(item);

        return itemMapper.toDto(item);
    }

    /**
     * Validates the item units of a given ItemDTO.
     * If the item units are null or empty, no validation is performed.
     * If the item has no basic unit, an ItemUnitException is thrown with a message in Arabic.
     * If the item has more than one basic unit, an ItemUnitException is thrown with a different message in Arabic.
     *
     * @param itemDTO The ItemDTO object to validate its item units
     * @throws ItemUnitException if the item has no basic unit or has more than one basic unit
     */
    @Override
    public void validateNewItemUnits(ItemDTO itemDTO) {
        if (itemDTO.getItemUnits() == null || itemDTO.getItemUnits().isEmpty()) {
            return;
        }

        /**
         *
         * create and add item units
         */
        boolean hasBasicUnit = false;
        boolean hasMultiBasicUnit = false;
        for (ItemUnitDTO itemUnitDTO : itemDTO.getItemUnits()) {
            if (itemUnitDTO.getBasic()) {
                if (hasBasicUnit) {
                    hasMultiBasicUnit = true;
                    break;
                }
                hasBasicUnit = true;
            }
        }

        if (!hasBasicUnit) {
            /**
             * throw must have basic unit
             */
            throw new ItemUnitException("مشكلة في وحدة الصنف", "يجب ان يحتوي الصنف علي وحدة اساسية");
        }

        if (hasMultiBasicUnit) {
            /**
             * throw must have only one basic unit
             */
            throw new ItemUnitException("مشكلة في وحدة الصنف", "يجب ان يحتوي الصنف علي وحدة اساسية واحدة فقط");
        }
    }

    /**
     * Updates an item with the information provided in the ItemDTO.
     *
     * @param itemDTO The ItemDTO containing the updated information.
     * @return The updated ItemDTO if the item exists, otherwise null.
     */
    @Override
    public ItemDTO update(ItemDTO itemDTO) {
        Optional<Item> itemToUpdate = itemRepository.findById(itemDTO.getId());
        if (!itemToUpdate.isPresent()) {
            return null;
        }
        this.validateNewItemUnits(itemDTO);

        log.debug("Request to update Item : {}", itemDTO);
        Item item = itemMapper.toEntity(itemDTO);
        item.setQty(itemToUpdate.get().getQty());
        // item.setItemUnits(itemToUpdate.get().getItemUnits());
        item = itemRepository.save(item);
        SaveItemUnits(itemDTO);
        return itemMapper.toDto(itemRepository.findById(itemDTO.getId()).get());
    }

    /**
     * Saves the item units associated with the given ItemDTO.
     *
     * This method retrieves the item by its ID from the item repository, then maps over it to update its item units.
     * If the item units in the provided ItemDTO are null or empty, it initializes a new HashSet for the item's units.
     * For each ItemUnitDTO in the ItemDTO's item units, it saves the item unit using the item unit service, sets the item ID,
     * saves the item unit again, and adds it to the item's unit collection.
     * Finally, the updated item is saved back to the repository.
     *
     * @param itemDTO The ItemDTO containing the item and its associated item units to be saved
     */
    public void SaveItemUnits(ItemDTO itemDTO) {
        itemRepository
            .findById(itemDTO.getId())
            .map(item -> {
                if (itemDTO.getItemUnits() == null || itemDTO.getItemUnits().isEmpty()) {
                    item.setItemUnits(new HashSet<>());
                } else {
                    for (ItemUnitDTO itemUnitDTO : itemDTO.getItemUnits()) {
                        ItemUnit itemUnit = itemUnitService.saveDomain(itemUnitDTO);

                        if (itemUnitDTO.getBasic() != null && itemUnitDTO.getBasic()) {
                            itemUnit.setPieces(new BigDecimal(1));
                            itemUnit.setCost(itemDTO.getCost().multiply(itemUnit.getPieces()));
                            if (itemUnit.getPrice() == null || (itemUnit.getPrice()).compareTo(new BigDecimal(0)) == 0) {
                                itemUnit.setPrice(new BigDecimal(itemDTO.getPrice1()).multiply(itemUnit.getPieces()));
                            }
                        } else {
                            if (itemUnit.getPieces() == null || (itemUnit.getPieces()).compareTo(new BigDecimal(0)) == 0) {
                                itemUnit.setPieces(new BigDecimal(1));
                            }
                            if (itemUnit.getPrice() == null || (itemUnit.getPrice()).compareTo(new BigDecimal(0)) == 0) {
                                itemUnit.setPrice(new BigDecimal(itemDTO.getPrice1()).multiply(itemUnit.getPieces()));
                            }
                            //  if (itemUnit.getPrice() == null || (itemUnit.getPrice()).compareTo(new BigDecimal(0)) == 0) {
                            itemUnit.setCost(itemDTO.getCost().multiply(itemUnit.getPieces()));
                            //  }
                        }

                        itemUnit.setItemId(itemDTO.getId());
                        itemUnit = itemUnitService.saveDomain(itemUnit);
                        item.getItemUnits().add(itemUnit);
                    }
                }
                return item;
            })
            .map(itemRepository::save);
    }

    /**
     * Partially updates an item based on the provided ItemDTO.
     *
     * @param itemDTO The ItemDTO containing the updated information.
     * @return An Optional containing the updated ItemDTO if found, otherwise empty.
     */
    @Override
    public Optional<ItemDTO> partialUpdate(ItemDTO itemDTO) {
        log.debug("Request to partially update Item : {}", itemDTO);

        this.validateNewItemUnits(itemDTO);
        SaveItemUnits(itemDTO);

        return itemRepository
            .findById(itemDTO.getId())
            .map(existingItem -> {
                /**
                 * disable changing qty and itemUnits
                 */
                itemMapper.partialUpdate(existingItem, itemDTO);
                return existingItem;
            })
            .map(itemRepository::save)
            .map(itemMapper::toDto);
    }

    @Override
    public Page<ItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Items");
        return itemRepository.findAll(pageable).map(itemMapper::toDto);
    }

    /**
     * Retrieves a paginated list of ItemViewDTOs based on the provided PaginationSearchModel.
     *
     * @param paginationSearchModel The model containing pagination information
     * @return A container holding the list of ItemViewDTOs and the total count of items
     */
    @Override
    public ItemViewDTOContainer findAllItemsViewDTO(PaginationSearchModel paginationSearchModel) {
        Query query = new Query();
        Query countQuery = new Query();
        /**
         * pagination
         */

        if (paginationSearchModel.getPage() != null && paginationSearchModel.getSize() != null && paginationSearchModel.getSize() > 0) {
            Pageable pageable = PageRequest.of(paginationSearchModel.getPage(), paginationSearchModel.getSize());
            query.with(pageable);
        }

        /**
         * sort
         */
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
            query.with(Sort.by(Sort.Direction.ASC, "pk"));
        }
        /**
         * query
         */
        if (paginationSearchModel.getSearchText() != null && !paginationSearchModel.getSearchText().isEmpty()) {
            ObjectId objectId = new ObjectId();
            if (ObjectId.isValid(paginationSearchModel.getSearchText())) {
                objectId = new ObjectId(paginationSearchModel.getSearchText());
            } else {
                objectId = null;
            }

            Criteria criteria = new Criteria();
            criteria.orOperator(
                Criteria.where("_id").is(objectId),
                Criteria.where("pk").regex(paginationSearchModel.getSearchText(), "i"),
                Criteria.where("name").regex(paginationSearchModel.getSearchText(), "i"),
                Criteria.where("barcode").regex(paginationSearchModel.getSearchText(), "i"),
                Criteria.where("price_1").regex(paginationSearchModel.getSearchText(), "i"),
                Criteria.where("price_2").regex(paginationSearchModel.getSearchText(), "i"),
                Criteria.where("category").regex(paginationSearchModel.getSearchText(), "i"),
                Criteria.where("cost").regex(paginationSearchModel.getSearchText(), "i"),
                Criteria.where("qty").regex(paginationSearchModel.getSearchText(), "i")
            );
            query.addCriteria(criteria);
            countQuery.addCriteria(criteria);
        }
        ItemViewDTOContainer itemViewDTOContainer = new ItemViewDTOContainer();

        List<Item> itemsFound = mongoTemplate.find(query, Item.class);
        Long ResultCount = mongoTemplate.count(countQuery, Item.class);

        itemViewDTOContainer.setResult(itemsFound.stream().map(itemMapper::toViewDto).collect(Collectors.toList()));
        itemViewDTOContainer.setTotal(ResultCount.intValue());
        return itemViewDTOContainer;
    }

    public Page<ItemDTO> findAllWithEagerRelationships(Pageable pageable) {
        return itemRepository.findAllWithEagerRelationships(pageable).map(itemMapper::toDto);
    }

    @Override
    public Optional<ItemDTO> findOne(String id) {
        log.debug("Request to get Item : {}", id);
        return itemRepository.findOneWithEagerRelationships(id).map(itemMapper::toDto);
    }

    /**
     * Get the "id" item.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<Item> findOneById(String id) {
        return itemRepository.findById(id);
    }

    /**
     * @return
     */
    @Override
    public ItemMapper getMapper() {
        return this.itemMapper;
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Item : {}", id);
        if (!invoiceItemRepository.findAllByItemId(id).isEmpty()) {
            throw new ItemDeletionException("Cannot delete Item with ID " + id + ": There are associated Invoices");
        }
        this.deleteItemUnits(id);
        itemRepository.deleteById(id);
    }

    @Override
    public void deleteItemUnits(String id) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isPresent()) {
            if (!item.get().getItemUnits().isEmpty()) {
                item
                    .get()
                    .getItemUnits()
                    .stream()
                    .forEach(itemUnit -> {
                        if (itemUnit.getId() != null) {
                            itemUnitService.delete(itemUnit.getId());
                        }
                    });
                Item itemToUpate = item.get();
                itemToUpate.getItemUnits().clear();
                itemRepository.save(itemToUpate);
            }
        }
    }

    /**
     * Retrieves all unique item categories from the MongoDB collection "items".
     *
     * @return A list of CategoryItem objects representing the unique item categories.
     */
    @Override
    public List<CategoryItem> getAllItemCategories() {
        List<CategoryItem> categoryList = new ArrayList<>();
        DistinctIterable distinctIterable = mongoTemplate.getCollection("items").distinct("category", String.class);
        MongoCursor cursor = distinctIterable.iterator();
        while (cursor.hasNext()) {
            String category = (String) cursor.next();
            CategoryItem categoryItem = new CategoryItem();
            categoryItem.setName(category);
            categoryList.add(categoryItem);
        }
        return categoryList;
    }

    @Override
    public Optional<ItemDTO> findOneByPk(String pk) {
        return this.itemRepository.findOneByPk(Integer.parseInt(pk)).map(itemMapper::toDto);
    }

    @Override
    public Optional<ItemDTO> getItemBeforeItemById(String id) {
        Optional<Item> itemFound = itemRepository.findById(id);

        if (!itemFound.isPresent()) {
            return getLastItem();
        }
        Integer itemPk = -1;
        try {
            itemPk = Integer.parseInt(itemFound.get().getPk());
        } catch (Exception e) {}

        Integer nextItemPk = itemPk - 1; //9
        Integer maximumSearchPk = itemPk - 1 - 1000; //1009

        Query query = new Query();

        query.addCriteria(Criteria.where("pk").gte(maximumSearchPk).lte(nextItemPk));

        query.limit(1).with(Sort.by(Sort.Direction.DESC, "pk"));

        List<Item> items = mongoTemplate.find(query, Item.class);

        if (items.isEmpty()) {
            return Optional.empty();
        }

        return items.stream().findFirst().map(itemMapper::toDto);
    }

    @Override
    public Optional<ItemDTO> getItemAfterItemById(String id) {
        Optional<Item> itemFound = itemRepository.findById(id);

        if (!itemFound.isPresent()) {
            return getLastItem();
        }
        Integer itemPk = -1;
        try {
            itemPk = Integer.parseInt(itemFound.get().getPk());
        } catch (Exception e) {}
        Integer nextItemPk = itemPk + 1; //9
        Integer maximumSearchPk = itemPk + 1 + 1000; //1009

        Query query = new Query();

        query.addCriteria(Criteria.where("pk").gte(nextItemPk).lte(maximumSearchPk));

        query.limit(1);

        List<Item> items = mongoTemplate.find(query, Item.class);

        if (items.isEmpty()) {
            return Optional.empty();
        }

        return items.stream().findFirst().map(itemMapper::toDto);
    }

    /**
     * Delete the "id" itemUnit.
     *
     * @param id the id of the entity.
     */
    @Override
    public void deleteUnitItemById(String id) {
        log.debug("Request to delete ItemUnit : {}", id);
        itemUnitRepository.deleteById(id);

        Query query = Query.query(Criteria.where("$id").is(new ObjectId(id)));
        Update update = new Update().pull("itemUnits", query);
        mongoTemplate.updateMulti(new Query(), update, Item.class);
    }

    @Override
    public Optional<ItemUnit> getUnitItemById(String id) {
        return itemUnitRepository.findById(id);
    }

    @Override
    public List<ItemUnitDTO> getItemUnits(String id) {
        Optional<Item> itemFound = itemRepository.findById(id);

        if (!itemFound.isPresent()) {
            throw new ItemNotFoundException(null);
        }
        return itemFound.get().getItemUnits().stream().map(itemUnitMapper::toDto).collect(Collectors.toList());
    }

    public Optional<ItemDTO> getLastItem() {
        Query query = new Query();
        query.limit(1);
        query.with(Sort.by(Sort.Direction.DESC, "created_date"));
        List<Item> items = mongoTemplate.find(query, Item.class);
        if (items.isEmpty()) {
            return Optional.empty();
        }

        return items.stream().findFirst().map(itemMapper::toDto);
    }

    @Override
    public ItemAnalysisDTO analyzeItem(String itemId, String storeId, Instant startDate, Instant endDate) {
        if (itemId == null || itemId.trim().isEmpty()) {
            throw new IllegalArgumentException("Item ID cannot be null or empty");
        }

        try {
            // Build the aggregation pipeline
            List<Document> pipeline = new ArrayList<>();

            // Match stage: Filter by itemId, storeId, and optional date range
            Document matchStage = new Document("$match", new Document("item.$id", new ObjectId(itemId)));
            if (storeId != null && !storeId.trim().isEmpty()) {
                matchStage.get("$match", Document.class).append("store.$id", new ObjectId(storeId));
            }
            if (startDate != null || endDate != null) {
                Document dateFilter = new Document();
                if (startDate != null) {
                    dateFilter.append("$gte", startDate);
                }
                if (endDate != null) {
                    dateFilter.append("$lte", endDate);
                }
                matchStage.get("$match", Document.class).append("created_date", dateFilter);
            }
            pipeline.add(matchStage);

            // Project stage: Calculate derived fields (e.g., profits, discounts)
            pipeline.add(
                new Document(
                    "$project",
                    new Document("totalSales", new Document("$toDouble", "$total_price"))
                        .append("netSales", new Document("$toDouble", "$net_price"))
                        .append("totalCost", new Document("$toDouble", "$total_cost"))
                        .append("netCost", new Document("$toDouble", "$net_cost"))
                        .append(
                            "profit",
                            new Document(
                                "$subtract",
                                Arrays.asList(new Document("$toDouble", "$net_price"), new Document("$toDouble", "$net_cost"))
                            )
                        )
                        .append("discount", new Document("$toDouble", "$discount"))
                        .append("qtyOut", new Document("$toDouble", "$qty_out"))
                        .append("qtyIn", new Document("$toDouble", "$qty_in"))
                )
            );

            // Group stage: Aggregate totals for the item
            pipeline.add(
                new Document(
                    "$group",
                    new Document("_id", null)
                        .append("totalSales", new Document("$sum", "$totalSales"))
                        .append("netSales", new Document("$sum", "$netSales"))
                        .append("totalCost", new Document("$sum", "$totalCost"))
                        .append("netCost", new Document("$sum", "$netCost"))
                        .append("totalProfit", new Document("$sum", "$profit"))
                        .append("totalDiscount", new Document("$sum", "$discount"))
                        .append("totalQtyOut", new Document("$sum", "$qtyOut"))
                        .append("totalQtyIn", new Document("$sum", "$qtyIn"))
                )
            );

            // Execute the aggregation
            MongoCollection<Document> collection = mongoTemplate.getCollection("invoice_items");
            AggregateIterable<Document> result = collection.aggregate(pipeline);
            Document aggregationResult = result.first();

            if (aggregationResult == null) {
                log.warn("No results found for item ID: {}", itemId);
                return new ItemAnalysisDTO(); // Return empty DTO if no result
            }

            // Map the aggregation result to ItemAnalysisDTO
            ItemAnalysisDTO itemAnalysis = new ItemAnalysisDTO();
            itemAnalysis.setTotalSales(safeGetDouble(aggregationResult, "totalSales"));
            itemAnalysis.setNetSales(safeGetDouble(aggregationResult, "netSales"));
            itemAnalysis.setTotalCost(safeGetDouble(aggregationResult, "totalCost"));
            itemAnalysis.setNetCost(safeGetDouble(aggregationResult, "netCost"));
            itemAnalysis.setTotalProfit(safeGetDouble(aggregationResult, "totalProfit"));
            itemAnalysis.setTotalDiscount(safeGetDouble(aggregationResult, "totalDiscount"));
            itemAnalysis.setTotalQtyOut(safeGetDouble(aggregationResult, "totalQtyOut"));
            itemAnalysis.setTotalQtyIn(safeGetDouble(aggregationResult, "totalQtyIn"));

            // Log the results for debugging
            log.info("Item Analysis DTO: {}", itemAnalysis);

            return itemAnalysis;
        } catch (Exception e) {
            log.error("Error analyzing item ID: {}", itemId, e);
            throw new RuntimeException("Failed to analyze item", e);
        }
    }

    @Override
    public ChartDataContainer getSalesChartData(String itemId, Instant startDate, Instant endDate) {
        if (itemId == null || itemId.trim().isEmpty()) {
            throw new IllegalArgumentException("Item ID cannot be null or empty");
        }

        try {
            // Build the aggregation pipeline to group sales by date
            List<Document> pipeline = new ArrayList<>();

            // Match stage: Filter by itemId and optional date range
            Document matchStage = new Document(
                "$match",
                new Document("item.$id", new ObjectId(itemId))
                    .append("created_date", new Document("$gte", startDate).append("$lte", endDate))
            );
            pipeline.add(matchStage);

            // Project stage: Extract necessary fields (date, total sales, qty, price)
            pipeline.add(
                new Document(
                    "$project",
                    new Document("date", "$created_date")
                        .append("totalSales", new Document("$toDouble", "$total_price"))
                        .append("qty", new Document("$toDouble", "$qty_out"))
                        .append("price", new Document("$toDouble", "$unit_price"))
                )
            );

            // Group stage: Group by date (or month) and calculate total sales, quantity, and price
            pipeline.add(
                new Document(
                    "$group",
                    new Document("_id", new Document("$dateToString", new Document("format", "%Y-%m-%d").append("date", "$date")))
                        .append("totalSales", new Document("$sum", "$totalSales"))
                        .append("totalQty", new Document("$sum", "$qty"))
                        .append("avgPrice", new Document("$avg", "$price"))
                )
            );

            // Sort stage: Sort by date in ascending order
            pipeline.add(new Document("$sort", new Document("_id", 1)));

            // Execute the aggregation
            MongoCollection<Document> collection = mongoTemplate.getCollection("invoice_items");
            AggregateIterable<Document> result = collection.aggregate(pipeline);

            List<com.konsol.core.service.api.dto.ChartDataDTO> chartDataList = new ArrayList<>();
            for (Document doc : result) {
                com.konsol.core.service.api.dto.ChartDataDTO chartData = new com.konsol.core.service.api.dto.ChartDataDTO();
                chartData.setDate(LocalDate.parse(doc.getString("_id")));
                chartData.setTotalSales(safeGetDouble(doc, "totalSales"));
                chartData.setTotalQty(safeGetDouble(doc, "totalQty"));
                chartData.setAvgPrice(safeGetDouble(doc, "avgPrice"));
                chartDataList.add(chartData);
            }
            ChartDataContainer chartDataContainer = new ChartDataContainer();
            chartDataContainer.setResult(chartDataList);
            chartDataContainer.setTotal(BigDecimal.valueOf(chartDataList.size()));
            log.info("Item Chart DTO: {}", chartDataList);
            // Return the result to be used for the chart rendering
            return chartDataContainer;
        } catch (Exception e) {
            log.error("Error generating sales chart data for item ID: {}", itemId, e);
            throw new RuntimeException("Failed to generate sales chart data", e);
        }
    }

    @Override
    public List<ItemSimpleDTO> findAllItemSimpleByCategory(String category) {
        return itemRepository.findAllByCategory(category).stream().map(itemMapper::toSimpleDTO).collect(Collectors.toList());
    }

    @Override
    public List<ItemSimpleDTO> allItemsByCategoryAndContainerIdPrice(String containerId, CategoryItem categoryItem) {
        if (containerId == null || containerId.isBlank() || containerId.isEmpty()) {
            if (categoryItem == null || categoryItem.getName() == null || categoryItem.getName().isEmpty()) {
                return new ArrayList<>();
            }
            return findAllItemSimpleByCategory(categoryItem.getName());
        }

        return itemRepository
            .findAllByCategory(categoryItem.getName())
            .stream()
            .map(item -> {
                if (item.getPriceOptions().isEmpty()) {
                    return item;
                }
                Optional<ItemPriceOptions> containerPriceOption = item
                    .getPriceOptions()
                    .stream()
                    .filter(itemPriceOptions ->
                        itemPriceOptions.getRefId() != null &&
                        !itemPriceOptions.getRefId().isEmpty() &&
                        !itemPriceOptions.getRefId().isBlank()
                    )
                    .filter(itemPriceOptions -> itemPriceOptions.getRefId().equals(containerId))
                    .findFirst();

                if (containerPriceOption.isPresent()) {
                    return item.price1(containerPriceOption.get().getValue().toString());
                }
                return item;
            })
            .map(itemMapper::toSimpleDTO)
            .collect(Collectors.toList());
    }

    private double safeGetDouble(Document doc, String key) {
        if (doc == null || !doc.containsKey(key)) {
            log.warn("Field {} not found in the document.", key);
            return 0.0;
        }

        Object value = doc.get(key);
        if (value == null) {
            log.warn("Null value for key: {}", key);
            return 0.0;
        }

        try {
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            }

            if (value instanceof String) {
                return Double.parseDouble((String) value);
            }
        } catch (NumberFormatException e) {
            log.warn("Unable to parse value for key {}: {}", key, value);
        }

        return 0.0;
    }
}
