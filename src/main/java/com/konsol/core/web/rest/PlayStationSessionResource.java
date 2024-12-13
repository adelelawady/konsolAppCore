package com.konsol.core.web.rest;

import com.konsol.core.repository.PlayStationSessionRepository;
import com.konsol.core.service.PlayStationSessionService;
import com.konsol.core.service.dto.PlayStationSessionDTO;
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
 * REST controller for managing {@link com.konsol.core.domain.PlayStationSession}.
 */
@RestController
@RequestMapping("/api/play-station-sessions")
public class PlayStationSessionResource {

    private static final Logger LOG = LoggerFactory.getLogger(PlayStationSessionResource.class);

    private static final String ENTITY_NAME = "playStationSession";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlayStationSessionService playStationSessionService;

    private final PlayStationSessionRepository playStationSessionRepository;

    public PlayStationSessionResource(
        PlayStationSessionService playStationSessionService,
        PlayStationSessionRepository playStationSessionRepository
    ) {
        this.playStationSessionService = playStationSessionService;
        this.playStationSessionRepository = playStationSessionRepository;
    }

    /**
     * {@code POST  /play-station-sessions} : Create a new playStationSession.
     *
     * @param playStationSessionDTO the playStationSessionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new playStationSessionDTO, or with status {@code 400 (Bad Request)} if the playStationSession has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PlayStationSessionDTO> createPlayStationSession(@Valid @RequestBody PlayStationSessionDTO playStationSessionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PlayStationSession : {}", playStationSessionDTO);
        if (playStationSessionDTO.getId() != null) {
            throw new BadRequestAlertException("A new playStationSession cannot already have an ID", ENTITY_NAME, "idexists");
        }
        playStationSessionDTO = playStationSessionService.save(playStationSessionDTO);
        return ResponseEntity.created(new URI("/api/play-station-sessions/" + playStationSessionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, playStationSessionDTO.getId().toString()))
            .body(playStationSessionDTO);
    }

    /**
     * {@code PUT  /play-station-sessions/:id} : Updates an existing playStationSession.
     *
     * @param id the id of the playStationSessionDTO to save.
     * @param playStationSessionDTO the playStationSessionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playStationSessionDTO,
     * or with status {@code 400 (Bad Request)} if the playStationSessionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the playStationSessionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlayStationSessionDTO> updatePlayStationSession(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PlayStationSessionDTO playStationSessionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PlayStationSession : {}, {}", id, playStationSessionDTO);
        if (playStationSessionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playStationSessionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playStationSessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        playStationSessionDTO = playStationSessionService.update(playStationSessionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, playStationSessionDTO.getId().toString()))
            .body(playStationSessionDTO);
    }

    /**
     * {@code PATCH  /play-station-sessions/:id} : Partial updates given fields of an existing playStationSession, field will ignore if it is null
     *
     * @param id the id of the playStationSessionDTO to save.
     * @param playStationSessionDTO the playStationSessionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playStationSessionDTO,
     * or with status {@code 400 (Bad Request)} if the playStationSessionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the playStationSessionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the playStationSessionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PlayStationSessionDTO> partialUpdatePlayStationSession(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PlayStationSessionDTO playStationSessionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PlayStationSession partially : {}, {}", id, playStationSessionDTO);
        if (playStationSessionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playStationSessionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playStationSessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlayStationSessionDTO> result = playStationSessionService.partialUpdate(playStationSessionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, playStationSessionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /play-station-sessions} : get all the playStationSessions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of playStationSessions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PlayStationSessionDTO>> getAllPlayStationSessions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of PlayStationSessions");
        Page<PlayStationSessionDTO> page = playStationSessionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /play-station-sessions/:id} : get the "id" playStationSession.
     *
     * @param id the id of the playStationSessionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the playStationSessionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlayStationSessionDTO> getPlayStationSession(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PlayStationSession : {}", id);
        Optional<PlayStationSessionDTO> playStationSessionDTO = playStationSessionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(playStationSessionDTO);
    }

    /**
     * {@code DELETE  /play-station-sessions/:id} : delete the "id" playStationSession.
     *
     * @param id the id of the playStationSessionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayStationSession(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PlayStationSession : {}", id);
        playStationSessionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
