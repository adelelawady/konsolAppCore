package com.konsol.core.service.impl;

import com.konsol.core.domain.AccountUser;
import com.konsol.core.repository.AccountUserRepository;
import com.konsol.core.service.AccountUserService;
import com.konsol.core.service.dto.AccountUserDTO;
import com.konsol.core.service.mapper.AccountUserMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link AccountUser}.
 */
@Service
public class AccountUserServiceImpl implements AccountUserService {

    private final Logger log = LoggerFactory.getLogger(AccountUserServiceImpl.class);

    private final AccountUserRepository accountUserRepository;

    private final AccountUserMapper accountUserMapper;

    public AccountUserServiceImpl(AccountUserRepository accountUserRepository, AccountUserMapper accountUserMapper) {
        this.accountUserRepository = accountUserRepository;
        this.accountUserMapper = accountUserMapper;
    }

    @Override
    public AccountUserDTO save(AccountUserDTO accountUserDTO) {
        log.debug("Request to save AccountUser : {}", accountUserDTO);
        AccountUser accountUser = accountUserMapper.toEntity(accountUserDTO);
        accountUser = accountUserRepository.save(accountUser);
        return accountUserMapper.toDto(accountUser);
    }

    @Override
    public AccountUserDTO update(AccountUserDTO accountUserDTO) {
        log.debug("Request to update AccountUser : {}", accountUserDTO);
        AccountUser accountUser = accountUserMapper.toEntity(accountUserDTO);
        accountUser = accountUserRepository.save(accountUser);
        return accountUserMapper.toDto(accountUser);
    }

    @Override
    public Optional<AccountUserDTO> partialUpdate(AccountUserDTO accountUserDTO) {
        log.debug("Request to partially update AccountUser : {}", accountUserDTO);

        return accountUserRepository
            .findById(accountUserDTO.getId())
            .map(existingAccountUser -> {
                accountUserMapper.partialUpdate(existingAccountUser, accountUserDTO);

                return existingAccountUser;
            })
            .map(accountUserRepository::save)
            .map(accountUserMapper::toDto);
    }

    @Override
    public Page<AccountUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AccountUsers");
        return accountUserRepository.findAll(pageable).map(accountUserMapper::toDto);
    }

    @Override
    public Optional<AccountUserDTO> findOne(String id) {
        log.debug("Request to get AccountUser : {}", id);
        return accountUserRepository.findById(id).map(accountUserMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete AccountUser : {}", id);
        accountUserRepository.deleteById(id);
    }
}
