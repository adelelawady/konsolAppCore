package com.konsol.core.web.rest;

import com.konsol.core.repository.PlaystationDeviceTypeRepository;
import com.konsol.core.service.PlaystationDeviceTypeService;
import com.konsol.core.service.dto.PlaystationDeviceTypeDTO;
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
 * REST controller for managing {@link com.konsol.core.domain.PlaystationDeviceType}.
 */
@RestController
@RequestMapping("/api/playstation-device-types")
public class PlaystationDeviceTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(PlaystationDeviceTypeResource.class);

    private static final String ENTITY_NAME = "playstationDeviceType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlaystationDeviceTypeService playstationDeviceTypeService;

    private final PlaystationDeviceTypeRepository playstationDeviceTypeRepository;

    public PlaystationDeviceTypeResource(
        PlaystationDeviceTypeService playstationDeviceTypeService,
        PlaystationDeviceTypeRepository playstationDeviceTypeRepository
    ) {
        this.playstationDeviceTypeService = playstationDeviceTypeService;
        this.playstationDeviceTypeRepository = playstationDeviceTypeRepository;
    }

    /**
     * {@code POST  /playstation-device-types} : Create a new playstationDeviceType.
     *
     * @param playstationDeviceTypeDTO the playstationDeviceTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new playstationDeviceTypeDTO, or with status {@code 400 (Bad Request)} if the playstationDeviceType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PlaystationDeviceTypeDTO> createPlaystationDeviceType(
        @Valid @RequestBody PlaystationDeviceTypeDTO playstationDeviceTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save PlaystationDeviceType : {}", playstationDeviceTypeDTO);
        if (playstationDeviceTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new playstationDeviceType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        playstationDeviceTypeDTO = playstationDeviceTypeService.save(playstationDeviceTypeDTO);
        return ResponseEntity.created(new URI("/api/playstation-device-types/" + playstationDeviceTypeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, playstationDeviceTypeDTO.getId()))
            .body(playstationDeviceTypeDTO);
    }

    /**
     * {@code PUT  /playstation-device-types/:id} : Updates an existing playstationDeviceType.
     *
     * @param id the id of the playstationDeviceTypeDTO to save.
     * @param playstationDeviceTypeDTO the playstationDeviceTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playstationDeviceTypeDTO,
     * or with status {@code 400 (Bad Request)} if the playstationDeviceTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the playstationDeviceTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlaystationDeviceTypeDTO> updatePlaystationDeviceType(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody PlaystationDeviceTypeDTO playstationDeviceTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PlaystationDeviceType : {}, {}", id, playstationDeviceTypeDTO);
        if (playstationDeviceTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playstationDeviceTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playstationDeviceTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        playstationDeviceTypeDTO = playstationDeviceTypeService.update(playstationDeviceTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, playstationDeviceTypeDTO.getId()))
            .body(playstationDeviceTypeDTO);
    }

    /**
     * {@code PATCH  /playstation-device-types/:id} : Partial updates given fields of an existing playstationDeviceType, field will ignore if it is null
     *
     * @param id the id of the playstationDeviceTypeDTO to save.
     * @param playstationDeviceTypeDTO the playstationDeviceTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playstationDeviceTypeDTO,
     * or with status {@code 400 (Bad Request)} if the playstationDeviceTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the playstationDeviceTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the playstationDeviceTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PlaystationDeviceTypeDTO> partialUpdatePlaystationDeviceType(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody PlaystationDeviceTypeDTO playstationDeviceTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PlaystationDeviceType partially : {}, {}", id, playstationDeviceTypeDTO);
        if (playstationDeviceTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playstationDeviceTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playstationDeviceTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlaystationDeviceTypeDTO> result = playstationDeviceTypeService.partialUpdate(playstationDeviceTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, playstationDeviceTypeDTO.getId())
        );
    }

    /**
     * {@code GET  /playstation-device-types} : get all the playstationDeviceTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of playstationDeviceTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PlaystationDeviceTypeDTO>> getAllPlaystationDeviceTypes(
        @ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of PlaystationDeviceTypes");
        Page<PlaystationDeviceTypeDTO> page = playstationDeviceTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /playstation-device-types/:id} : get the "id" playstationDeviceType.
     *
     * @param id the id of the playstationDeviceTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the playstationDeviceTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlaystationDeviceTypeDTO> getPlaystationDeviceType(@PathVariable("id") String id) {
        LOG.debug("REST request to get PlaystationDeviceType : {}", id);
        Optional<PlaystationDeviceTypeDTO> playstationDeviceTypeDTO = playstationDeviceTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(playstationDeviceTypeDTO);
    }

    /**
     * {@code DELETE  /playstation-device-types/:id} : delete the "id" playstationDeviceType.
     *
     * @param id the id of the playstationDeviceTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaystationDeviceType(@PathVariable("id") String id) {
        LOG.debug("REST request to delete PlaystationDeviceType : {}", id);
        playstationDeviceTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
