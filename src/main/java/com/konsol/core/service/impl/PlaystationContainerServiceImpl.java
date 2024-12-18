package com.konsol.core.service.impl;

import com.konsol.core.domain.PlaystationContainer;
import com.konsol.core.repository.PlaystationContainerRepository;
import com.konsol.core.service.PlaystationContainerService;
import com.konsol.core.service.dto.PlaystationContainerDTO;
import com.konsol.core.service.mapper.PlaystationContainerMapper;
import java.util.Optional;
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
    public PlaystationContainerDTO save(PlaystationContainerDTO playstationContainerDTO) {
        LOG.debug("Request to save PlaystationContainer : {}", playstationContainerDTO);
        PlaystationContainer playstationContainer = playstationContainerMapper.toEntity(playstationContainerDTO);
        playstationContainer = playstationContainerRepository.save(playstationContainer);
        return playstationContainerMapper.toDto(playstationContainer);
    }

    @Override
    public PlaystationContainerDTO update(PlaystationContainerDTO playstationContainerDTO) {
        LOG.debug("Request to update PlaystationContainer : {}", playstationContainerDTO);
        PlaystationContainer playstationContainer = playstationContainerMapper.toEntity(playstationContainerDTO);
        playstationContainer = playstationContainerRepository.save(playstationContainer);
        return playstationContainerMapper.toDto(playstationContainer);
    }

    @Override
    public Optional<PlaystationContainerDTO> partialUpdate(PlaystationContainerDTO playstationContainerDTO) {
        LOG.debug("Request to partially update PlaystationContainer : {}", playstationContainerDTO);

        return playstationContainerRepository
            .findById(playstationContainerDTO.getId())
            .map(existingPlaystationContainer -> {
                playstationContainerMapper.partialUpdate(existingPlaystationContainer, playstationContainerDTO);

                return existingPlaystationContainer;
            })
            .map(playstationContainerRepository::save)
            .map(playstationContainerMapper::toDto);
    }

    @Override
    public Page<PlaystationContainerDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PlaystationContainers");
        return playstationContainerRepository.findAll(pageable).map(playstationContainerMapper::toDto);
    }

    @Override
    public Optional<PlaystationContainerDTO> findOne(String id) {
        LOG.debug("Request to get PlaystationContainer : {}", id);
        return playstationContainerRepository.findById(id).map(playstationContainerMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete PlaystationContainer : {}", id);
        playstationContainerRepository.deleteById(id);
    }
}
