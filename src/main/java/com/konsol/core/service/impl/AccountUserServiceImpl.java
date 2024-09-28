package com.konsol.core.service.impl;

import com.konsol.core.domain.AccountUser;
import com.konsol.core.repository.AccountUserRepository;
import com.konsol.core.service.AccountUserService;
import com.konsol.core.service.api.dto.AccountUserContainer;
import com.konsol.core.service.api.dto.AccountUserDTO;
import com.konsol.core.service.api.dto.AccountUserSearchModel;
import com.konsol.core.service.api.dto.CreateAccountUserDTO;
import com.konsol.core.service.core.query.MongoQueryService;
import com.konsol.core.service.mapper.AccountUserMapper;
import java.math.BigDecimal;
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

    private final MongoQueryService mongoQueryService;

    public AccountUserServiceImpl(
        AccountUserRepository accountUserRepository,
        AccountUserMapper accountUserMapper,
        MongoQueryService mongoQueryService
    ) {
        this.accountUserRepository = accountUserRepository;
        this.accountUserMapper = accountUserMapper;
        this.mongoQueryService = mongoQueryService;
    }

    /**
     * Saves the given AccountUserDTO by converting it to an entity, saving it to the repository,
     * and then converting the saved entity back to a DTO.
     *
     * @param accountUserDTO The AccountUserDTO to be saved
     * @return The saved AccountUserDTO
     */
    @Override
    public AccountUserDTO save(AccountUserDTO accountUserDTO) {
        log.debug("Request to save AccountUser : {}", accountUserDTO);
        AccountUser accountUser = accountUserMapper.toEntity(accountUserDTO);
        accountUser = accountUserRepository.save(accountUser);
        return accountUserMapper.toDto(accountUser);
    }

    /**
     * Saves the given AccountUser entity.
     *
     * @param accountUser The AccountUser entity to be saved
     * @return The saved AccountUser entity
     */
    @Override
    public AccountUser save(AccountUser accountUser) {
        return accountUserRepository.save(accountUser);
    }

    /**
     * Creates a new account user based on the provided data.
     *
     * @param createAccountUserDTO The data to create the account user
     * @return The created account user DTO
     */
    @Override
    public AccountUserDTO create(CreateAccountUserDTO createAccountUserDTO) {
        log.debug("Request to save AccountUser : {}", createAccountUserDTO);

        AccountUser accountUser = accountUserMapper.fromCreateAccountUser(createAccountUserDTO);

        accountUser = accountUserRepository.save(accountUser);
        return accountUserMapper.toDto(accountUser);
    }

    /**
     * Updates an AccountUser entity partially based on the provided AccountUserDTO.
     *
     * @param accountUserDTO The AccountUserDTO containing the updated information.
     * @return An Optional containing the updated AccountUserDTO if found and updated, otherwise empty.
     */
    @Override
    public Optional<AccountUserDTO> update(AccountUserDTO accountUserDTO) {
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
    public Optional<AccountUser> findOneDomain(String id) {
        log.debug("Request to get AccountUser : {}", id);
        return accountUserRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete AccountUser : {}", id);
        accountUserRepository.deleteById(id);
    }

    /**
     * Do a monog service query for searching all account users
     * @param accountUserSearchModel model holds all search model fields
     * @return AccountUserContainer List of Account User [result]
     */
    @Override
    public AccountUserContainer accountUserSearchPaginate(AccountUserSearchModel accountUserSearchModel) {
        return this.mongoQueryService.accountUserSearchPaginate(accountUserSearchModel);
    }

    @Override
    public void addAccountBalance(String accountId, BigDecimal value) {
        Optional<AccountUser> optionalAccountUser = this.findOneDomain(accountId);
        if (optionalAccountUser.isPresent()) {
            AccountUser accountUser = optionalAccountUser.get();

            if (accountUser.getBalanceOut().compareTo(new BigDecimal(0)) > 0) { // >
                /**
                 * has money out
                 * remove from money out
                 */
                if (accountUser.getBalanceOut().compareTo(value) >= 0) {
                    accountUser.balanceOut(accountUser.getBalanceOut().subtract(value));
                } else {
                    BigDecimal afterSubtract = value.subtract(accountUser.getBalanceOut());
                    accountUser.balanceOut(new BigDecimal(0));
                    accountUser.balanceIn(afterSubtract);
                }
            } else {
                /**
                 * has no money out
                 * add to money in
                 */
                accountUser.balanceIn(accountUser.getBalanceIn().add(value));
            }
            this.save(accountUser);
        }
    }

    @Override
    public void subtractAccountBalance(String accountId, BigDecimal value) {
        Optional<AccountUser> optionalAccountUser = this.findOneDomain(accountId);
        if (optionalAccountUser.isPresent()) {
            AccountUser accountUser = optionalAccountUser.get();

            if (accountUser.getBalanceIn().compareTo(new BigDecimal(0)) > 0) { // >
                /**
                 * has money In
                 * remove from money In
                 */

                if (accountUser.getBalanceIn().compareTo(value) >= 0) {
                    accountUser.balanceIn(accountUser.getBalanceIn().subtract(value));
                } else {
                    BigDecimal afterSubtract = value.subtract(accountUser.getBalanceIn());
                    accountUser.balanceIn(new BigDecimal(0));
                    accountUser.balanceOut(afterSubtract);
                }
            } else {
                /**
                 * has no money in
                 * add to money Out
                 */

                accountUser.balanceOut(accountUser.getBalanceOut().add(value));
            }
            this.save(accountUser);
        }
    }
}
