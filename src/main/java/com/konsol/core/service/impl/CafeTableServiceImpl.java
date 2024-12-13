package com.konsol.core.service.impl;

import com.konsol.core.domain.CafeTable;
import com.konsol.core.repository.CafeTableRepository;
import com.konsol.core.service.CafeTableService;
import com.konsol.core.service.dto.CafeTableDTO;
import com.konsol.core.service.mapper.CafeTableMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.konsol.core.domain.CafeTable}.
 */
@Service
public class CafeTableServiceImpl implements CafeTableService {

    private static final Logger LOG = LoggerFactory.getLogger(CafeTableServiceImpl.class);

    private final CafeTableRepository cafeTableRepository;

    private final CafeTableMapper cafeTableMapper;

    public CafeTableServiceImpl(CafeTableRepository cafeTableRepository, CafeTableMapper cafeTableMapper) {
        this.cafeTableRepository = cafeTableRepository;
        this.cafeTableMapper = cafeTableMapper;
    }

    @Override
    public CafeTableDTO save(CafeTableDTO cafeTableDTO) {
        LOG.debug("Request to save CafeTable : {}", cafeTableDTO);
        CafeTable cafeTable = cafeTableMapper.toEntity(cafeTableDTO);
        cafeTable = cafeTableRepository.save(cafeTable);
        return cafeTableMapper.toDto(cafeTable);
    }

    @Override
    public CafeTableDTO update(CafeTableDTO cafeTableDTO) {
        LOG.debug("Request to update CafeTable : {}", cafeTableDTO);
        CafeTable cafeTable = cafeTableMapper.toEntity(cafeTableDTO);
        cafeTable = cafeTableRepository.save(cafeTable);
        return cafeTableMapper.toDto(cafeTable);
    }

    @Override
    public Optional<CafeTableDTO> partialUpdate(CafeTableDTO cafeTableDTO) {
        LOG.debug("Request to partially update CafeTable : {}", cafeTableDTO);

        return cafeTableRepository
            .findById(cafeTableDTO.getId())
            .map(existingCafeTable -> {
                cafeTableMapper.partialUpdate(existingCafeTable, cafeTableDTO);

                return existingCafeTable;
            })
            .map(cafeTableRepository::save)
            .map(cafeTableMapper::toDto);
    }

    @Override
    public Page<CafeTableDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CafeTables");
        return cafeTableRepository.findAll(pageable).map(cafeTableMapper::toDto);
    }

    @Override
    public Optional<CafeTableDTO> findOne(String id) {
        LOG.debug("Request to get CafeTable : {}", id);
        return cafeTableRepository.findById(id).map(cafeTableMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete CafeTable : {}", id);
        cafeTableRepository.deleteById(id);
    }
}
