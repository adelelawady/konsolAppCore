package com.konsol.core.service.impl;

import com.konsol.core.domain.PlayStationSession;
import com.konsol.core.repository.PlayStationSessionRepository;
import com.konsol.core.service.PlayStationSessionService;
import com.konsol.core.service.dto.PlayStationSessionDTO;
import com.konsol.core.service.mapper.PlayStationSessionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.konsol.core.domain.PlayStationSession}.
 */
@Service
public class PlayStationSessionServiceImpl implements PlayStationSessionService {

    private static final Logger LOG = LoggerFactory.getLogger(PlayStationSessionServiceImpl.class);

    private final PlayStationSessionRepository playStationSessionRepository;

    private final PlayStationSessionMapper playStationSessionMapper;

    public PlayStationSessionServiceImpl(
        PlayStationSessionRepository playStationSessionRepository,
        PlayStationSessionMapper playStationSessionMapper
    ) {
        this.playStationSessionRepository = playStationSessionRepository;
        this.playStationSessionMapper = playStationSessionMapper;
    }

    @Override
    public PlayStationSessionDTO save(PlayStationSessionDTO playStationSessionDTO) {
        LOG.debug("Request to save PlayStationSession : {}", playStationSessionDTO);
        PlayStationSession playStationSession = playStationSessionMapper.toEntity(playStationSessionDTO);
        playStationSession = playStationSessionRepository.save(playStationSession);
        return playStationSessionMapper.toDto(playStationSession);
    }

    @Override
    public PlayStationSessionDTO update(PlayStationSessionDTO playStationSessionDTO) {
        LOG.debug("Request to update PlayStationSession : {}", playStationSessionDTO);
        PlayStationSession playStationSession = playStationSessionMapper.toEntity(playStationSessionDTO);
        playStationSession = playStationSessionRepository.save(playStationSession);
        return playStationSessionMapper.toDto(playStationSession);
    }

    @Override
    public Optional<PlayStationSessionDTO> partialUpdate(PlayStationSessionDTO playStationSessionDTO) {
        LOG.debug("Request to partially update PlayStationSession : {}", playStationSessionDTO);

        return playStationSessionRepository
            .findById(playStationSessionDTO.getId())
            .map(existingPlayStationSession -> {
                playStationSessionMapper.partialUpdate(existingPlayStationSession, playStationSessionDTO);

                return existingPlayStationSession;
            })
            .map(playStationSessionRepository::save)
            .map(playStationSessionMapper::toDto);
    }

    @Override
    public Page<PlayStationSessionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PlayStationSessions");
        return playStationSessionRepository.findAll(pageable).map(playStationSessionMapper::toDto);
    }

    @Override
    public Optional<PlayStationSessionDTO> findOne(Long id) {
        LOG.debug("Request to get PlayStationSession : {}", id);
        return playStationSessionRepository.findById(id).map(playStationSessionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PlayStationSession : {}", id);
        playStationSessionRepository.deleteById(id);
    }
}
