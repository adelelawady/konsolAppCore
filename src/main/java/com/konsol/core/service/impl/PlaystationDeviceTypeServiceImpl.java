package com.konsol.core.service.impl;

import com.konsol.core.domain.playstation.PlaystationDeviceType;
import com.konsol.core.repository.PlaystationDeviceTypeRepository;
import com.konsol.core.service.PlaystationDeviceTypeService;
import com.konsol.core.service.api.dto.PsDeviceType;
import com.konsol.core.service.mapper.PlaystationDeviceTypeMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.konsol.core.domain.playstation.PlaystationDeviceType}.
 */
@Service
public class PlaystationDeviceTypeServiceImpl implements PlaystationDeviceTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(PlaystationDeviceTypeServiceImpl.class);

    private final PlaystationDeviceTypeRepository playstationDeviceTypeRepository;

    private final PlaystationDeviceTypeMapper playstationDeviceTypeMapper;

    public PlaystationDeviceTypeServiceImpl(
        PlaystationDeviceTypeRepository playstationDeviceTypeRepository,
        PlaystationDeviceTypeMapper playstationDeviceTypeMapper
    ) {
        this.playstationDeviceTypeRepository = playstationDeviceTypeRepository;
        this.playstationDeviceTypeMapper = playstationDeviceTypeMapper;
    }

    @Override
    public PsDeviceType save(PsDeviceType PsDeviceType) {
        LOG.debug("Request to save PlaystationDeviceType : {}", PsDeviceType);
        PlaystationDeviceType playstationDeviceType = playstationDeviceTypeMapper.toEntity(PsDeviceType);
        playstationDeviceType = playstationDeviceTypeRepository.save(playstationDeviceType);
        return playstationDeviceTypeMapper.toDto(playstationDeviceType);
    }

    @Override
    public PsDeviceType update(PsDeviceType PsDeviceType) {
        LOG.debug("Request to update PlaystationDeviceType : {}", PsDeviceType);
        PlaystationDeviceType playstationDeviceType = playstationDeviceTypeMapper.toEntity(PsDeviceType);
        playstationDeviceType = playstationDeviceTypeRepository.save(playstationDeviceType);
        return playstationDeviceTypeMapper.toDto(playstationDeviceType);
    }

    @Override
    public Optional<PsDeviceType> partialUpdate(PsDeviceType PsDeviceType) {
        LOG.debug("Request to partially update PlaystationDeviceType : {}", PsDeviceType);

        return playstationDeviceTypeRepository
            .findById(PsDeviceType.getId())
            .map(existingPlaystationDeviceType -> {
                playstationDeviceTypeMapper.partialUpdate(existingPlaystationDeviceType, PsDeviceType);

                return existingPlaystationDeviceType;
            })
            .map(playstationDeviceTypeRepository::save)
            .map(playstationDeviceTypeMapper::toDto);
    }

    @Override
    public Page<PsDeviceType> findAll(Pageable pageable) {
        LOG.debug("Request to get all PlaystationDeviceTypes");
        return playstationDeviceTypeRepository.findAll(pageable).map(playstationDeviceTypeMapper::toDto);
    }

    @Override
    public List<PsDeviceType> findAll() {
        LOG.debug("Request to get all PlaystationDeviceTypes");
        return playstationDeviceTypeRepository.findAll().stream().map(playstationDeviceTypeMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<PsDeviceType> findOne(String id) {
        LOG.debug("Request to get PlaystationDeviceType : {}", id);
        return playstationDeviceTypeRepository.findById(id).map(playstationDeviceTypeMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete PlaystationDeviceType : {}", id);
        playstationDeviceTypeRepository.deleteById(id);
    }
}
