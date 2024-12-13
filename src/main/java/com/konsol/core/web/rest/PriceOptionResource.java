package com.konsol.core.web.rest;

import com.konsol.core.repository.PriceOptionRepository;
import com.konsol.core.service.PriceOptionService;
import com.konsol.core.service.dto.PriceOptionDTO;
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
 * REST controller for managing {@link com.konsol.core.domain.PriceOption}.
 */
@RestController
@RequestMapping("/api/price-options")
public class PriceOptionResource {

    private static final Logger LOG = LoggerFactory.getLogger(PriceOptionResource.class);

    private static final String ENTITY_NAME = "priceOption";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PriceOptionService priceOptionService;

    private final PriceOptionRepository priceOptionRepository;

    public PriceOptionResource(PriceOptionService priceOptionService, PriceOptionRepository priceOptionRepository) {
        this.priceOptionService = priceOptionService;
        this.priceOptionRepository = priceOptionRepository;
    }

    /**
     * {@code POST  /price-options} : Create a new priceOption.
     *
     * @param priceOptionDTO the priceOptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new priceOptionDTO, or with status {@code 400 (Bad Request)} if the priceOption has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PriceOptionDTO> createPriceOption(@Valid @RequestBody PriceOptionDTO priceOptionDTO) throws URISyntaxException {
        LOG.debug("REST request to save PriceOption : {}", priceOptionDTO);
        if (priceOptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new priceOption cannot already have an ID", ENTITY_NAME, "idexists");
        }
        priceOptionDTO = priceOptionService.save(priceOptionDTO);
        return ResponseEntity.created(new URI("/api/price-options/" + priceOptionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, priceOptionDTO.getId()))
            .body(priceOptionDTO);
    }

    /**
     * {@code PUT  /price-options/:id} : Updates an existing priceOption.
     *
     * @param id the id of the priceOptionDTO to save.
     * @param priceOptionDTO the priceOptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated priceOptionDTO,
     * or with status {@code 400 (Bad Request)} if the priceOptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the priceOptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PriceOptionDTO> updatePriceOption(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody PriceOptionDTO priceOptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PriceOption : {}, {}", id, priceOptionDTO);
        if (priceOptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, priceOptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!priceOptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        priceOptionDTO = priceOptionService.update(priceOptionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, priceOptionDTO.getId()))
            .body(priceOptionDTO);
    }

    /**
     * {@code PATCH  /price-options/:id} : Partial updates given fields of an existing priceOption, field will ignore if it is null
     *
     * @param id the id of the priceOptionDTO to save.
     * @param priceOptionDTO the priceOptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated priceOptionDTO,
     * or with status {@code 400 (Bad Request)} if the priceOptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the priceOptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the priceOptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PriceOptionDTO> partialUpdatePriceOption(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody PriceOptionDTO priceOptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PriceOption partially : {}, {}", id, priceOptionDTO);
        if (priceOptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, priceOptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!priceOptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PriceOptionDTO> result = priceOptionService.partialUpdate(priceOptionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, priceOptionDTO.getId())
        );
    }

    /**
     * {@code GET  /price-options} : get all the priceOptions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of priceOptions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PriceOptionDTO>> getAllPriceOptions(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of PriceOptions");
        Page<PriceOptionDTO> page = priceOptionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /price-options/:id} : get the "id" priceOption.
     *
     * @param id the id of the priceOptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the priceOptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PriceOptionDTO> getPriceOption(@PathVariable("id") String id) {
        LOG.debug("REST request to get PriceOption : {}", id);
        Optional<PriceOptionDTO> priceOptionDTO = priceOptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(priceOptionDTO);
    }

    /**
     * {@code DELETE  /price-options/:id} : delete the "id" priceOption.
     *
     * @param id the id of the priceOptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePriceOption(@PathVariable("id") String id) {
        LOG.debug("REST request to delete PriceOption : {}", id);
        priceOptionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
