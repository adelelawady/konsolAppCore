package com.konsol.core.service.impl;

import com.konsol.core.domain.Money;
import com.konsol.core.repository.MoneyRepository;
import com.konsol.core.service.MoneyService;
import com.konsol.core.service.api.dto.MoneyDTO;
import com.konsol.core.service.api.dto.MoniesSearchModel;
import com.konsol.core.service.api.dto.MoniesViewDTOContainer;
import com.konsol.core.service.mapper.MoneyMapper;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.Arrays;
import java.util.Optional;
import org.bson.BsonNull;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Money}.
 */
@Service
public class MoneyServiceImpl implements MoneyService {

    private final Logger log = LoggerFactory.getLogger(MoneyServiceImpl.class);

    private final MoneyRepository moneyRepository;

    private final MoneyMapper moneyMapper;

    @Autowired
    public final MongoTemplate mongoTemplate;

    public MoneyServiceImpl(MoneyRepository moneyRepository, MoneyMapper moneyMapper, MongoTemplate mongoTemplate) {
        this.moneyRepository = moneyRepository;
        this.moneyMapper = moneyMapper;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public MoneyDTO save(MoneyDTO moneyDTO) {
        log.debug("Request to save Money : {}", moneyDTO);
        Money money = moneyMapper.toEntity(moneyDTO);
        money = moneyRepository.save(money);
        return moneyMapper.toDto(money);
    }

    @Override
    public MoneyDTO update(MoneyDTO moneyDTO) {
        log.debug("Request to update Money : {}", moneyDTO);
        Money money = moneyMapper.toEntity(moneyDTO);
        money = moneyRepository.save(money);
        return moneyMapper.toDto(money);
    }

    @Override
    public Optional<MoneyDTO> partialUpdate(MoneyDTO moneyDTO) {
        log.debug("Request to partially update Money : {}", moneyDTO);

        return moneyRepository
            .findById(moneyDTO.getId())
            .map(existingMoney -> {
                moneyMapper.partialUpdate(existingMoney, moneyDTO);

                return existingMoney;
            })
            .map(moneyRepository::save)
            .map(moneyMapper::toDto);
    }

    @Override
    public Page<MoneyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Monies");
        return moneyRepository.findAll(pageable).map(moneyMapper::toDto);
    }

    @Override
    public Optional<MoneyDTO> findOne(String id) {
        log.debug("Request to get Money : {}", id);
        return moneyRepository.findById(id).map(moneyMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Money : {}", id);
        moneyRepository.deleteById(id);
    }

    @Override
    public MoniesViewDTOContainer moniesViewSearchPaginate(MoniesSearchModel moniesSearchModel) {
        MongoCollection<Document> collection = mongoTemplate.getCollection("monies");
        AggregateIterable<Document> result = collection.aggregate(
            Arrays.asList(
                new Document(
                    "$match",
                    new Document(
                        "$or",
                        Arrays.asList(
                            new Document(
                                "$or",
                                Arrays.asList(
                                    new Document("pk", new BsonNull()),
                                    new Document("kind", new BsonNull()),
                                    new Document("created_date", new Document("$gte", new BsonNull()).append("$lt", new BsonNull()))
                                )
                            ),
                            new Document(
                                "$or",
                                Arrays.asList(
                                    new Document("_id", new ObjectId("641331ab3a789e767dec3d2c")),
                                    new Document("bank.$id", new ObjectId("641331ab3a789e767dec3d2c")),
                                    new Document("account.$id", new ObjectId("641331ab3a789e767dec3d2c")),
                                    new Document("item.$id", new ObjectId("641331ab3a789e767dec3d2c"))
                                )
                            )
                        )
                    )
                ),
                new Document("$skip", 0L),
                new Document("$limit", 10L),
                new Document("$sort", new Document("created_date", -1L))
            )
        );

        return null;
    }
}
