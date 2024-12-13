package com.konsol.core.service.impl;

import com.konsol.core.domain.PlaystationDevice;
import com.konsol.core.repository.PlaystationDeviceRepository;
import com.konsol.core.service.PlaystationDeviceService;
import com.konsol.core.service.dto.PlaystationDeviceDTO;
import com.konsol.core.service.mapper.PlaystationDeviceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.konsol.core.domain.PlaystationDevice}.
 */
@Service
public class PlaystationDeviceServiceImpl implements PlaystationDeviceService {

    private static final Logger LOG = LoggerFactory.getLogger(PlaystationDeviceServiceImpl.class);

    private final PlaystationDeviceRepository playstationDeviceRepository;

    private final PlaystationDeviceMapper playstationDeviceMapper;

    public PlaystationDeviceServiceImpl(
        PlaystationDeviceRepository playstationDeviceRepository,
        PlaystationDeviceMapper playstationDeviceMapper
    ) {
        this.playstationDeviceRepository = playstationDeviceRepository;
        this.playstationDeviceMapper = playstationDeviceMapper;
    }

    @Override
    public PlaystationDeviceDTO save(PlaystationDeviceDTO playstationDeviceDTO) {
        LOG.debug("Request to save PlaystationDevice : {}", playstationDeviceDTO);
        PlaystationDevice playstationDevice = playstationDeviceMapper.toEntity(playstationDeviceDTO);
        playstationDevice = playstationDeviceRepository.save(playstationDevice);
        return playstationDeviceMapper.toDto(playstationDevice);
    }

    @Override
    public PlaystationDeviceDTO update(PlaystationDeviceDTO playstationDeviceDTO) {
        LOG.debug("Request to update PlaystationDevice : {}", playstationDeviceDTO);
        PlaystationDevice playstationDevice = playstationDeviceMapper.toEntity(playstationDeviceDTO);
        playstationDevice = playstationDeviceRepository.save(playstationDevice);
        return playstationDeviceMapper.toDto(playstationDevice);
    }

    @Override
    public Optional<PlaystationDeviceDTO> partialUpdate(PlaystationDeviceDTO playstationDeviceDTO) {
        LOG.debug("Request to partially update PlaystationDevice : {}", playstationDeviceDTO);

        return playstationDeviceRepository
            .findById(playstationDeviceDTO.getId())
            .map(existingPlaystationDevice -> {
                playstationDeviceMapper.partialUpdate(existingPlaystationDevice, playstationDeviceDTO);

                return existingPlaystationDevice;
            })
            .map(playstationDeviceRepository::save)
            .map(playstationDeviceMapper::toDto);
    }

    @Override
    public Page<PlaystationDeviceDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PlaystationDevices");
        return playstationDeviceRepository.findAll(pageable).map(playstationDeviceMapper::toDto);
    }

    @Override
    public Optional<PlaystationDeviceDTO> findOne(String id) {
        LOG.debug("Request to get PlaystationDevice : {}", id);
        return playstationDeviceRepository.findById(id).map(playstationDeviceMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete PlaystationDevice : {}", id);
        playstationDeviceRepository.deleteById(id);
    }
}
