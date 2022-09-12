package com.konsol.core.service.impl;

import com.konsol.core.domain.Item;
import com.konsol.core.domain.Pk;
import com.konsol.core.domain.enumeration.PkKind;
import com.konsol.core.repository.ItemRepository;
import com.konsol.core.service.ItemService;
import com.konsol.core.service.PkService;
import com.konsol.core.service.api.dto.CategoryItem;
import com.konsol.core.service.api.dto.ItemDTO;
import com.konsol.core.service.mapper.ItemMapper;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCursor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.Optionals;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Item}.
 */
@Service
public class ItemServiceImpl implements ItemService {

    private final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    private final MongoTemplate mongoTemplate;

    public ItemServiceImpl(ItemRepository itemRepository, ItemMapper itemMapper, MongoTemplate mongoTemplate) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public ItemDTO save(ItemDTO itemDTO) {
        log.debug("Request to save Item : {}", itemDTO);
        Item item = itemMapper.toEntity(itemDTO);
        item = itemRepository.save(item);
        return itemMapper.toDto(item);
    }

    @Override
    public ItemDTO update(ItemDTO itemDTO) {
        log.debug("Request to update Item : {}", itemDTO);
        Item item = itemMapper.toEntity(itemDTO);
        item = itemRepository.save(item);
        return itemMapper.toDto(item);
    }

    @Override
    public Optional<ItemDTO> partialUpdate(ItemDTO itemDTO) {
        log.debug("Request to partially update Item : {}", itemDTO);

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

    public Page<ItemDTO> findAllWithEagerRelationships(Pageable pageable) {
        return itemRepository.findAllWithEagerRelationships(pageable).map(itemMapper::toDto);
    }

    @Override
    public Optional<ItemDTO> findOne(String id) {
        log.debug("Request to get Item : {}", id);
        return itemRepository.findOneWithEagerRelationships(id).map(itemMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Item : {}", id);
        itemRepository.deleteById(id);
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
        Integer itemPk = itemFound.get().getPk(); //10
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
        Integer itemPk = itemFound.get().getPk(); //10
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
}
