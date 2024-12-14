package com.konsol.core.web.rest;

import com.konsol.core.repository.CafeTableRepository;
import com.konsol.core.service.CafeTableService;
import com.konsol.core.service.dto.CafeTableDTO;
import com.konsol.core.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.konsol.core.domain.CafeTable}.
 */
@RestController
@RequestMapping("/api/cafe-tables")
public class CafeTableResource {

    private static final Logger LOG = LoggerFactory.getLogger(CafeTableResource.class);

    private static final String ENTITY_NAME = "cafeTable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CafeTableService cafeTableService;

    private final CafeTableRepository cafeTableRepository;

    public CafeTableResource(CafeTableService cafeTableService, CafeTableRepository cafeTableRepository) {
        this.cafeTableService = cafeTableService;
        this.cafeTableRepository = cafeTableRepository;
    }

    /**
     * {@code POST  /cafe-tables} : Create a new cafeTable.
     *
     * @param cafeTableDTO the cafeTableDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cafeTableDTO, or with status {@code 400 (Bad Request)} if the cafeTable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CafeTableDTO> createCafeTable(@Valid @RequestBody CafeTableDTO cafeTableDTO) throws URISyntaxException {
        LOG.debug("REST request to save CafeTable : {}", cafeTableDTO);
        if (cafeTableDTO.getId() != null) {
            throw new BadRequestAlertException("A new cafeTable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cafeTableDTO = cafeTableService.save(cafeTableDTO);
        return ResponseEntity.created(new URI("/api/cafe-tables/" + cafeTableDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cafeTableDTO.getId()))
            .body(cafeTableDTO);
    }

    /**
     * {@code PUT  /cafe-tables/:id} : Updates an existing cafeTable.
     *
     * @param id the id of the cafeTableDTO to save.
     * @param cafeTableDTO the cafeTableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cafeTableDTO,
     * or with status {@code 400 (Bad Request)} if the cafeTableDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cafeTableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CafeTableDTO> updateCafeTable(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody CafeTableDTO cafeTableDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CafeTable : {}, {}", id, cafeTableDTO);
        if (cafeTableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cafeTableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cafeTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cafeTableDTO = cafeTableService.update(cafeTableDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cafeTableDTO.getId()))
            .body(cafeTableDTO);
    }

    /**
     * {@code PATCH  /cafe-tables/:id} : Partial updates given fields of an existing cafeTable, field will ignore if it is null
     *
     * @param id the id of the cafeTableDTO to save.
     * @param cafeTableDTO the cafeTableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cafeTableDTO,
     * or with status {@code 400 (Bad Request)} if the cafeTableDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cafeTableDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cafeTableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CafeTableDTO> partialUpdateCafeTable(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody CafeTableDTO cafeTableDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CafeTable partially : {}, {}", id, cafeTableDTO);
        if (cafeTableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cafeTableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cafeTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CafeTableDTO> result = cafeTableService.partialUpdate(cafeTableDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cafeTableDTO.getId())
        );
    }

    /**
     * {@code GET  /cafe-tables} : get all the cafeTables.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cafeTables in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CafeTableDTO>> getAllCafeTables(@ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of CafeTables");
        Page<CafeTableDTO> page = cafeTableService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cafe-tables/:id} : get the "id" cafeTable.
     *
     * @param id the id of the cafeTableDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cafeTableDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CafeTableDTO> getCafeTable(@PathVariable("id") String id) {
        LOG.debug("REST request to get CafeTable : {}", id);
        Optional<CafeTableDTO> cafeTableDTO = cafeTableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cafeTableDTO);
    }

    /**
     * {@code DELETE  /cafe-tables/:id} : delete the "id" cafeTable.
     *
     * @param id the id of the cafeTableDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCafeTable(@PathVariable("id") String id) {
        LOG.debug("REST request to delete CafeTable : {}", id);
        cafeTableService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
