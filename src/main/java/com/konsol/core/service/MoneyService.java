package com.konsol.core.service;

import com.konsol.core.service.api.dto.CreateMoneyDTO;
import com.konsol.core.service.api.dto.MoneyDTO;
import com.konsol.core.service.api.dto.MoniesSearchModel;
import com.konsol.core.service.api.dto.MoniesViewDTOContainer;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

/**
 * Service Interface for managing {@link com.konsol.core.domain.Money}.
 */
public interface MoneyService {
    /**
     * Save a money.
     *
     * @param moneyDTO the entity to save.
     * @return the persisted entity.
     */
    MoneyDTO save(MoneyDTO moneyDTO);

    /**
     * Partially updates a money.
     *
     * @param moneyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MoneyDTO> update(MoneyDTO moneyDTO);

    /**
     * Get all the monies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MoneyDTO> findAll(Pageable pageable);

    /**
     * Get the "id" money.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MoneyDTO> findOne(String id);

    /**
     * Delete the "id" money.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * create new money and save it to system
     * @param createMoneyDTO the entity to be created
     * @return the persisted entity.
     */
    MoneyDTO createMoney(CreateMoneyDTO createMoneyDTO, boolean addMoneyToAccount);

    /**
     * create new money and save it to system
     * @param createMoneyDTO the entity to be created
     * @return the persisted entity.
     */
    MoneyDTO createMoney(CreateMoneyDTO createMoneyDTO);
    /**
     * search money daftar
     * @param moniesSearchModel model holds all search model fields
     * @return MoniesViewDTOContainer
     */
    // MoniesViewDTOContainer moniesViewSearchPaginate(MoniesSearchModel moniesSearchModel);
}
