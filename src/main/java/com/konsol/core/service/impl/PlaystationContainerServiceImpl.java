package com.konsol.core.service.impl;

import com.konsol.core.repository.PlaystationContainerRepository;
import com.konsol.core.service.PlaystationContainerService;
import com.konsol.core.service.api.dto.PlaystationContainer;
import com.konsol.core.service.mapper.PlaystationContainerMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.konsol.core.domain.PlaystationContainer}.
 */
@Service
public class PlaystationContainerServiceImpl implements PlaystationContainerService {

    private static final Logger LOG = LoggerFactory.getLogger(PlaystationContainerServiceImpl.class);

    private final PlaystationContainerRepository playstationContainerRepository;

    private final PlaystationContainerMapper playstationContainerMapper;

    public PlaystationContainerServiceImpl(
        PlaystationContainerRepository playstationContainerRepository,
        PlaystationContainerMapper playstationContainerMapper
    ) {
        this.playstationContainerRepository = playstationContainerRepository;
        this.playstationContainerMapper = playstationContainerMapper;
    }

    @Override
    public PlaystationContainer save(PlaystationContainer PlaystationContainer) {
        LOG.debug("Request to save PlaystationContainer : {}", PlaystationContainer);
        com.konsol.core.domain.PlaystationContainer playstationContainer = playstationContainerMapper.toEntity(PlaystationContainer);
        playstationContainer = playstationContainerRepository.save(playstationContainer);
        return playstationContainerMapper.toDto(playstationContainer);
    }

    @Override
    public PlaystationContainer update(PlaystationContainer PlaystationContainer) {
        LOG.debug("Request to update PlaystationContainer : {}", PlaystationContainer);
        com.konsol.core.domain.PlaystationContainer playstationContainer = playstationContainerMapper.toEntity(PlaystationContainer);
        playstationContainer = playstationContainerRepository.save(playstationContainer);
        return playstationContainerMapper.toDto(playstationContainer);
    }

    @Override
    public Optional<PlaystationContainer> partialUpdate(PlaystationContainer PlaystationContainer) {
        LOG.debug("Request to partially update PlaystationContainer : {}", PlaystationContainer);

        return playstationContainerRepository
            .findById(PlaystationContainer.getId())
            .map(existingPlaystationContainer -> {
                playstationContainerMapper.partialUpdate(existingPlaystationContainer, PlaystationContainer);

                return existingPlaystationContainer;
            })
            .map(playstationContainerRepository::save)
            .map(playstationContainerMapper::toDto);
    }

    @Override
    public Page<PlaystationContainer> findAll(Pageable pageable) {
        LOG.debug("Request to get all PlaystationContainers");
        return playstationContainerRepository.findAll(pageable).map(playstationContainerMapper::toDto);
    }

    @Override
    public List<PlaystationContainer> findAll() {
        LOG.debug("Request to get all PlaystationContainers");
        return playstationContainerRepository.findAll().stream().map(playstationContainerMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<PlaystationContainer> findOne(String id) {
        LOG.debug("Request to get PlaystationContainer : {}", id);
        return playstationContainerRepository.findById(id).map(playstationContainerMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete PlaystationContainer : {}", id);
        playstationContainerRepository.deleteById(id);
    }
}
