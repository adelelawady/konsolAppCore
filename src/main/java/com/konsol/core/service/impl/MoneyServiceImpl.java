package com.konsol.core.service.impl;

import com.konsol.core.domain.AccountUser;
import com.konsol.core.domain.Bank;
import com.konsol.core.domain.Money;
import com.konsol.core.domain.Pk;
import com.konsol.core.domain.enumeration.MoneyKind;
import com.konsol.core.domain.enumeration.PkKind;
import com.konsol.core.repository.MoneyRepository;
import com.konsol.core.service.AccountUserService;
import com.konsol.core.service.BankService;
import com.konsol.core.service.MoneyService;
import com.konsol.core.service.PkService;
import com.konsol.core.service.api.dto.CreateMoneyDTO;
import com.konsol.core.service.api.dto.MoneyDTO;
import com.konsol.core.service.api.dto.MoniesSearchModel;
import com.konsol.core.service.api.dto.MoniesViewDTOContainer;
import com.konsol.core.service.dto.AccountUserDTO;
import com.konsol.core.service.mapper.MoneyMapper;
import com.konsol.core.web.rest.api.errors.MoneyException;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import org.bson.BsonNull;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Money}.
 */
@Service
public class MoneyServiceImpl implements MoneyService {

    private final Logger log = LoggerFactory.getLogger(MoneyServiceImpl.class);

    private final MoneyRepository moneyRepository;

    private final MoneyMapper moneyMapper;

    @Autowired
    public final MongoTemplate mongoTemplate;

    private final PkService pkService;

    private final AccountUserService accountUserService;

    private final BankService bankService;

    public MoneyServiceImpl(
        MoneyRepository moneyRepository,
        MoneyMapper moneyMapper,
        MongoTemplate mongoTemplate,
        PkService pkService,
        AccountUserService accountUserService,
        BankService bankService
    ) {
        this.moneyRepository = moneyRepository;
        this.moneyMapper = moneyMapper;
        this.mongoTemplate = mongoTemplate;
        this.pkService = pkService;
        this.accountUserService = accountUserService;
        this.bankService = bankService;
    }

    @Override
    public MoneyDTO save(MoneyDTO moneyDTO) {
        log.debug("Request to save Money : {}", moneyDTO);
        Money money = moneyMapper.toEntity(moneyDTO);
        money = moneyRepository.save(money);
        return moneyMapper.toDto(money);
    }

    @Override
    public Optional<MoneyDTO> update(MoneyDTO moneyDTO) {
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

    @Override
    public MoneyDTO createMoney(CreateMoneyDTO createMoneyDTO, boolean addMoneyToAccount) {
        log.debug("Request to create Money : {}", createMoneyDTO);

        /**
         * Make sure the kind hs value
         */

        if (createMoneyDTO.getKind().getValue().isEmpty()) {
            throw new MoneyException("Create Money Exception", "Money Kind Not Found");
        }

        /**
         * Make sure the money has account or a bank available
         */
        if (
            (createMoneyDTO.getAccountId() == null || createMoneyDTO.getAccountId().isEmpty()) &&
            (createMoneyDTO.getBankId() == null || createMoneyDTO.getBankId().isEmpty())
        ) {
            throw new MoneyException("Create Money Exception", "account or bank must be exist");
        }

        /**
         * create the entitiy
         * clear id to null
         */

        Money money = new Money();
        money.setId(null);

        if (createMoneyDTO.getMoneyOut() != null) {
            money.setMoneyOut(createMoneyDTO.getMoneyOut());
        } else {
            money.setMoneyOut(new BigDecimal(0));
        }

        if (createMoneyDTO.getMoneyIn() != null) {
            money.setMoneyIn(createMoneyDTO.getMoneyIn());
        } else {
            money.setMoneyIn(new BigDecimal(0));
        }

        money.setKind(MoneyKind.valueOf(createMoneyDTO.getKind().getValue()));
        money.setDetails(createMoneyDTO.getDetails());

        if (!(createMoneyDTO.getAccountId() == null || createMoneyDTO.getAccountId().isEmpty())) {
            Optional<AccountUser> optionalAccountUser = accountUserService.findOneDomain(createMoneyDTO.getAccountId());
            if (optionalAccountUser.isPresent()) {
                money.setAccount(optionalAccountUser.get());
            } else {
                throw new MoneyException("Create Money Exception", "Account Not Found");
            }
        }

        if (!(createMoneyDTO.getBankId() == null || createMoneyDTO.getBankId().isEmpty())) {
            Optional<Bank> bankOptional = bankService.findOneDomain(createMoneyDTO.getBankId());
            if (bankOptional.isPresent()) {
                money.setBank(bankOptional.get());
            } else {
                throw new MoneyException("Create Money Exception", "Bank Not Found");
            }
        }

        /**
         * Generate pk
         */
        Pk pk = pkService.generatePkEntity(PkKind.Money);
        money.setPk(pk.getValue().toString());

        if (addMoneyToAccount && !(createMoneyDTO.getAccountId() == null || createMoneyDTO.getAccountId().isEmpty())) {
            Optional<AccountUser> optionalAccountUser = accountUserService.findOneDomain(createMoneyDTO.getAccountId());
            if (optionalAccountUser.isPresent()) {
                AccountUser accountUser = optionalAccountUser.get();
                switch (createMoneyDTO.getKind()) {
                    case PAYMENT:
                        {
                            accountUserService.addAccountBalance(accountUser.getId(), money.getMoneyIn());
                        }
                    case RECEIPT:
                        {
                            accountUserService.subtractAccountBalance(accountUser.getId(), money.getMoneyOut());
                        }
                }
                money.setAccount(accountUserService.findOneDomain(createMoneyDTO.getAccountId()).get());
            }
        }

        money = moneyRepository.save(money);
        return moneyMapper.toDto(money);
    }

    @Override
    public MoneyDTO createMoney(CreateMoneyDTO createMoneyDTO) {
        return this.createMoney(createMoneyDTO, true);
    }
}
