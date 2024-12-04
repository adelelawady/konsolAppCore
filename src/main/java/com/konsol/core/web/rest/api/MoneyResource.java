package com.konsol.core.web.rest.api;

import com.konsol.core.repository.MoneyRepository;
import com.konsol.core.security.AuthoritiesConstants;
import com.konsol.core.service.MoneyService;
import com.konsol.core.service.api.dto.CreateMoneyDTO;
import com.konsol.core.service.api.dto.MoneyDTO;
import com.konsol.core.service.api.dto.MoniesSearchModel;
import com.konsol.core.service.api.dto.MoniesViewDTOContainer;
import com.konsol.core.service.core.query.MongoQueryService;
import com.konsol.core.web.api.MoniesApiDelegate;
import com.konsol.core.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.konsol.core.domain.Money}.
 */
@Service
public class MoneyResource implements MoniesApiDelegate {

    private final Logger log = LoggerFactory.getLogger(MoneyResource.class);

    private static final String ENTITY_NAME = "money";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MoneyService moneyService;

    private final MoneyRepository moneyRepository;

    private final MongoQueryService mongoQueryService;

    public MoneyResource(MoneyService moneyService, MoneyRepository moneyRepository, MongoQueryService mongoQueryService) {
        this.moneyService = moneyService;
        this.moneyRepository = moneyRepository;
        this.mongoQueryService = mongoQueryService;
    }

    /**
     * {@code POST  /monies} : Create a new money.
     *
     * @param createMoneyDTO the moneyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new moneyDTO, or with status {@code 400 (Bad Request)} if the money has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.CREATE_PAYMENT +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<MoneyDTO> createMoney(@Valid @RequestBody CreateMoneyDTO createMoneyDTO) {
        log.debug("REST request to save Money : {}", createMoneyDTO);

        MoneyDTO result = moneyService.createMoney(createMoneyDTO);
        try {
            return ResponseEntity
                .created(new URI("/api/monies/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                .body(result);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@code PATCH  /monies/:id} : Partial updates given fields of an existing money, field will ignore if it is null
     *
     * @param id the id of the moneyDTO to save.
     * @param moneyDTO the moneyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moneyDTO,
     * or with status {@code 400 (Bad Request)} if the moneyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the moneyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the moneyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.UPDATE_PAYMENT +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<MoneyDTO> updateMoney(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody MoneyDTO moneyDTO
    ) {
        log.debug("REST request to partial update Money partially : {}, {}", id, moneyDTO);
        if (moneyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moneyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moneyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MoneyDTO> result = moneyService.update(moneyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moneyDTO.getId())
        );
    }

    /**
     * {@code GET  /monies} : get all the monies.
     *
     * @param page the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of monies in body.
     */
    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.VIEW_PAYMENT +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<List<MoneyDTO>> getAllMonies(Integer page, Integer size, List<String> sort) {
        log.debug("REST request to get a page of Monies");
        Pageable pageable = PageRequest.of(page, size);
        Page<MoneyDTO> pages = moneyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pages);
        return ResponseEntity.ok().headers(headers).body(pages.getContent());
    }

    /**
     * {@code GET  /monies/:id} : get the "id" money.
     *
     * @param id the id of the moneyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the moneyDTO, or with status {@code 404 (Not Found)}.
     */
    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.VIEW_PAYMENT +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<MoneyDTO> getMoney(@PathVariable String id) {
        log.debug("REST request to get Money : {}", id);
        Optional<MoneyDTO> moneyDTO = moneyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(moneyDTO);
    }

    /**
     * {@code DELETE  /monies/:id} : delete the "id" money.
     *
     * @param id the id of the moneyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.DELETE_PAYMENT +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<Void> deleteMoney(@PathVariable String id) {
        log.debug("REST request to delete Money : {}", id);
        moneyService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.VIEW_PAYMENT +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<MoniesViewDTOContainer> moniesViewSearchPaginate(MoniesSearchModel moniesSearchModel) {
        return ResponseEntity.ok(mongoQueryService.moniesViewSearchPaginate(moniesSearchModel));
    }
}
