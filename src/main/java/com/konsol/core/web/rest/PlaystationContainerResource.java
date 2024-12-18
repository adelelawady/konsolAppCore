package com.konsol.core.web.rest;

import com.konsol.core.repository.PlaystationContainerRepository;
import com.konsol.core.service.PlaystationContainerService;
import com.konsol.core.service.dto.PlaystationContainerDTO;
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
 * REST controller for managing {@link com.konsol.core.domain.PlaystationContainer}.
 */
@RestController
@RequestMapping("/api/playstation-containers")
public class PlaystationContainerResource {

    private static final Logger LOG = LoggerFactory.getLogger(PlaystationContainerResource.class);

    private static final String ENTITY_NAME = "playstationContainer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlaystationContainerService playstationContainerService;

    private final PlaystationContainerRepository playstationContainerRepository;

    public PlaystationContainerResource(
        PlaystationContainerService playstationContainerService,
        PlaystationContainerRepository playstationContainerRepository
    ) {
        this.playstationContainerService = playstationContainerService;
        this.playstationContainerRepository = playstationContainerRepository;
    }

    /**
     * {@code POST  /playstation-containers} : Create a new playstationContainer.
     *
     * @param playstationContainerDTO the playstationContainerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new playstationContainerDTO, or with status {@code 400 (Bad Request)} if the playstationContainer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PlaystationContainerDTO> createPlaystationContainer(
        @Valid @RequestBody PlaystationContainerDTO playstationContainerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save PlaystationContainer : {}", playstationContainerDTO);
        if (playstationContainerDTO.getId() != null) {
            throw new BadRequestAlertException("A new playstationContainer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        playstationContainerDTO = playstationContainerService.save(playstationContainerDTO);
        return ResponseEntity
            .created(new URI("/api/playstation-containers/" + playstationContainerDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, playstationContainerDTO.getId()))
            .body(playstationContainerDTO);
    }

    /**
     * {@code PUT  /playstation-containers/:id} : Updates an existing playstationContainer.
     *
     * @param id the id of the playstationContainerDTO to save.
     * @param playstationContainerDTO the playstationContainerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playstationContainerDTO,
     * or with status {@code 400 (Bad Request)} if the playstationContainerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the playstationContainerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlaystationContainerDTO> updatePlaystationContainer(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody PlaystationContainerDTO playstationContainerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PlaystationContainer : {}, {}", id, playstationContainerDTO);
        if (playstationContainerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playstationContainerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playstationContainerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        playstationContainerDTO = playstationContainerService.update(playstationContainerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, playstationContainerDTO.getId()))
            .body(playstationContainerDTO);
    }

    /**
     * {@code PATCH  /playstation-containers/:id} : Partial updates given fields of an existing playstationContainer, field will ignore if it is null
     *
     * @param id the id of the playstationContainerDTO to save.
     * @param playstationContainerDTO the playstationContainerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playstationContainerDTO,
     * or with status {@code 400 (Bad Request)} if the playstationContainerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the playstationContainerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the playstationContainerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PlaystationContainerDTO> partialUpdatePlaystationContainer(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody PlaystationContainerDTO playstationContainerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PlaystationContainer partially : {}, {}", id, playstationContainerDTO);
        if (playstationContainerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playstationContainerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playstationContainerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlaystationContainerDTO> result = playstationContainerService.partialUpdate(playstationContainerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, playstationContainerDTO.getId())
        );
    }

    /**
     * {@code GET  /playstation-containers} : get all the playstationContainers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of playstationContainers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PlaystationContainerDTO>> getAllPlaystationContainers(@ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of PlaystationContainers");
        Page<PlaystationContainerDTO> page = playstationContainerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /playstation-containers/:id} : get the "id" playstationContainer.
     *
     * @param id the id of the playstationContainerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the playstationContainerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlaystationContainerDTO> getPlaystationContainer(@PathVariable("id") String id) {
        LOG.debug("REST request to get PlaystationContainer : {}", id);
        Optional<PlaystationContainerDTO> playstationContainerDTO = playstationContainerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(playstationContainerDTO);
    }

    /**
     * {@code DELETE  /playstation-containers/:id} : delete the "id" playstationContainer.
     *
     * @param id the id of the playstationContainerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaystationContainer(@PathVariable("id") String id) {
        LOG.debug("REST request to delete PlaystationContainer : {}", id);
        playstationContainerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
