package com.konsol.core.web.rest;

import com.konsol.core.domain.playstation.PlaystationDevice;
import com.konsol.core.repository.PlaystationDeviceRepository;
import com.konsol.core.repository.PlaystationDeviceTypeRepository;
import com.konsol.core.service.PlaystationDeviceService;
import com.konsol.core.service.PlaystationDeviceTypeService;
import com.konsol.core.service.api.dto.PsDeviceDTO;
import com.konsol.core.service.api.dto.PsDeviceType;
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

    private final PlaystationDeviceTypeService playstationDeviceTypeService;

    private final PlaystationDeviceTypeRepository playstationDeviceTypeRepository;

    public PlaystationDeviceResource(
        PlaystationDeviceService playstationDeviceService,
        PlaystationDeviceRepository playstationDeviceRepository,
        PlaystationDeviceTypeService playstationDeviceTypeService,
        PlaystationDeviceTypeRepository playstationDeviceTypeRepository
    ) {
        this.playstationDeviceService = playstationDeviceService;
        this.playstationDeviceRepository = playstationDeviceRepository;
        this.playstationDeviceTypeService = playstationDeviceTypeService;
        this.playstationDeviceTypeRepository = playstationDeviceTypeRepository;
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
    @Override
    public ResponseEntity<PsDeviceDTO> getDevice(String id) {
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

    /**
     * {@code POST  /playstation-device-types} : Create a new playstationDeviceType.
     *
     * @param psDeviceType the PsDeviceType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new PsDeviceType, or with status {@code 400 (Bad Request)} if the playstationDeviceType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    @Override
    public ResponseEntity<PsDeviceType> createPlayStationDeviceType(PsDeviceType psDeviceType) {
        LOG.debug("REST request to save PlaystationDeviceType : {}", psDeviceType);
        if (psDeviceType.getId() != null) {
            throw new BadRequestAlertException("A new playstationDeviceType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        psDeviceType = playstationDeviceTypeService.save(psDeviceType);
        try {
            return ResponseEntity
                .created(new URI("/api/playstation-device-types/" + psDeviceType.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, psDeviceType.getId()))
                .body(psDeviceType);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@code PUT  /playstation-device-types/:id} : Updates an existing playstationDeviceType.
     *
     * @param id the id of the PsDeviceType to save.
     * @param psDeviceType the PsDeviceType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated PsDeviceType,
     * or with status {@code 400 (Bad Request)} if the PsDeviceType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the PsDeviceType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Override
    public ResponseEntity<PsDeviceType> updateDeviceType(String id, PsDeviceType psDeviceType) {
        LOG.debug("REST request to update PlaystationDeviceType : {}, {}", id, psDeviceType);
        if (psDeviceType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, psDeviceType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playstationDeviceTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        psDeviceType = playstationDeviceTypeService.update(psDeviceType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, psDeviceType.getId()))
            .body(psDeviceType);
    }

    /**
     * {@code PATCH  /playstation-device-types/:id} : Partial updates given fields of an existing playstationDeviceType, field will ignore if it is null
     *
     * @param id the id of the PsDeviceType to save.
     * @param psDeviceType the PsDeviceType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated PsDeviceType,
     * or with status {@code 400 (Bad Request)} if the PsDeviceType is not valid,
     * or with status {@code 404 (Not Found)} if the PsDeviceType is not found,
     * or with status {@code 500 (Internal Server Error)} if the PsDeviceType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Override
    public ResponseEntity<PsDeviceType> partialUpdateDeviceType(String id, PsDeviceType psDeviceType) {
        LOG.debug("REST request to partial update PlaystationDeviceType partially : {}, {}", id, psDeviceType);
        if (psDeviceType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, psDeviceType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playstationDeviceTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PsDeviceType> result = playstationDeviceTypeService.partialUpdate(psDeviceType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, psDeviceType.getId())
        );
    }

    /**
     * {@code GET  /playstation-device-types} : get all the playstationDeviceTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of playstationDeviceTypes in body.
     */
    @Override
    public ResponseEntity<List<PsDeviceType>> getDevicesTypes() {
        LOG.debug("REST request to get a page of PlaystationDeviceTypes");
        List<PsDeviceType> page = playstationDeviceTypeService.findAll();
        // HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().body(page);
    }

    /**
     * {@code GET  /playstation-device-types/:id} : get the "id" playstationDeviceType.
     *
     * @param id the id of the PsDeviceType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the PsDeviceType, or with status {@code 404 (Not Found)}.
     */
    @Override
    public ResponseEntity<PsDeviceType> getDeviceType(String id) {
        LOG.debug("REST request to get PlaystationDeviceType : {}", id);
        Optional<PsDeviceType> PsDeviceType = playstationDeviceTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(PsDeviceType);
    }

    /**
     * {@code DELETE  /playstation-device-types/:id} : delete the "id" playstationDeviceType.
     *
     * @param id the id of the PsDeviceType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Override
    public ResponseEntity<PsDeviceType> deleteDeviceType(String id) {
        LOG.debug("REST request to delete PlaystationDeviceType : {}", id);
        playstationDeviceTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
