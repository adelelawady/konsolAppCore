package com.konsol.core.service.impl;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.konsol.core.domain.AccountUser;
import com.konsol.core.domain.Bank;
import com.konsol.core.domain.Money;
import com.konsol.core.domain.Settings;
import com.konsol.core.domain.enumeration.InvoiceKind;
import com.konsol.core.domain.enumeration.MoneyKind;
import com.konsol.core.repository.AccountUserRepository;
import com.konsol.core.repository.InvoiceRepository;
import com.konsol.core.repository.MoneyRepository;
import com.konsol.core.service.AccountUserService;
import com.konsol.core.service.InvoiceService;
import com.konsol.core.service.MoneyService;
import com.konsol.core.service.SettingService;
import com.konsol.core.service.api.dto.*;
import com.konsol.core.service.core.query.MongoQueryService;
import com.konsol.core.service.dto.AccountTransactionsContainer;
import com.konsol.core.service.dto.AccountTransactionsDTO;
import com.konsol.core.service.dto.BankTransactionsDTO;
import com.konsol.core.service.exception.BankNotFoundException;
import com.konsol.core.service.mapper.AccountUserMapper;
import com.konsol.core.service.mapper.sup.AccountTransactionsMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

/**
 * Service Implementation for managing {@link AccountUser}.
 */
@Component
@Primary
public class AccountUserServiceImpl implements AccountUserService {

    private final Logger log = LoggerFactory.getLogger(AccountUserServiceImpl.class);

    private final AccountUserRepository accountUserRepository;

    private final AccountUserMapper accountUserMapper;

    private final MongoQueryService mongoQueryService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MoneyRepository moneyRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    MoneyRepository MoneyRepository;

    private final SettingService settingService;
    private final AccountTransactionsMapper accountTransactionsMapper;

    public AccountUserServiceImpl(
        AccountUserRepository accountUserRepository,
        AccountUserMapper accountUserMapper,
        MongoQueryService mongoQueryService,
        SettingService settingService,
        AccountTransactionsMapper accountTransactionsMapper
    ) {
        this.accountUserRepository = accountUserRepository;
        this.accountUserMapper = accountUserMapper;
        this.mongoQueryService = mongoQueryService;
        this.settingService = settingService;
        this.accountTransactionsMapper = accountTransactionsMapper;
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
        Settings settings = settingService.getSettings();
        return accountUserRepository
            .findById(accountUserDTO.getId())
            .map(existingAccountUser -> {
                // Calculate balance differences before update
                BigDecimal oldBalanceIn = existingAccountUser.getBalanceIn() != null ? existingAccountUser.getBalanceIn() : BigDecimal.ZERO;
                BigDecimal oldBalanceOut = existingAccountUser.getBalanceOut() != null
                    ? existingAccountUser.getBalanceOut()
                    : BigDecimal.ZERO;

                // Apply the update
                accountUserMapper.partialUpdate(existingAccountUser, accountUserDTO);

                // Calculate new balances
                BigDecimal newBalanceIn = existingAccountUser.getBalanceIn() != null ? existingAccountUser.getBalanceIn() : BigDecimal.ZERO;
                BigDecimal newBalanceOut = existingAccountUser.getBalanceOut() != null
                    ? existingAccountUser.getBalanceOut()
                    : BigDecimal.ZERO;

                // Calculate differences
                BigDecimal balanceInDiff = newBalanceIn.subtract(oldBalanceIn);
                BigDecimal balanceOutDiff = newBalanceOut.subtract(oldBalanceOut);

                // Validate that only one balance is changing
                if (balanceInDiff.compareTo(BigDecimal.ZERO) != 0 && balanceOutDiff.compareTo(BigDecimal.ZERO) != 0) {
                    throw new IllegalStateException("Cannot modify both BalanceIn and BalanceOut simultaneously");
                }

                // Create money transactions for balance changes
                if (balanceInDiff.compareTo(BigDecimal.ZERO) != 0) {
                    // Validate that BalanceOut is zero
                    if (newBalanceOut.compareTo(BigDecimal.ZERO) != 0) {
                        throw new IllegalStateException("BalanceOut must be zero when modifying BalanceIn");
                    }

                    Money moneyDTO = new Money();
                    moneyDTO.setAccount(existingAccountUser);
                    moneyDTO.setMoneyIn(balanceInDiff.abs());
                    moneyDTO.setMoneyOut(BigDecimal.ZERO);
                    moneyDTO.setKind(MoneyKind.PAYMENT);
                    moneyDTO.setDetails("Balance In Update By : ");
                    // moneyDTO.setBank(settings.getMAIN_SELECTED_BANK_ID());
                    MoneyRepository.save(moneyDTO);

                    log.info(
                        "Account {} balance_in changed by {}: {} -> {}",
                        existingAccountUser.getId(),
                        balanceInDiff,
                        oldBalanceIn,
                        newBalanceIn
                    );
                }

                if (balanceOutDiff.compareTo(BigDecimal.ZERO) != 0) {
                    // Validate that BalanceIn is zero
                    if (newBalanceIn.compareTo(BigDecimal.ZERO) != 0) {
                        throw new IllegalStateException("BalanceIn must be zero when modifying BalanceOut");
                    }

                    Money moneyDTO = new Money();
                    moneyDTO.setAccount(existingAccountUser);
                    moneyDTO.setMoneyIn(BigDecimal.ZERO);
                    moneyDTO.setMoneyOut(balanceOutDiff.abs());
                    moneyDTO.setKind(MoneyKind.RECEIPT);
                    moneyDTO.setDetails("Balance Out Update By : ");
                    // moneyDTO.setBankId(settings.getMAIN_SELECTED_BANK_ID());
                    moneyRepository.save(moneyDTO);

                    log.info(
                        "Account {} balance_out changed by {}: {} -> {}",
                        existingAccountUser.getId(),
                        balanceOutDiff,
                        oldBalanceOut,
                        newBalanceOut
                    );
                }

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
        return this.mongoQueryService.accountUserSearchPaginateQuery(accountUserSearchModel);
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

    @Override
    public com.konsol.core.service.api.dto.AccountTransactionsContainer processAccountTransactions(
        String accountId,
        PaginationSearchModel paginationSearchModel
    ) {
        log.debug("Request to load bank movements for bank ID: {} with pagination: {}", accountId, paginationSearchModel);

        // Validate bank exists
        Optional<AccountUser> accountUser = accountUserRepository.findById(accountId);
        if (accountUser.isEmpty()) {
            log.error("accountUser not found with ID: {}", accountId);
            throw new BankNotFoundException(String.format("accountUser with id %s not found", accountId));
        }

        try {
            // Determine sort direction
            Sort.Direction sortDirection = paginationSearchModel.getSortOrder() != null
                ? Sort.Direction.fromString(paginationSearchModel.getSortOrder())
                : Sort.Direction.ASC;
            String sortField = paginationSearchModel.getSortField() != null ? paginationSearchModel.getSortField() : "created_date";

            // Calculate skip value for pagination
            int skip = (paginationSearchModel.getPage()) * paginationSearchModel.getSize();

            List<AccountTransactionsDTO> movements = new ArrayList<>();

            // Create pipeline for Money collection
            List<AggregationOperation> moneyOperations = new ArrayList<>(
                Arrays.asList(
                    // Match documents for the specific bank
                    match(Criteria.where("account._id").is(new ObjectId(accountId))),
                    // Project the required fields
                    project()
                        .and("_id")
                        .as("id")
                        .and("pk")
                        .as("pk")
                        .andExpression("{ $toString: '$bank.$id' }")
                        .as("bankId")
                        .andExpression("'MONEY'")
                        .as("sourceType")
                        .and(ConditionalOperators.ifNull("kind").then("UNKNOWN"))
                        .as("sourceKind")
                        .and("_id")
                        .as("sourceId")
                        .and("pk")
                        .as("sourcePk")
                        .and(ConditionalOperators.ifNull("money_in").then(0))
                        .as("moneyIn")
                        .and(ConditionalOperators.ifNull("money_out").then(0))
                        .as("moneyOut")
                        .and("details")
                        .as("details")
                        .and("created_date")
                        .as("createdDate")
                        .andExpression("{ $toString: '$account._id' }")
                        .as("accountId"),
                    // Add sort
                    sort(Sort.by(sortDirection, "createdDate"))
                )
            );

            // Create pipeline for Invoice collection
            List<AggregationOperation> invoiceOperations = new ArrayList<>(
                Arrays.asList(
                    // Match documents for the specific bank
                    match(Criteria.where("account.$id").is(new ObjectId(accountId))),
                    // Project the required fields
                    project()
                        .and("_id")
                        .as("id")
                        .and("pk")
                        .as("pk")
                        .andExpression("{ $toString: '$bank.$id' }")
                        .as("bankId")
                        .andExpression("'INVOICE'")
                        .as("sourceType")
                        .and(ConditionalOperators.ifNull("kind").then("UNKNOWN"))
                        .as("sourceKind")
                        .and("_id")
                        .as("sourceId")
                        .and("pk")
                        .as("sourcePk")
                        .and(ConditionalOperators.ifNull("net_price").then(0))
                        .as("moneyIn")
                        .and(ConditionalOperators.ifNull("net_cost").then(0))
                        .as("moneyOut")
                        .and("details")
                        .as("details")
                        .and("created_date")
                        .as("createdDate")
                        .andExpression("{ $toString: '$account._id' }")
                        .as("accountId"),
                    // Add sort
                    sort(Sort.by(sortDirection, "createdDate"))
                )
            );

            // Execute Money aggregation
            movements.addAll(
                mongoTemplate.aggregate(newAggregation(moneyOperations), "monies", AccountTransactionsDTO.class).getMappedResults()
            );

            // Execute Invoice aggregation
            movements.addAll(
                mongoTemplate.aggregate(newAggregation(invoiceOperations), "invoices", AccountTransactionsDTO.class).getMappedResults()
            );

            // Sort the combined results by createdDate
            movements.sort((m1, m2) -> {
                if (m1.getCreatedDate() == null && m2.getCreatedDate() == null) return 0;
                if (m1.getCreatedDate() == null) return 1;
                if (m2.getCreatedDate() == null) return -1;
                return sortDirection == Sort.Direction.ASC
                    ? m2.getCreatedDate().compareTo(m1.getCreatedDate())
                    : m1.getCreatedDate().compareTo(m2.getCreatedDate());
            });

            int movementsTotalSize = movements.size();
            // Apply pagination to the combined results
            int toIndex = Math.min(skip + paginationSearchModel.getSize(), movements.size());

            // Check if fromIndex is within bounds
            if (skip < movements.size()) {
                movements = movements.subList(skip, toIndex);
            } else {
                movements = new ArrayList<>();
            }
            log.debug("Successfully loaded {} account movements for account ID: {}", movements.size(), accountId);

            com.konsol.core.service.api.dto.AccountTransactionsContainer accountTransactionsContainer = new com.konsol.core.service.api.dto.AccountTransactionsContainer();
            accountTransactionsContainer.setResult(movements.stream().map(accountTransactionsMapper::toDto).collect(Collectors.toList()));
            accountTransactionsContainer.setTotal((long) movementsTotalSize);
            return accountTransactionsContainer;
        } catch (Exception e) {
            log.error("Error loading account movements for account ID {}: {}", accountId, e.getMessage());
            throw new RuntimeException("Error loading account movements", e);
        }
    }
}
