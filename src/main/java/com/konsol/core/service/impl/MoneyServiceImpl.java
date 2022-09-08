package com.konsol.core.service.impl;

import com.konsol.core.domain.Money;
import com.konsol.core.repository.MoneyRepository;
import com.konsol.core.service.MoneyService;
import com.konsol.core.service.dto.MoneyDTO;
import com.konsol.core.service.mapper.MoneyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Money}.
 */
@Service
public class MoneyServiceImpl implements MoneyService {

    private final Logger log = LoggerFactory.getLogger(MoneyServiceImpl.class);

    private final MoneyRepository moneyRepository;

    private final MoneyMapper moneyMapper;

    public MoneyServiceImpl(MoneyRepository moneyRepository, MoneyMapper moneyMapper) {
        this.moneyRepository = moneyRepository;
        this.moneyMapper = moneyMapper;
    }

    @Override
    public MoneyDTO save(MoneyDTO moneyDTO) {
        log.debug("Request to save Money : {}", moneyDTO);
        Money money = moneyMapper.toEntity(moneyDTO);
        money = moneyRepository.save(money);
        return moneyMapper.toDto(money);
    }

    @Override
    public MoneyDTO update(MoneyDTO moneyDTO) {
        log.debug("Request to update Money : {}", moneyDTO);
        Money money = moneyMapper.toEntity(moneyDTO);
        money = moneyRepository.save(money);
        return moneyMapper.toDto(money);
    }

    @Override
    public Optional<MoneyDTO> partialUpdate(MoneyDTO moneyDTO) {
        log.debug("Request to partially update Money : {}", moneyDTO);

        return moneyRepository
            .findById(moneyDTO.getId())
            .map(existingMoney -> {
                moneyMapper.partialUpdate(existingMoney, moneyDTO);

                return existingMoney;
            })
            .map(moneyRepository::save)
            .map(moneyMapper::toDto);
    }

    @Override
    public Page<MoneyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Monies");
        return moneyRepository.findAll(pageable).map(moneyMapper::toDto);
    }

    @Override
    public Optional<MoneyDTO> findOne(String id) {
        log.debug("Request to get Money : {}", id);
        return moneyRepository.findById(id).map(moneyMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Money : {}", id);
        moneyRepository.deleteById(id);
    }
}
