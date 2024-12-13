package com.konsol.core.service.impl;

import com.konsol.core.domain.PriceOption;
import com.konsol.core.repository.PriceOptionRepository;
import com.konsol.core.service.PriceOptionService;
import com.konsol.core.service.dto.PriceOptionDTO;
import com.konsol.core.service.mapper.PriceOptionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.konsol.core.domain.PriceOption}.
 */
@Service
public class PriceOptionServiceImpl implements PriceOptionService {

    private static final Logger LOG = LoggerFactory.getLogger(PriceOptionServiceImpl.class);

    private final PriceOptionRepository priceOptionRepository;

    private final PriceOptionMapper priceOptionMapper;

    public PriceOptionServiceImpl(PriceOptionRepository priceOptionRepository, PriceOptionMapper priceOptionMapper) {
        this.priceOptionRepository = priceOptionRepository;
        this.priceOptionMapper = priceOptionMapper;
    }

    @Override
    public PriceOptionDTO save(PriceOptionDTO priceOptionDTO) {
        LOG.debug("Request to save PriceOption : {}", priceOptionDTO);
        PriceOption priceOption = priceOptionMapper.toEntity(priceOptionDTO);
        priceOption = priceOptionRepository.save(priceOption);
        return priceOptionMapper.toDto(priceOption);
    }

    @Override
    public PriceOptionDTO update(PriceOptionDTO priceOptionDTO) {
        LOG.debug("Request to update PriceOption : {}", priceOptionDTO);
        PriceOption priceOption = priceOptionMapper.toEntity(priceOptionDTO);
        priceOption = priceOptionRepository.save(priceOption);
        return priceOptionMapper.toDto(priceOption);
    }

    @Override
    public Optional<PriceOptionDTO> partialUpdate(PriceOptionDTO priceOptionDTO) {
        LOG.debug("Request to partially update PriceOption : {}", priceOptionDTO);

        return priceOptionRepository
            .findById(priceOptionDTO.getId())
            .map(existingPriceOption -> {
                priceOptionMapper.partialUpdate(existingPriceOption, priceOptionDTO);

                return existingPriceOption;
            })
            .map(priceOptionRepository::save)
            .map(priceOptionMapper::toDto);
    }

    @Override
    public Page<PriceOptionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PriceOptions");
        return priceOptionRepository.findAll(pageable).map(priceOptionMapper::toDto);
    }

    @Override
    public Optional<PriceOptionDTO> findOne(String id) {
        LOG.debug("Request to get PriceOption : {}", id);
        return priceOptionRepository.findById(id).map(priceOptionMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete PriceOption : {}", id);
        priceOptionRepository.deleteById(id);
    }
}
