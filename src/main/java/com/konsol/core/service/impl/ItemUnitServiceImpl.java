package com.konsol.core.service.impl;

import com.konsol.core.domain.Item;
import com.konsol.core.domain.ItemUnit;
import com.konsol.core.repository.ItemUnitRepository;
import com.konsol.core.service.ItemUnitService;
import com.konsol.core.service.api.dto.ItemUnitDTO;
import com.konsol.core.service.mapper.ItemUnitMapper;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link ItemUnit}.
 */
@Service
public class ItemUnitServiceImpl implements ItemUnitService {

    private final Logger log = LoggerFactory.getLogger(ItemUnitServiceImpl.class);

    private final ItemUnitRepository itemUnitRepository;

    private final ItemUnitMapper itemUnitMapper;

    private final MongoTemplate mongoTemplate;

    public ItemUnitServiceImpl(ItemUnitRepository itemUnitRepository, ItemUnitMapper itemUnitMapper, MongoTemplate mongoTemplate) {
        this.itemUnitRepository = itemUnitRepository;
        this.itemUnitMapper = itemUnitMapper;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public ItemUnitDTO save(ItemUnitDTO itemUnitDTO) {
        log.debug("Request to save ItemUnit : {}", itemUnitDTO);
        ItemUnit itemUnit = itemUnitMapper.toEntity(itemUnitDTO);
        itemUnit = itemUnitRepository.save(itemUnit);
        return itemUnitMapper.toDto(itemUnit);
    }

    @Override
    public ItemUnit saveDomain(ItemUnitDTO itemUnitDTO) {
        log.debug("Request to save ItemUnit : {}", itemUnitDTO);
        ItemUnit itemUnit = itemUnitMapper.toEntity(itemUnitDTO);
        return itemUnitRepository.save(itemUnit);
    }

    @Override
    public ItemUnit saveDomain(ItemUnit itemUnit) {
        return itemUnitRepository.save(itemUnit);
    }

    @Override
    public ItemUnitDTO update(ItemUnitDTO itemUnitDTO) {
        log.debug("Request to update ItemUnit : {}", itemUnitDTO);
        ItemUnit itemUnit = itemUnitMapper.toEntity(itemUnitDTO);
        itemUnit = itemUnitRepository.save(itemUnit);
        return itemUnitMapper.toDto(itemUnit);
    }

    @Override
    public Optional<ItemUnitDTO> partialUpdate(ItemUnitDTO itemUnitDTO) {
        log.debug("Request to partially update ItemUnit : {}", itemUnitDTO);

        return itemUnitRepository
            .findById(itemUnitDTO.getId())
            .map(existingItemUnit -> {
                itemUnitMapper.partialUpdate(existingItemUnit, itemUnitDTO);

                return existingItemUnit;
            })
            .map(itemUnitRepository::save)
            .map(itemUnitMapper::toDto);
    }

    @Override
    public Page<ItemUnitDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ItemUnits");
        return itemUnitRepository.findAll(pageable).map(itemUnitMapper::toDto);
    }

    @Override
    public Optional<ItemUnitDTO> findOne(String id) {
        log.debug("Request to get ItemUnit : {}", id);
        return itemUnitRepository.findById(id).map(itemUnitMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete ItemUnit : {}", id);
        itemUnitRepository.deleteById(id);

        Query query = Query.query(Criteria.where("$id").is(new ObjectId(id)));
        Update update = new Update().pull("itemUnits", query);
        mongoTemplate.updateMulti(new Query(), update, Item.class);
    }
}
