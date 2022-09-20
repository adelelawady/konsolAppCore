package com.konsol.core.service.impl;

import com.konsol.core.domain.Item;
import com.konsol.core.domain.ItemUnit;
import com.konsol.core.domain.Pk;
import com.konsol.core.domain.enumeration.PkKind;
import com.konsol.core.repository.ItemRepository;
import com.konsol.core.repository.ItemUnitRepository;
import com.konsol.core.service.ItemService;
import com.konsol.core.service.ItemUnitService;
import com.konsol.core.service.PkService;
import com.konsol.core.service.api.dto.*;
import com.konsol.core.service.mapper.ItemMapper;
import com.konsol.core.service.mapper.ItemUnitMapper;
import com.konsol.core.web.rest.api.errors.ItemUnitException;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCursor;
import java.util.*;
import java.util.stream.Collectors;
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

    public ItemServiceImpl(
        ItemRepository itemRepository,
        ItemMapper itemMapper,
        ItemUnitRepository itemUnitRepository,
        MongoTemplate mongoTemplate,
        PkService pkService,
        ItemUnitService itemUnitService,
        ItemUnitMapper itemUnitMapper
    ) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.itemUnitRepository = itemUnitRepository;
        this.mongoTemplate = mongoTemplate;
        this.pkService = pkService;
        this.itemUnitService = itemUnitService;
        this.itemUnitMapper = itemUnitMapper;
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
            if (itemUnitDTO.getBasic() && !hasBasicUnit) {
                hasBasicUnit = true;
            } else if (itemUnitDTO.getBasic() && hasBasicUnit) {
                hasMultiBasicUnit = true;
                break;
            }
        }

        if (!hasBasicUnit) {
            /**
             * throw must have basic unit
             */
            throw new ItemUnitException("مشكلة في وحدة الصنف", "يجب ان يحتوي الصنف علي وحدة اساسية", null);
        }

        if (hasMultiBasicUnit) {
            /**
             * throw must have only one basic unit
             */
            throw new ItemUnitException("مشكلة في وحدة الصنف", "يجب ان يحتوي الصنف علي وحدة اساسية واحدة فقط", null);
        }
    }

    @Override
    public ItemDTO update(ItemDTO itemDTO) {
        Optional<Item> itemToUpdate = itemRepository.findById(itemDTO.getId());
        if (!itemToUpdate.isPresent()) {
            return null;
        }

        log.debug("Request to update Item : {}", itemDTO);
        Item item = itemMapper.toEntity(itemDTO);
        item.setItemUnits(itemToUpdate.get().getItemUnits());
        item = itemRepository.save(item);
        return itemMapper.toDto(item);
    }

    public void SaveItemUnits(ItemDTO itemDTO) {
        itemRepository
            .findById(itemDTO.getId())
            .map(item -> {
                if (itemDTO.getItemUnits() == null || itemDTO.getItemUnits().isEmpty()) {
                    item.setItemUnits(new HashSet<>());
                } else {
                    for (ItemUnitDTO itemUnitDTO : itemDTO.getItemUnits()) {
                        ItemUnit itemUnit = itemUnitService.saveDomain(itemUnitDTO);
                        itemUnit.setItemId(itemDTO.getId());
                        itemUnit = itemUnitService.saveDomain(itemUnit);
                        item.getItemUnits().add(itemUnit);
                    }
                }
                return item;
            })
            .map(itemRepository::save);
    }

    @Override
    public Optional<ItemDTO> partialUpdate(ItemDTO itemDTO) {
        log.debug("Request to partially update Item : {}", itemDTO);

        this.validateNewItemUnits(itemDTO);
        SaveItemUnits(itemDTO);
        itemDTO.setItemUnits(null);
        return itemRepository
            .findById(itemDTO.getId())
            .map(existingItem -> {
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

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}
