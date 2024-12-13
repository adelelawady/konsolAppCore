package com.konsol.core.service.impl;

import com.konsol.core.domain.PlaystationDeviceType;
import com.konsol.core.repository.PlaystationDeviceTypeRepository;
import com.konsol.core.service.PlaystationDeviceTypeService;
import com.konsol.core.service.dto.PlaystationDeviceTypeDTO;
import com.konsol.core.service.mapper.PlaystationDeviceTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.konsol.core.domain.PlaystationDeviceType}.
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
    public PlaystationDeviceTypeDTO save(PlaystationDeviceTypeDTO playstationDeviceTypeDTO) {
        LOG.debug("Request to save PlaystationDeviceType : {}", playstationDeviceTypeDTO);
        PlaystationDeviceType playstationDeviceType = playstationDeviceTypeMapper.toEntity(playstationDeviceTypeDTO);
        playstationDeviceType = playstationDeviceTypeRepository.save(playstationDeviceType);
        return playstationDeviceTypeMapper.toDto(playstationDeviceType);
    }

    @Override
    public PlaystationDeviceTypeDTO update(PlaystationDeviceTypeDTO playstationDeviceTypeDTO) {
        LOG.debug("Request to update PlaystationDeviceType : {}", playstationDeviceTypeDTO);
        PlaystationDeviceType playstationDeviceType = playstationDeviceTypeMapper.toEntity(playstationDeviceTypeDTO);
        playstationDeviceType = playstationDeviceTypeRepository.save(playstationDeviceType);
        return playstationDeviceTypeMapper.toDto(playstationDeviceType);
    }

    @Override
    public Optional<PlaystationDeviceTypeDTO> partialUpdate(PlaystationDeviceTypeDTO playstationDeviceTypeDTO) {
        LOG.debug("Request to partially update PlaystationDeviceType : {}", playstationDeviceTypeDTO);

        return playstationDeviceTypeRepository
            .findById(playstationDeviceTypeDTO.getId())
            .map(existingPlaystationDeviceType -> {
                playstationDeviceTypeMapper.partialUpdate(existingPlaystationDeviceType, playstationDeviceTypeDTO);

                return existingPlaystationDeviceType;
            })
            .map(playstationDeviceTypeRepository::save)
            .map(playstationDeviceTypeMapper::toDto);
    }

    @Override
    public Page<PlaystationDeviceTypeDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PlaystationDeviceTypes");
        return playstationDeviceTypeRepository.findAll(pageable).map(playstationDeviceTypeMapper::toDto);
    }

    @Override
    public Optional<PlaystationDeviceTypeDTO> findOne(String id) {
        LOG.debug("Request to get PlaystationDeviceType : {}", id);
        return playstationDeviceTypeRepository.findById(id).map(playstationDeviceTypeMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete PlaystationDeviceType : {}", id);
        playstationDeviceTypeRepository.deleteById(id);
    }
}
