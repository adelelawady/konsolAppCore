package com.konsol.core.service.impl;

import com.konsol.core.domain.PlaystationContainer;
import com.konsol.core.domain.playstation.PlayStationSession;
import com.konsol.core.repository.PlayStationSessionRepository;
import com.konsol.core.repository.PlaystationContainerRepository;
import com.konsol.core.service.PlayStationSessionService;
import com.konsol.core.service.api.dto.PsSessionDTO;
import com.konsol.core.service.mapper.PlayStationSessionMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link PlayStationSession}.
 */
@Service
public class PlayStationSessionServiceImpl implements PlayStationSessionService {

    private static final Logger LOG = LoggerFactory.getLogger(PlayStationSessionServiceImpl.class);

    private final PlayStationSessionRepository playStationSessionRepository;

    private final PlayStationSessionMapper playStationSessionMapper;

    private final PlaystationContainerRepository playstationContainerRepository;

    private final MongoTemplate mongoTemplate;

    public PlayStationSessionServiceImpl(
        PlayStationSessionRepository playStationSessionRepository,
        PlayStationSessionMapper playStationSessionMapper,
        PlaystationContainerRepository playstationContainerRepository,
        MongoTemplate mongoTemplate
    ) {
        this.playStationSessionRepository = playStationSessionRepository;
        this.playStationSessionMapper = playStationSessionMapper;
        this.playstationContainerRepository = playstationContainerRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public PsSessionDTO save(PsSessionDTO PsSessionDTO) {
        LOG.debug("Request to save PlayStationSession : {}", PsSessionDTO);
        PlayStationSession playStationSession = playStationSessionMapper.toEntity(PsSessionDTO);
        playStationSession = playStationSessionRepository.save(playStationSession);
        return playStationSessionMapper.toDto(playStationSession);
    }

    @Override
    public PsSessionDTO update(PsSessionDTO PsSessionDTO) {
        LOG.debug("Request to update PlayStationSession : {}", PsSessionDTO);
        PlayStationSession playStationSession = playStationSessionMapper.toEntity(PsSessionDTO);
        playStationSession = playStationSessionRepository.save(playStationSession);
        return playStationSessionMapper.toDto(playStationSession);
    }

    @Override
    public Optional<PsSessionDTO> partialUpdate(PsSessionDTO PsSessionDTO) {
        LOG.debug("Request to partially update PlayStationSession : {}", PsSessionDTO);

        return playStationSessionRepository
            .findById(PsSessionDTO.getId())
            .map(existingPlayStationSession -> {
                playStationSessionMapper.partialUpdate(existingPlayStationSession, PsSessionDTO);

                return existingPlayStationSession;
            })
            .map(playStationSessionRepository::save)
            .map(playStationSessionMapper::toDto);
    }

    @Override
    public Page<PsSessionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PlayStationSessions");
        return playStationSessionRepository.findAll(pageable).map(playStationSessionMapper::toDto);
    }

    @Override
    public List<PsSessionDTO> findAll() {
        LOG.debug("Request to get all PlayStationSessions");
        return playStationSessionRepository.findAll().stream().map(playStationSessionMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<PsSessionDTO> findAllByContainerId(String containerId) {
        Optional<PlaystationContainer> playstationContainerOptional = playstationContainerRepository.findById(containerId);
        if (playstationContainerOptional.isEmpty()) {
            return new ArrayList<>();
        }

        PlaystationContainer container = playstationContainerOptional.get();
        
        // Create the aggregation pipeline using Document
        List<Document> pipeline = Arrays.asList(
            new Document("$match", 
                new Document("device", new Document("$exists", true))
                    .append("device.category", 
                        new Document("$in", new ArrayList<>(container.getAcceptedOrderCategories())))
            )
        );

        // Execute the aggregation
        List<PlayStationSession> sessions = mongoTemplate.aggregate(
            Aggregation.newAggregation(
                ctx -> Document.parse(pipeline.get(0).toJson())
            ),
            PlayStationSession.class,
            PlayStationSession.class
        ).getMappedResults();

        return sessions.stream()
            .map(playStationSessionMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<PsSessionDTO> findOne(String id) {
        LOG.debug("Request to get PlayStationSession : {}", id);
        return playStationSessionRepository.findById(id).map(playStationSessionMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete PlayStationSession : {}", id);
        playStationSessionRepository.deleteById(id);
    }
}
