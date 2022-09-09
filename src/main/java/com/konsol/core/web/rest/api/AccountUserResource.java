package com.konsol.core.web.rest.api;

import com.konsol.core.repository.AccountUserRepository;
import com.konsol.core.service.AccountUserService;
import com.konsol.core.service.dto.AccountUserDTO;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.konsol.core.domain.AccountUser}.
 */
@RestController
@RequestMapping("/api")
public class AccountUserResource {

    private final Logger log = LoggerFactory.getLogger(AccountUserResource.class);

    private static final String ENTITY_NAME = "accountUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountUserService accountUserService;

    private final AccountUserRepository accountUserRepository;

    public AccountUserResource(AccountUserService accountUserService, AccountUserRepository accountUserRepository) {
        this.accountUserService = accountUserService;
        this.accountUserRepository = accountUserRepository;
    }

    /**
     * {@code POST  /account-users} : Create a new accountUser.
     *
     * @param accountUserDTO the accountUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountUserDTO, or with status {@code 400 (Bad Request)} if the accountUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/account-users")
    public ResponseEntity<AccountUserDTO> createAccountUser(@Valid @RequestBody AccountUserDTO accountUserDTO) throws URISyntaxException {
        log.debug("REST request to save AccountUser : {}", accountUserDTO);
        if (accountUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new accountUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountUserDTO result = accountUserService.save(accountUserDTO);
        return ResponseEntity
            .created(new URI("/api/account-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /account-users/:id} : Updates an existing accountUser.
     *
     * @param id the id of the accountUserDTO to save.
     * @param accountUserDTO the accountUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountUserDTO,
     * or with status {@code 400 (Bad Request)} if the accountUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/account-users/{id}")
    public ResponseEntity<AccountUserDTO> updateAccountUser(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody AccountUserDTO accountUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AccountUser : {}, {}", id, accountUserDTO);
        if (accountUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AccountUserDTO result = accountUserService.update(accountUserDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountUserDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /account-users/:id} : Partial updates given fields of an existing accountUser, field will ignore if it is null
     *
     * @param id the id of the accountUserDTO to save.
     * @param accountUserDTO the accountUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountUserDTO,
     * or with status {@code 400 (Bad Request)} if the accountUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the accountUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the accountUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/account-users/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccountUserDTO> partialUpdateAccountUser(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody AccountUserDTO accountUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccountUser partially : {}, {}", id, accountUserDTO);
        if (accountUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccountUserDTO> result = accountUserService.partialUpdate(accountUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountUserDTO.getId())
        );
    }

    /**
     * {@code GET  /account-users} : get all the accountUsers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accountUsers in body.
     */
    @GetMapping("/account-users")
    public ResponseEntity<List<AccountUserDTO>> getAllAccountUsers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of AccountUsers");
        Page<AccountUserDTO> page = accountUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /account-users/:id} : get the "id" accountUser.
     *
     * @param id the id of the accountUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/account-users/{id}")
    public ResponseEntity<AccountUserDTO> getAccountUser(@PathVariable String id) {
        log.debug("REST request to get AccountUser : {}", id);
        Optional<AccountUserDTO> accountUserDTO = accountUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountUserDTO);
    }

    /**
     * {@code DELETE  /account-users/:id} : delete the "id" accountUser.
     *
     * @param id the id of the accountUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/account-users/{id}")
    public ResponseEntity<Void> deleteAccountUser(@PathVariable String id) {
        log.debug("REST request to delete AccountUser : {}", id);
        accountUserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
