package com.konsol.core.service;

import com.konsol.core.domain.Bank;
import com.konsol.core.service.api.dto.BankDTO;
import com.konsol.core.service.dto.BankBalanceDTO;
import com.konsol.core.service.dto.BankTransactionsDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.konsol.core.domain.Bank}.
 */
public interface BankService {
    /**
     * Save a bank.
     *
     * @param bankDTO the entity to save.
     * @return the persisted entity.
     */
    BankDTO save(BankDTO bankDTO);

    /**
     * Partially updates a bank.
     *
     * @param bankDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BankDTO> update(BankDTO bankDTO);

    /**
     * Get all the banks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BankDTO> findAll(Pageable pageable);

    /**
     * Get the "id" bank.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BankDTO> findOne(String id);

    Optional<Bank> findOneDomain(String id);

    /**
     * Delete the "id" bank.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * Get the first bank ordered by id.
     *
     * @return the entity.
     */
    Optional<Bank> findFirstByOrderById();

    /**
     * Load bank movements for a specific bank.
     *
     * @param bankId the id of the bank.
     * @return list of bank movements.
     */
    List<BankTransactionsDTO> processBankTransactions(String bankId);

    /**
     * Calculate bank balance and profits.
     *
     * @param bankId the bank id
     * @return BankBalanceDTO containing total balance and profits
     */
    BankBalanceDTO calculateBankBalance(String bankId);
}
