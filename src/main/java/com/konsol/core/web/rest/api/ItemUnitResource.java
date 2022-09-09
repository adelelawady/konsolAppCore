package com.konsol.core.web.rest.api;

import com.konsol.core.repository.ItemUnitRepository;
import com.konsol.core.service.ItemUnitService;
import com.konsol.core.service.api.dto.ItemUnitDTO;
import com.konsol.core.web.api.ItemUnitsApiDelegate;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.konsol.core.domain.ItemUnit}.
 */
@Service
public class ItemUnitResource implements ItemUnitsApiDelegate {

    private final Logger log = LoggerFactory.getLogger(ItemUnitResource.class);

    private static final String ENTITY_NAME = "itemUnit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemUnitService itemUnitService;

    private final ItemUnitRepository itemUnitRepository;

    public ItemUnitResource(ItemUnitService itemUnitService, ItemUnitRepository itemUnitRepository) {
        this.itemUnitService = itemUnitService;
        this.itemUnitRepository = itemUnitRepository;
    }

    /**
     * {@code POST  /item-units} : Create a new itemUnit.
     *
     * @param itemUnitDTO the itemUnitDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemUnitDTO, or with status {@code 400 (Bad Request)} if the itemUnit has already an ID.
     */
    @Override
    public ResponseEntity<ItemUnitDTO> createItemUnit(@Valid @RequestBody ItemUnitDTO itemUnitDTO) {
        log.debug("REST request to save ItemUnit : {}", itemUnitDTO);
        if (itemUnitDTO.getId() != null) {
            throw new BadRequestAlertException("A new itemUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemUnitDTO result = itemUnitService.save(itemUnitDTO);
        try {
            return ResponseEntity
                .created(new URI("/api/item-units/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                .body(result);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@code PUT  /item-units/:id} : Updates an existing itemUnit.
     *
     * @param id the id of the itemUnitDTO to save.
     * @param itemUnitDTO the itemUnitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemUnitDTO,
     * or with status {@code 400 (Bad Request)} if the itemUnitDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemUnitDTO couldn't be updated.
     */
    @Override
    public ResponseEntity<ItemUnitDTO> updateItemUnit(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody ItemUnitDTO itemUnitDTO
    ) {
        log.debug("REST request to update ItemUnit : {}, {}", id, itemUnitDTO);
        if (itemUnitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemUnitDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ItemUnitDTO result = itemUnitService.update(itemUnitDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemUnitDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /item-units/:id} : Partial updates given fields of an existing itemUnit, field will ignore if it is null
     *
     * @param id the id of the itemUnitDTO to save.
     * @param itemUnitDTO the itemUnitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemUnitDTO,
     * or with status {@code 400 (Bad Request)} if the itemUnitDTO is not valid,
     * or with status {@code 404 (Not Found)} if the itemUnitDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemUnitDTO couldn't be updated.
     */
    @Override
    public ResponseEntity<ItemUnitDTO> partialUpdateItemUnit(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody ItemUnitDTO itemUnitDTO
    ) {
        log.debug("REST request to partial update ItemUnit partially : {}, {}", id, itemUnitDTO);
        if (itemUnitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemUnitDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ItemUnitDTO> result = itemUnitService.partialUpdate(itemUnitDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemUnitDTO.getId())
        );
    }

    /**
     * {@code GET  /item-units} : get all the itemUnits.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemUnits in body.
     */
    @Override
    public ResponseEntity<List<ItemUnitDTO>> getAllItemUnits(Integer pager, Integer size, List<String> sort) {
        Pageable pageable = PageRequest.of(pager, size);
        log.debug("REST request to get a page of ItemUnits");
        Page<ItemUnitDTO> page = itemUnitService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /item-units/:id} : get the "id" itemUnit.
     *
     * @param id the id of the itemUnitDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemUnitDTO, or with status {@code 404 (Not Found)}.
     */
    @Override
    public ResponseEntity<ItemUnitDTO> getItemUnit(@PathVariable String id) {
        log.debug("REST request to get ItemUnit : {}", id);
        Optional<ItemUnitDTO> itemUnitDTO = itemUnitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemUnitDTO);
    }

    /**
     * {@code DELETE  /item-units/:id} : delete the "id" itemUnit.
     *
     * @param id the id of the itemUnitDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Override
    public ResponseEntity<Void> deleteItemUnit(@PathVariable String id) {
        log.debug("REST request to delete ItemUnit : {}", id);
        itemUnitService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
