package com.konsol.core.service.impl;

import com.konsol.core.domain.Sheft;
import com.konsol.core.repository.SheftRepository;
import com.konsol.core.service.SheftService;
import com.konsol.core.service.dto.SheftDTO;
import com.konsol.core.service.mapper.SheftMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.konsol.core.domain.Sheft}.
 */
@Service
public class SheftServiceImpl implements SheftService {

    private static final Logger LOG = LoggerFactory.getLogger(SheftServiceImpl.class);

    private final SheftRepository sheftRepository;

    private final SheftMapper sheftMapper;

    public SheftServiceImpl(SheftRepository sheftRepository, SheftMapper sheftMapper) {
        this.sheftRepository = sheftRepository;
        this.sheftMapper = sheftMapper;
    }

    @Override
    public SheftDTO save(SheftDTO sheftDTO) {
        LOG.debug("Request to save Sheft : {}", sheftDTO);
        Sheft sheft = sheftMapper.toEntity(sheftDTO);
        sheft = sheftRepository.save(sheft);
        return sheftMapper.toDto(sheft);
    }

    @Override
    public SheftDTO update(SheftDTO sheftDTO) {
        LOG.debug("Request to update Sheft : {}", sheftDTO);
        Sheft sheft = sheftMapper.toEntity(sheftDTO);
        sheft = sheftRepository.save(sheft);
        return sheftMapper.toDto(sheft);
    }

    @Override
    public Optional<SheftDTO> partialUpdate(SheftDTO sheftDTO) {
        LOG.debug("Request to partially update Sheft : {}", sheftDTO);

        return sheftRepository
            .findById(sheftDTO.getId())
            .map(existingSheft -> {
                sheftMapper.partialUpdate(existingSheft, sheftDTO);

                return existingSheft;
            })
            .map(sheftRepository::save)
            .map(sheftMapper::toDto);
    }

    @Override
    public Page<SheftDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Shefts");
        return sheftRepository.findAll(pageable).map(sheftMapper::toDto);
    }

    @Override
    public Optional<SheftDTO> findOne(String id) {
        LOG.debug("Request to get Sheft : {}", id);
        return sheftRepository.findById(id).map(sheftMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete Sheft : {}", id);
        sheftRepository.deleteById(id);
    }
}
