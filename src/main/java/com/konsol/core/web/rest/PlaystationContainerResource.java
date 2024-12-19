package com.konsol.core.web.rest;

import com.konsol.core.repository.PlaystationContainerRepository;
import com.konsol.core.service.PlaystationContainerService;
import com.konsol.core.service.api.dto.PlaystationContainer;
import com.konsol.core.web.api.PlaystationContainersApiDelegate;
import com.konsol.core.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
public class PlaystationContainerResource implements PlaystationContainersApiDelegate {

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
     * @param playstationContainer the PlaystationContainer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new PlaystationContainer, or with status {@code 400 (Bad Request)} if the playstationContainer has already an ID.
     */

    @Override
    public ResponseEntity<PlaystationContainer> createPlaystationContainer(PlaystationContainer playstationContainer) {
        LOG.debug("REST request to save PlaystationContainer : {}", playstationContainer);
        if (playstationContainer.getId() != null) {
            throw new BadRequestAlertException("A new playstationContainer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        playstationContainer = playstationContainerService.save(playstationContainer);
        try {
            return ResponseEntity
                .created(new URI("/api/playstation-containers/" + playstationContainer.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, playstationContainer.getId()))
                .body(playstationContainer);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@code PUT  /playstation-containers/:id} : Updates an existing playstationContainer.
     *
     * @param id the id of the PlaystationContainer to save.
     * @param playstationContainer the PlaystationContainer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated PlaystationContainer,
     * or with status {@code 400 (Bad Request)} if the PlaystationContainer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the PlaystationContainer couldn't be updated.
     */
    @Override
    public ResponseEntity<PlaystationContainer> updatePlaystationContainer(String id, PlaystationContainer playstationContainer) {
        LOG.debug("REST request to update PlaystationContainer : {}, {}", id, playstationContainer);
        if (playstationContainer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playstationContainer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playstationContainerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        playstationContainer = playstationContainerService.update(playstationContainer);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, playstationContainer.getId()))
            .body(playstationContainer);
    }

    /**
     * {@code PATCH  /playstation-containers/:id} : Partial updates given fields of an existing playstationContainer, field will ignore if it is null
     *
     * @param id the id of the PlaystationContainer to save.
     * @param playstationContainer the PlaystationContainer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated PlaystationContainer,
     * or with status {@code 400 (Bad Request)} if the PlaystationContainer is not valid,
     * or with status {@code 404 (Not Found)} if the PlaystationContainer is not found,
     * or with status {@code 500 (Internal Server Error)} if the PlaystationContainer couldn't be updated.
     */
    @Override
    public ResponseEntity<PlaystationContainer> partialUpdatePlaystationContainer(String id, PlaystationContainer playstationContainer) {
        LOG.debug("REST request to partial update PlaystationContainer partially : {}, {}", id, playstationContainer);
        if (playstationContainer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playstationContainer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playstationContainerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlaystationContainer> result = playstationContainerService.partialUpdate(playstationContainer);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, playstationContainer.getId())
        );
    }

    /**
     * {@code GET  /playstation-containers} : get all the playstationContainers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of playstationContainers in body.
     */
    @Override
    public ResponseEntity<List<PlaystationContainer>> getPlaystationContainers(Integer page, Integer size) {
        LOG.debug("REST request to get a page of PlaystationContainers");
        Pageable pageable = PageRequest.of(page, size);
        Page<PlaystationContainer> pagex = playstationContainerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pagex);
        return ResponseEntity.ok().headers(headers).body(pagex.getContent());
    }

    /**
     * {@code GET  /playstation-containers/:id} : get the "id" playstationContainer.
     *
     * @param id the id of the PlaystationContainer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the PlaystationContainer, or with status {@code 404 (Not Found)}.
     */

    @Override
    public ResponseEntity<PlaystationContainer> getPlaystationContainer(String id) {
        LOG.debug("REST request to get PlaystationContainer : {}", id);
        Optional<PlaystationContainer> PlaystationContainer = playstationContainerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(PlaystationContainer);
    }

    /**
     * {@code DELETE  /playstation-containers/:id} : delete the "id" playstationContainer.
     *
     * @param id the id of the PlaystationContainer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */

    @Override
    public ResponseEntity<Void> deletePlaystationContainer(String id) {
        LOG.debug("REST request to delete PlaystationContainer : {}", id);
        playstationContainerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
