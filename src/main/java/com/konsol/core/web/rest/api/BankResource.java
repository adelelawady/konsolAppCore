package com.konsol.core.web.rest.api;

import com.konsol.core.repository.BankRepository;
import com.konsol.core.security.AuthoritiesConstants;
import com.konsol.core.service.BankService;
import com.konsol.core.service.api.dto.*;
import com.konsol.core.service.mapper.sup.BankBalanceMapper;
import com.konsol.core.service.mapper.sup.BankTransactionsMapper;
import com.konsol.core.web.api.BanksApiDelegate;
import com.konsol.core.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.konsol.core.domain.Bank}.
 */
@Service
public class BankResource implements BanksApiDelegate {

    private final Logger log = LoggerFactory.getLogger(BankResource.class);

    private static final String ENTITY_NAME = "bank";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BankService bankService;

    private final BankRepository bankRepository;

    private final BankBalanceMapper bankBalanceMapper;

    private final BankTransactionsMapper bankTransactionsMapper;

    public BankResource(
        BankService bankService,
        BankRepository bankRepository,
        BankBalanceMapper bankBalanceMapper,
        BankTransactionsMapper bankTransactionsMapper
    ) {
        this.bankService = bankService;
        this.bankRepository = bankRepository;
        this.bankBalanceMapper = bankBalanceMapper;
        this.bankTransactionsMapper = bankTransactionsMapper;
    }

    /**
     * {@code POST  /banks} : Create a new bank.
     *
     * @param bankDTO the bankDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bankDTO, or with status {@code 400 (Bad Request)} if the bank has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    //@PostMapping("/banks")
    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.CREATE_BANK +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<BankDTO> createBank(@Valid @RequestBody BankDTO bankDTO) {
        log.debug("REST request to save Bank : {}", bankDTO);
        if (bankDTO.getId() != null) {
            throw new BadRequestAlertException("A new bank cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BankDTO result = bankService.save(bankDTO);
        try {
            return ResponseEntity
                .created(new URI("/api/banks/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                .body(result);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@code PATCH  /banks/:id} : Partial updates given fields of an existing bank, field will ignore if it is null
     *
     * @param id the id of the bankDTO to save.
     * @param bankDTO the bankDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bankDTO,
     * or with status {@code 400 (Bad Request)} if the bankDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bankDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bankDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.UPDATE_BANK +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<BankDTO> updateBank(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody BankDTO bankDTO
    ) {
        log.debug("REST request to partial update Bank partially : {}, {}", id, bankDTO);
        if (bankDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bankDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bankRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BankDTO> result = bankService.update(bankDTO);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bankDTO.getId()));
    }

    /**
     * {@code GET  /banks} : get all the banks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of banks in body.
     */
    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.VIEW_BANK +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<List<BankDTO>> getAllBanks(Integer page, Integer size, List<String> sort) {
        log.debug("REST request to get a page of Banks");
        Pageable pageable = PageRequest.of(page, size);
        Page<BankDTO> pages = bankService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pages);
        return ResponseEntity.ok().headers(headers).body(pages.getContent());
    }

    /**
     * {@code GET  /banks/:id} : get the "id" bank.
     *
     * @param id the id of the bankDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bankDTO, or with status {@code 404 (Not Found)}.
     */
    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.VIEW_BANK +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<BankDTO> getBank(@PathVariable String id) {
        log.debug("REST request to get Bank : {}", id);
        Optional<BankDTO> bankDTO = bankService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bankDTO);
    }

    /**
     * {@code DELETE  /banks/:id} : delete the "id" bank.
     *
     * @param id the id of the bankDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.DELETE_BANK +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<Void> deleteBank(@PathVariable String id) {
        log.debug("REST request to delete Bank : {}", id);
        bankService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    @Override
    public ResponseEntity<BankBalanceDTO> getBankAnalysis(String id) {
        return ResponseEntity.ok(bankBalanceMapper.toDto(bankService.calculateBankBalance(id)));
    }

    @Override
    public ResponseEntity<BankTransactionsContainer> getBankTransactions(String id, PaginationSearchModel paginationSearchModel) {
        return ResponseEntity.ok(bankService.processBankTransactions(id, paginationSearchModel));
    }
}
