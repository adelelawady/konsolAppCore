package com.konsol.core.web.rest.api;

import com.konsol.core.repository.PkRepository;
import com.konsol.core.service.PkService;
import com.konsol.core.service.dto.PkDTO;
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
 * REST controller for managing {@link com.konsol.core.domain.Pk}.
 */
@RestController
@RequestMapping("/api")
public class PkResource {

    private final Logger log = LoggerFactory.getLogger(PkResource.class);

    private static final String ENTITY_NAME = "pk";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PkService pkService;

    private final PkRepository pkRepository;

    public PkResource(PkService pkService, PkRepository pkRepository) {
        this.pkService = pkService;
        this.pkRepository = pkRepository;
    }

    /**
     * {@code POST  /pks} : Create a new pk.
     *
     * @param pkDTO the pkDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pkDTO, or with status {@code 400 (Bad Request)} if the pk has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pks")
    public ResponseEntity<PkDTO> createPk(@Valid @RequestBody PkDTO pkDTO) throws URISyntaxException {
        log.debug("REST request to save Pk : {}", pkDTO);
        if (pkDTO.getId() != null) {
            throw new BadRequestAlertException("A new pk cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PkDTO result = pkService.save(pkDTO);
        return ResponseEntity
            .created(new URI("/api/pks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /pks/:id} : Updates an existing pk.
     *
     * @param id the id of the pkDTO to save.
     * @param pkDTO the pkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pkDTO,
     * or with status {@code 400 (Bad Request)} if the pkDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pks/{id}")
    public ResponseEntity<PkDTO> updatePk(@PathVariable(value = "id", required = false) final String id, @Valid @RequestBody PkDTO pkDTO)
        throws URISyntaxException {
        log.debug("REST request to update Pk : {}, {}", id, pkDTO);
        if (pkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PkDTO result = pkService.update(pkDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pkDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /pks/:id} : Partial updates given fields of an existing pk, field will ignore if it is null
     *
     * @param id the id of the pkDTO to save.
     * @param pkDTO the pkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pkDTO,
     * or with status {@code 400 (Bad Request)} if the pkDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pkDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PkDTO> partialUpdatePk(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody PkDTO pkDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pk partially : {}, {}", id, pkDTO);
        if (pkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PkDTO> result = pkService.partialUpdate(pkDTO);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pkDTO.getId()));
    }

    /**
     * {@code GET  /pks} : get all the pks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pks in body.
     */
    @GetMapping("/pks")
    public ResponseEntity<List<PkDTO>> getAllPks(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Pks");
        Page<PkDTO> page = pkService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pks/:id} : get the "id" pk.
     *
     * @param id the id of the pkDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pkDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pks/{id}")
    public ResponseEntity<PkDTO> getPk(@PathVariable String id) {
        log.debug("REST request to get Pk : {}", id);
        Optional<PkDTO> pkDTO = pkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pkDTO);
    }

    /**
     * {@code DELETE  /pks/:id} : delete the "id" pk.
     *
     * @param id the id of the pkDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pks/{id}")
    public ResponseEntity<Void> deletePk(@PathVariable String id) {
        log.debug("REST request to delete Pk : {}", id);
        pkService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
