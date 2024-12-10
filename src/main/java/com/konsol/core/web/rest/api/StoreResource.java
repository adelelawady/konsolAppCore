package com.konsol.core.web.rest.api;

import com.konsol.core.repository.StoreItemRepository;
import com.konsol.core.repository.StoreRepository;
import com.konsol.core.security.AuthoritiesConstants;
import com.konsol.core.service.StoreItemService;
import com.konsol.core.service.StoreService;
import com.konsol.core.service.api.dto.*;
import com.konsol.core.service.mapper.StoreItemMapper;
import com.konsol.core.web.api.StoresApi;
import com.konsol.core.web.api.StoresApiDelegate;
import com.konsol.core.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.konsol.core.domain.Store}.
 */
@Service
public class StoreResource implements StoresApiDelegate {

    private final Logger log = LoggerFactory.getLogger(StoreResource.class);

    private static final String ENTITY_NAME = "store";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StoreService storeService;

    private final StoreRepository storeRepository;

    private final StoreItemMapper storeItemMapper;

    public StoreResource(StoreService storeService, StoreRepository storeRepository, StoreItemMapper storeItemMapper) {
        this.storeService = storeService;
        this.storeRepository = storeRepository;
        this.storeItemMapper = storeItemMapper;
    }

    /**
     * {@code POST  /stores} : Create a new store.
     *
     * @param storeDTO the storeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new storeDTO, or with status {@code 400 (Bad Request)} if the store has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.CREATE_STORE +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<StoreDTO> createStore(@Valid @RequestBody StoreDTO storeDTO) {
        log.debug("REST request to save Store : {}", storeDTO);
        if (storeDTO.getId() != null) {
            throw new BadRequestAlertException("A new store cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StoreDTO result = storeService.save(storeDTO);
        try {
            return ResponseEntity
                .created(new URI("/api/stores/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                .body(result);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@code PATCH  /stores/:id} : Partial updates given fields of an existing store, field will ignore if it is null
     *
     * @param id the id of the storeDTO to save.
     * @param storeDTO the storeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storeDTO,
     * or with status {@code 400 (Bad Request)} if the storeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the storeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the storeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.UPDATE_STORE +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<StoreDTO> updateStore(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody StoreDTO storeDTO
    ) {
        log.debug("REST request to partial update Store partially : {}, {}", id, storeDTO);
        if (storeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StoreDTO> result = storeService.update(storeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, storeDTO.getId())
        );
    }

    /**
     * GET /stores
     *
     * @param pager Zero-based page index (0..N) (optional, default to 0)
     * @param size The size of the page to be returned (optional, default to 20)
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
     * @param eagerload  (optional, default to false)
     * @return OK (status code 200)
     * @see StoresApi#getAllStores
     */
    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.VIEW_STORE +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<List<StoreDTO>> getAllStores(Integer pager, Integer size, List<String> sort, Boolean eagerload) {
        log.debug("REST request to get a page of Stores");
        Pageable pageable = PageRequest.of(pager, size);
        Page<StoreDTO> page;
        if (eagerload) {
            page = storeService.findAllWithEagerRelationships(pageable);
        } else {
            page = storeService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /stores/:id} : get the "id" store.
     *
     * @param id the id of the storeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the storeDTO, or with status {@code 404 (Not Found)}.
     */
    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.VIEW_STORE +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<StoreDTO> getStore(@PathVariable String id) {
        log.debug("REST request to get Store : {}", id);
        Optional<StoreDTO> storeDTO = storeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(storeDTO);
    }

    /**
     * {@code DELETE  /stores/:id} : delete the "id" store.
     *
     * @param id the id of the storeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.DELETE_STORE +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<Void> deleteStore(@PathVariable String id) {
        log.debug("REST request to delete Store : {}", id);
        storeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    /**
     * GET /stores/names
     * return all store names with id
     *
     * @return OK (status code 200)
     */
    @Override
    public ResponseEntity<List<StoreNameDTO>> getAllStoresNames() {
        return ResponseEntity.ok().body(storeService.getAllStoresNames());
    }

    /**
     * GET /stores/item/{id}/storeItems
     * return all store items qty in all stores item availabe in with item id
     *
     * @param id  (required)
     * @return OK (status code 200)
     * @see StoresApi#getAllStoresItemsForItem
     */
    @Override
    public ResponseEntity<List<StoreItemDTO>> getAllStoresItemsForItem(String id) {
        return ResponseEntity.ok().body(storeService.getAllStoresItemsForItem(id));
    }

    /**
     * GET /stores/item/{id}/storeItems/all
     * return all store items qty in all stores with item id
     *
     * @param id  (required)
     * @return OK (status code 200)
     * @see StoresApi#getAllStoresItemsInAllStoresForItem
     */
    @Override
    public ResponseEntity<List<StoreItemDTO>> getAllStoresItemsInAllStoresForItem(String id) {
        return ResponseEntity.ok().body(storeService.getAllStoresItemsInAllStoresForItem(id));
    }

    /**
     * POST /stores/storeItems :
     * creates if not exsits or updates exsitsing store item  for selected item and store
     *
     * @param storeItemIdOnlyDTO store item contains item.id and store.id and qty required (optional)
     * @return OK (status code 200)
     * @see StoresApi#setStoreItem
     */
    @Override
    public ResponseEntity<StoreItemDTO> setStoreItem(StoreItemIdOnlyDTO storeItemIdOnlyDTO) {
        return ResponseEntity.ok().body(storeService.setStoreItem(storeItemIdOnlyDTO, true));
    }

    @Override
    public ResponseEntity<List<StoreItemDTO>> getStoresItemsForStore(String id, PaginationSearchModel paginationSearchModel) {
        return ResponseEntity
            .ok()
            .body(
                storeService
                    .getStoresItemsForStore(id, paginationSearchModel)
                    .stream()
                    .map(storeItemMapper::toDto)
                    .collect(Collectors.toList())
            );
    }
}
