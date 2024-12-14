package com.konsol.core.web.rest;

import com.konsol.core.domain.playstation.PlaystationDevice;
import com.konsol.core.repository.PlaystationDeviceRepository;
import com.konsol.core.service.PlaystationDeviceService;
import com.konsol.core.service.api.dto.PsDeviceDTO;
import com.konsol.core.web.api.PlaystationApiDelegate;
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
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link PlaystationDevice}.
 */
@Service
public class PlaystationDeviceResource implements PlaystationApiDelegate {

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
     * @param psDeviceDTO the PsDeviceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new PsDeviceDTO, or with status {@code 400 (Bad Request)} if the playstationDevice has already an ID.
     */
    @Override
    public ResponseEntity<PsDeviceDTO> createPlayStationDevice(PsDeviceDTO psDeviceDTO) {
        LOG.debug("REST request to save PlaystationDevice : {}", psDeviceDTO);
        if (psDeviceDTO.getId() != null) {
            throw new BadRequestAlertException("A new playstationDevice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        psDeviceDTO = playstationDeviceService.save(psDeviceDTO);
        try {
            return ResponseEntity
                .created(new URI("/api/playstation-devices/" + psDeviceDTO.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, psDeviceDTO.getId()))
                .body(psDeviceDTO);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@code PUT  /playstation-devices/:id} : Updates an existing playstationDevice.
     *
     * @param id the id of the PsDeviceDTO to save.
     * @param psDeviceDTO the PsDeviceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated PsDeviceDTO,
     * or with status {@code 400 (Bad Request)} if the PsDeviceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the PsDeviceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Override
    public ResponseEntity<PsDeviceDTO> updateDevice(String id, PsDeviceDTO psDeviceDTO) {
        LOG.debug("REST request to update PlaystationDevice : {}, {}", id, psDeviceDTO);
        if (psDeviceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, psDeviceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playstationDeviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        psDeviceDTO = playstationDeviceService.update(psDeviceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, psDeviceDTO.getId()))
            .body(psDeviceDTO);
    }

    /**
     * {@code PATCH  /playstation-devices/:id} : Partial updates given fields of an existing playstationDevice, field will ignore if it is null
     *
     * @param id the id of the PsDeviceDTO to save.
     * @param psDeviceDTO the PsDeviceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated PsDeviceDTO,
     * or with status {@code 400 (Bad Request)} if the PsDeviceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the PsDeviceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the PsDeviceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Override
    public ResponseEntity<PsDeviceDTO> partialUpdateDevice(String id, PsDeviceDTO psDeviceDTO) {
        LOG.debug("REST request to partial update PlaystationDevice partially : {}, {}", id, psDeviceDTO);
        if (psDeviceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, psDeviceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playstationDeviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PsDeviceDTO> result = playstationDeviceService.partialUpdate(psDeviceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, psDeviceDTO.getId())
        );
    }

    /**
     * {@code GET  /playstation-devices} : get all the playstationDevices.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of playstationDevices in body.
     */
    @Override
    public ResponseEntity<List<PsDeviceDTO>> getDevices() {
        LOG.debug("REST request to get a page of PlaystationDevices");
        List<PsDeviceDTO> page = playstationDeviceService.findAll();
        // HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().body(page);
    }

    /**
     * {@code GET  /playstation-devices/:id} : get the "id" playstationDevice.
     *
     * @param id the id of the PsDeviceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the PsDeviceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PsDeviceDTO> getPlaystationDevice(@PathVariable("id") String id) {
        LOG.debug("REST request to get PlaystationDevice : {}", id);
        Optional<PsDeviceDTO> PsDeviceDTO = playstationDeviceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(PsDeviceDTO);
    }

    /**
     * {@code DELETE  /playstation-devices/:id} : delete the "id" playstationDevice.
     *
     * @param id the id of the PsDeviceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Override
    public ResponseEntity<PsDeviceDTO> deleteDevice(String id) {
        LOG.debug("REST request to delete PlaystationDevice : {}", id);
        playstationDeviceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
