package com.konsol.core.web.rest;

import com.konsol.core.repository.PlaystationDeviceRepository;
import com.konsol.core.service.PlaystationDeviceService;
import com.konsol.core.service.dto.PlaystationDeviceDTO;
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
 * REST controller for managing {@link com.konsol.core.domain.PlaystationDevice}.
 */
@RestController
@RequestMapping("/api/playstation-devices")
public class PlaystationDeviceResource {

    private static final Logger LOG = LoggerFactory.getLogger(PlaystationDeviceResource.class);

    private static final String ENTITY_NAME = "playstationDevice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlaystationDeviceService playstationDeviceService;

    private final PlaystationDeviceRepository playstationDeviceRepository;

    public PlaystationDeviceResource(
        PlaystationDeviceService playstationDeviceService,
        PlaystationDeviceRepository playstationDeviceRepository
    ) {
        this.playstationDeviceService = playstationDeviceService;
        this.playstationDeviceRepository = playstationDeviceRepository;
    }

    /**
     * {@code POST  /playstation-devices} : Create a new playstationDevice.
     *
     * @param playstationDeviceDTO the playstationDeviceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new playstationDeviceDTO, or with status {@code 400 (Bad Request)} if the playstationDevice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PlaystationDeviceDTO> createPlaystationDevice(@Valid @RequestBody PlaystationDeviceDTO playstationDeviceDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PlaystationDevice : {}", playstationDeviceDTO);
        if (playstationDeviceDTO.getId() != null) {
            throw new BadRequestAlertException("A new playstationDevice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        playstationDeviceDTO = playstationDeviceService.save(playstationDeviceDTO);
        return ResponseEntity.created(new URI("/api/playstation-devices/" + playstationDeviceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, playstationDeviceDTO.getId()))
            .body(playstationDeviceDTO);
    }

    /**
     * {@code PUT  /playstation-devices/:id} : Updates an existing playstationDevice.
     *
     * @param id the id of the playstationDeviceDTO to save.
     * @param playstationDeviceDTO the playstationDeviceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playstationDeviceDTO,
     * or with status {@code 400 (Bad Request)} if the playstationDeviceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the playstationDeviceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlaystationDeviceDTO> updatePlaystationDevice(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody PlaystationDeviceDTO playstationDeviceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PlaystationDevice : {}, {}", id, playstationDeviceDTO);
        if (playstationDeviceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playstationDeviceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playstationDeviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        playstationDeviceDTO = playstationDeviceService.update(playstationDeviceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, playstationDeviceDTO.getId()))
            .body(playstationDeviceDTO);
    }

    /**
     * {@code PATCH  /playstation-devices/:id} : Partial updates given fields of an existing playstationDevice, field will ignore if it is null
     *
     * @param id the id of the playstationDeviceDTO to save.
     * @param playstationDeviceDTO the playstationDeviceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playstationDeviceDTO,
     * or with status {@code 400 (Bad Request)} if the playstationDeviceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the playstationDeviceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the playstationDeviceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PlaystationDeviceDTO> partialUpdatePlaystationDevice(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody PlaystationDeviceDTO playstationDeviceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PlaystationDevice partially : {}, {}", id, playstationDeviceDTO);
        if (playstationDeviceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playstationDeviceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playstationDeviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlaystationDeviceDTO> result = playstationDeviceService.partialUpdate(playstationDeviceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, playstationDeviceDTO.getId())
        );
    }

    /**
     * {@code GET  /playstation-devices} : get all the playstationDevices.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of playstationDevices in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PlaystationDeviceDTO>> getAllPlaystationDevices(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of PlaystationDevices");
        Page<PlaystationDeviceDTO> page = playstationDeviceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /playstation-devices/:id} : get the "id" playstationDevice.
     *
     * @param id the id of the playstationDeviceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the playstationDeviceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlaystationDeviceDTO> getPlaystationDevice(@PathVariable("id") String id) {
        LOG.debug("REST request to get PlaystationDevice : {}", id);
        Optional<PlaystationDeviceDTO> playstationDeviceDTO = playstationDeviceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(playstationDeviceDTO);
    }

    /**
     * {@code DELETE  /playstation-devices/:id} : delete the "id" playstationDevice.
     *
     * @param id the id of the playstationDeviceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaystationDevice(@PathVariable("id") String id) {
        LOG.debug("REST request to delete PlaystationDevice : {}", id);
        playstationDeviceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
