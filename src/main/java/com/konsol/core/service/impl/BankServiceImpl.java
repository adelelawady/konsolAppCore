package com.konsol.core.service.impl;

import com.konsol.core.domain.Bank;
import com.konsol.core.repository.BankRepository;
import com.konsol.core.service.BankService;
import com.konsol.core.service.api.dto.BankDTO;
import com.konsol.core.service.mapper.BankMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Bank}.
 */
@Service
public class BankServiceImpl implements BankService {

    private final Logger log = LoggerFactory.getLogger(BankServiceImpl.class);

    private final BankRepository bankRepository;

    private final BankMapper bankMapper;

    public BankServiceImpl(BankRepository bankRepository, BankMapper bankMapper) {
        this.bankRepository = bankRepository;
        this.bankMapper = bankMapper;
    }

    @Override
    public BankDTO save(BankDTO bankDTO) {
        log.debug("Request to save Bank : {}", bankDTO);
        Bank bank = bankMapper.toEntity(bankDTO);
        bank = bankRepository.save(bank);
        return bankMapper.toDto(bank);
    }

    @Override
    public Optional<BankDTO> update(BankDTO bankDTO) {
        log.debug("Request to partially update Bank : {}", bankDTO);

        return bankRepository
            .findById(bankDTO.getId())
            .map(existingBank -> {
                bankMapper.partialUpdate(existingBank, bankDTO);

                return existingBank;
            })
            .map(bankRepository::save)
            .map(bankMapper::toDto);
    }

    @Override
    public Page<BankDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Banks");
        return bankRepository.findAll(pageable).map(bankMapper::toDto);
    }

    @Override
    public Optional<BankDTO> findOne(String id) {
        log.debug("Request to get Bank : {}", id);
        return bankRepository.findById(id).map(bankMapper::toDto);
    }

    @Override
    public Optional<Bank> findOneDomain(String id) {
        log.debug("Request to get Bank : {}", id);
        return bankRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Bank : {}", id);
        bankRepository.deleteById(id);
    }

    @Override
    public Optional<Bank> findFirstByOrderById() {
        return bankRepository.findFirstByOrderById();
    }
}
