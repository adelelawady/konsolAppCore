package com.konsol.core.web.rest;

import com.konsol.core.repository.SheftRepository;
import com.konsol.core.service.SheftService;
import com.konsol.core.service.api.dto.PsSessionDTO;
import com.konsol.core.service.api.dto.SheftDTO;
import com.konsol.core.service.mapper.SheftMapper;
import com.konsol.core.service.mapper.SheftMapperImpl;
import com.konsol.core.web.api.SheftsApiDelegate;
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
import org.springframework.data.domain.PageRequest;
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
 * REST controller for managing {@link com.konsol.core.domain.Sheft}.
 */
@Service
public class SheftResource implements SheftsApiDelegate {

    private static final Logger LOG = LoggerFactory.getLogger(SheftResource.class);

    private static final String ENTITY_NAME = "sheft";
    private final SheftMapper sheftMapper;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SheftService sheftService;

    private final SheftRepository sheftRepository;

    public SheftResource(SheftService sheftService, SheftRepository sheftRepository, SheftMapperImpl sheftMapper) {
        this.sheftService = sheftService;
        this.sheftRepository = sheftRepository;
        this.sheftMapper = sheftMapper;
    }

    /**
     * {@code POST  /shefts} : Create a new sheft.
     *
     * @param sheftDTO the sheftDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sheftDTO, or with status {@code 400 (Bad Request)} if the sheft has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Override
    public ResponseEntity<SheftDTO> createSheft(SheftDTO sheftDTO) {
        LOG.debug("REST request to save Sheft : {}", sheftDTO);
        if (sheftDTO.getId() != null) {
            throw new BadRequestAlertException("A new sheft cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sheftDTO = sheftService.save(sheftDTO);
        try {
            return ResponseEntity
                .created(new URI("/api/shefts/" + sheftDTO.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, sheftDTO.getId()))
                .body(sheftDTO);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@code PUT  /shefts/:id} : Updates an existing sheft.
     *
     * @param id the id of the sheftDTO to save.
     * @param sheftDTO the sheftDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sheftDTO,
     * or with status {@code 400 (Bad Request)} if the sheftDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sheftDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    /*
    @Override
    public ResponseEntity<SheftDTO> updateSheft(String id, SheftDTO sheftDTO) {
        LOG.debug("REST request to update Sheft : {}, {}", id, sheftDTO);
        if (sheftDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sheftDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sheftRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        sheftDTO = sheftService.update(sheftDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sheftDTO.getId()))
            .body(sheftDTO);
    }
*/
    /**
     * {@code PATCH  /shefts/:id} : Partial updates given fields of an existing sheft, field will ignore if it is null
     *
     * @param id the id of the sheftDTO to save.
     * @param sheftDTO the sheftDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sheftDTO,
     * or with status {@code 400 (Bad Request)} if the sheftDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sheftDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sheftDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Override
    public ResponseEntity<SheftDTO> partialUpdateSheft(String id, SheftDTO sheftDTO) {
        LOG.debug("REST request to partial update Sheft partially : {}, {}", id, sheftDTO);
        if (sheftDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sheftDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sheftRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SheftDTO> result = sheftService.partialUpdate(sheftDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sheftDTO.getId())
        );
    }

    /**
     * {@code GET  /shefts} : get all the shefts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shefts in body.
     */
    @Override
    public ResponseEntity<List<SheftDTO>> getAllShefts(Integer pagex, Integer size) {
        LOG.debug("REST request to get a page of Shefts");
        Pageable pageable = PageRequest.of(pagex, size);
        Page<SheftDTO> page = sheftService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shefts/:id} : get the "id" sheft.
     *
     * @param id the id of the sheftDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sheftDTO, or with status {@code 404 (Not Found)}.
     */
    @Override
    public ResponseEntity<SheftDTO> getSheft(String id) {
        LOG.debug("REST request to get Sheft : {}", id);
        Optional<SheftDTO> sheftDTO = sheftService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sheftDTO);
    }

    /**
     * {@code DELETE  /shefts/:id} : delete the "id" sheft.
     *
     * @param id the id of the sheftDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Override
    public ResponseEntity<Void> deleteSheft(String id) {
        LOG.debug("REST request to delete Sheft : {}", id);
        sheftService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    @Override
    public ResponseEntity<SheftDTO> getActiveSheft() {
        return ResponseEntity.ok(sheftService.getCurrentSheft());
    }

    @Override
    public ResponseEntity<SheftDTO> stopActiveSheft(Boolean print) {
        return ResponseEntity.ok(sheftMapper.toDto(sheftService.endSheft(print)));
    }

    @Override
    public ResponseEntity<SheftDTO> startSheft() {
        return ResponseEntity.ok(sheftMapper.toDto(sheftService.startSheft()));
    }

    @Override
    public ResponseEntity<List<PsSessionDTO>> getActiveSheftSessions() {
        return ResponseEntity.ok(sheftService.activeSheftSessions());
    }
}
