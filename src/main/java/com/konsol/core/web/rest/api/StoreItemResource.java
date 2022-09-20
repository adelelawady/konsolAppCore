package com.konsol.core.web.rest.api;

import com.konsol.core.repository.StoreItemRepository;
import com.konsol.core.service.StoreItemService;
import com.konsol.core.service.api.dto.StoreItemDTO;
import com.konsol.core.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing {@link com.konsol.core.domain.StoreItem}.
 */
public class StoreItemResource {

    private final Logger log = LoggerFactory.getLogger(StoreItemResource.class);

    private static final String ENTITY_NAME = "storeItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StoreItemService storeItemService;

    private final StoreItemRepository storeItemRepository;

    public StoreItemResource(StoreItemService storeItemService, StoreItemRepository storeItemRepository) {
        this.storeItemService = storeItemService;
        this.storeItemRepository = storeItemRepository;
    }

    /**
     * {@code POST  /store-items} : Create a new storeItem.
     *
     * @param storeItemDTO the storeItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new storeItemDTO, or with status {@code 400 (Bad Request)} if the storeItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    public ResponseEntity<StoreItemDTO> createStoreItem(@Valid @RequestBody StoreItemDTO storeItemDTO) {
        log.debug("REST request to save StoreItem : {}", storeItemDTO);
        if (storeItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new storeItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StoreItemDTO result = storeItemService.save(storeItemDTO);
        try {
            return ResponseEntity
                .created(new URI("/api/store-items/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                .body(result);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@code PUT  /store-items/:id} : Updates an existing storeItem.
     *
     * @param id the id of the storeItemDTO to save.
     * @param storeItemDTO the storeItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storeItemDTO,
     * or with status {@code 400 (Bad Request)} if the storeItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the storeItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    public ResponseEntity<StoreItemDTO> updateStoreItem(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody StoreItemDTO storeItemDTO
    ) {
        log.debug("REST request to update StoreItem : {}, {}", id, storeItemDTO);
        if (storeItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storeItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storeItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StoreItemDTO result = storeItemService.update(storeItemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, storeItemDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /store-items/:id} : Partial updates given fields of an existing storeItem, field will ignore if it is null
     *
     * @param id the id of the storeItemDTO to save.
     * @param storeItemDTO the storeItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storeItemDTO,
     * or with status {@code 400 (Bad Request)} if the storeItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the storeItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the storeItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    public ResponseEntity<StoreItemDTO> partialUpdateStoreItem(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody StoreItemDTO storeItemDTO
    ) {
        log.debug("REST request to partial update StoreItem partially : {}, {}", id, storeItemDTO);
        if (storeItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storeItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storeItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StoreItemDTO> result = storeItemService.partialUpdate(storeItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, storeItemDTO.getId())
        );
    }

    /**
     * {@code GET  /store-items} : get all the storeItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of storeItems in body.
     */

    public ResponseEntity<List<StoreItemDTO>> getAllStoreItems(Integer pager, Integer size, List<String> sort) {
        log.debug("REST request to get a page of StoreItems");
        Pageable pageable = PageRequest.of(pager, size);
        Page<StoreItemDTO> page = storeItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /store-items/:id} : get the "id" storeItem.
     *
     * @param id the id of the storeItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the storeItemDTO, or with status {@code 404 (Not Found)}.
     */

    public ResponseEntity<StoreItemDTO> getStoreItem(@PathVariable String id) {
        log.debug("REST request to get StoreItem : {}", id);
        Optional<StoreItemDTO> storeItemDTO = storeItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(storeItemDTO);
    }

    /**
     * {@code DELETE  /store-items/:id} : delete the "id" storeItem.
     *
     * @param id the id of the storeItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */

    public ResponseEntity<Void> deleteStoreItem(@PathVariable String id) {
        log.debug("REST request to delete StoreItem : {}", id);
        storeItemService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
