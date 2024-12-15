package com.konsol.core.web.rest.api;

import com.konsol.core.domain.Pk;
import com.konsol.core.domain.enumeration.PkKind;
import com.konsol.core.repository.ItemRepository;
import com.konsol.core.security.AuthoritiesConstants;
import com.konsol.core.service.ItemService;
import com.konsol.core.service.PkService;
import com.konsol.core.service.api.dto.*;
import com.konsol.core.service.mapper.sup.ItemAnalysisMapper;
import com.konsol.core.web.api.ItemsApi;
import com.konsol.core.web.api.ItemsApiDelegate;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

@Service
public class ItemResource implements ItemsApiDelegate {

    private final Logger log = LoggerFactory.getLogger(ItemResource.class);

    private static final String ENTITY_NAME = "item";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemService itemService;

    private final ItemRepository itemRepository;

    private final ItemAnalysisMapper itemAnalysisMapper;

    public ItemResource(ItemService itemService, ItemRepository itemRepository, ItemAnalysisMapper itemAnalysisMapper) {
        this.itemService = itemService;
        this.itemRepository = itemRepository;
        this.itemAnalysisMapper = itemAnalysisMapper;
    }

    /**
     * {@code POST  /items} : Create a new item.
     *
     * @param itemDTO the itemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemDTO, or with status {@code 400 (Bad Request)} if the item has already an ID.
     */
    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.CREATE_ITEM +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<ItemDTO> createItem(@Valid @RequestBody ItemDTO itemDTO) {
        log.debug("REST request to save Item : {}", itemDTO);
        if (itemDTO.getId() != null) {
            throw new BadRequestAlertException("A new item cannot already have an ID", ENTITY_NAME, "idexists");
        }

        ItemDTO result = itemService.create(itemDTO);
        try {
            return ResponseEntity
                .created(new URI("/api/items/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                .body(result);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@code PUT  /items/:id} : Updates an existing item.
     *
     * @param id the id of the itemDTO to save.
     * @param itemDTO the itemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemDTO,
     * or with status {@code 400 (Bad Request)} if the itemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemDTO couldn't be updated.
     */
    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.UPDATE_ITEM +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<ItemDTO> updateItem(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody ItemDTO itemDTO
    ) {
        log.debug("REST request to update Item : {}, {}", id, itemDTO);

        if (itemDTO.getId() == null) {
            if (id == null) {
                throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
            } else {
                itemDTO.id(id);
            }
        }

        if (!Objects.equals(id, itemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ItemDTO result = itemService.update(itemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /items/:id} : Partial updates given fields of an existing item, field will ignore if it is null
     *
     * @param id the id of the itemDTO to save.
     * @param itemDTO the itemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemDTO,
     * or with status {@code 400 (Bad Request)} if the itemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the itemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemDTO couldn't be updated.
     */
    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.UPDATE_ITEM +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<ItemDTO> partialUpdateItem(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody ItemDTO itemDTO
    ) {
        log.debug("REST request to partial update Item partially : {}, {}", id, itemDTO);
        if (itemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ItemDTO> result = itemService.partialUpdate(itemDTO);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemDTO.getId()));
    }

    /**
     * {@code GET  /items} : get all the items.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of items in body.
     */
    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.VIEW_ITEM +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<List<ItemDTO>> getAllItems(Integer pager, Integer size, List<String> sort, Boolean eagerload) {
        Pageable pageable = PageRequest.of(pager, size);
        log.debug("REST request to get a page of Items");
        Page<ItemDTO> page;
        if (eagerload) {
            page = itemService.findAllWithEagerRelationships(pageable);
        } else {
            page = itemService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /items/:id} : get the "id" item.
     *
     * @param id the id of the itemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemDTO, or with status {@code 404 (Not Found)}.
     */
    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.VIEW_ITEM +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<ItemDTO> getItem(@PathVariable String id) {
        log.debug("REST request to get Item : {}", id);
        Optional<ItemDTO> itemDTO = itemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemDTO);
    }

    /**
     * {@code DELETE  /items/:id} : delete the "id" item.
     *
     * @param id the id of the itemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.DELETE_ITEM +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<Void> deleteItem(@PathVariable String id) {
        log.debug("REST request to delete Item : {}", id);
        itemService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    /**
     * GET /items/categories
     * get categories from all items available
     *
     * @return OK (status code 200)
     * @see ItemsApi#getAllItemsCategories
     */
    @Override
    public ResponseEntity<List<CategoryItem>> getAllItemsCategories() {
        return ResponseEntity.ok(itemService.getAllItemCategories());
    }

    /**
     * GET /items/pk/{PkId}
     * get item by pk id
     *
     * @param pkId  (required)
     * @return OK (status code 200)
     * @see ItemsApi#getItemByPk
     */
    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.VIEW_ITEM +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<ItemDTO> getItemByPk(String pkId) {
        return ResponseEntity.ok(itemService.findOneByPk(pkId).orElse(null));
    }

    /**
     * GET /items/id/{id}/before
     * get the item before this item by id
     *
     * @param id  (required)
     * @return OK (status code 200)
     * @see ItemsApi#getItemBeforeItemById
     */
    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.VIEW_ITEM +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<ItemDTO> getItemBeforeItemById(String id) {
        return ResponseEntity.ok(itemService.getItemBeforeItemById(id).orElse(null));
    }

    /**
     * GET /items/id/{id}/after
     * get the item before this item by id
     *
     * @param id  (required)
     * @return OK (status code 200)
     * @see ItemsApi#getItemAfterItemById
     */
    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.VIEW_ITEM +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<ItemDTO> getItemAfterItemById(String id) {
        return ResponseEntity.ok(itemService.getItemAfterItemById(id).orElse(null));
    }

    /**
     * POST /items/view :
     * item view dto search and pagination and sort request
     *
     * @param paginationSearchModel  (optional)
     * @return OK (status code 200)
     * @see ItemsApi#itemsViewSearchPaginate
     */
    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.VIEW_ITEM +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<ItemViewDTOContainer> itemsViewSearchPaginate(PaginationSearchModel paginationSearchModel) {
        return ResponseEntity.ok().body(itemService.findAllItemsViewDTO(paginationSearchModel));
    }

    /**
     * DELETE /items/units/{id}/delete :
     *
     * @param id  (required)
     * @return OK (status code 200)
     * @see ItemsApi#deleteUnitItemById
     */
    @Override
    public ResponseEntity<Void> deleteUnitItemById(String id) {
        log.debug("REST request to delete ItemUnit : {}", id);
        itemService.deleteUnitItemById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    @Override
    public ResponseEntity<List<ItemUnitDTO>> getItemUnits(String id) {
        return ResponseEntity.ok().body(itemService.getItemUnits(id));
    }

    @Override
    public ResponseEntity<ItemAnalysisDTO> getItemsAnalysis(String id, ItemAnalysisSearchDTO itemAnalysisSearchDTO) {
        if (itemAnalysisSearchDTO.getStartDate() != null || itemAnalysisSearchDTO.getEndDate() != null) {
            return ResponseEntity.ok(
                itemAnalysisMapper.toDto(
                    itemService.analyzeItem(
                        itemAnalysisSearchDTO.getItemId(),
                        itemAnalysisSearchDTO.getStoreId(),
                        itemAnalysisSearchDTO.getStartDate().toInstant(),
                        itemAnalysisSearchDTO.getEndDate().toInstant()
                    )
                )
            );
        }
        return ResponseEntity.ok(
            itemAnalysisMapper.toDto(
                itemService.analyzeItem(itemAnalysisSearchDTO.getItemId(), itemAnalysisSearchDTO.getStoreId(), null, null)
            )
        );
    }

    @Override
    public ResponseEntity<ChartDataContainer> getItemCharts(String id, ChartSearchDTO chartSearchDTO) {
        return ResponseEntity.ok(
            itemService.getSalesChartData(
                chartSearchDTO.getItemId(),
                chartSearchDTO.getStartDate().toInstant(),
                chartSearchDTO.getEndDate().toInstant()
            )
        );
    }

    @Override
    public ResponseEntity<List<ItemSimpleDTO>> getItemsByCategory(String category) {
        return ResponseEntity.ok(itemService.findAllItemSimpleByCategory(category));
    }
}
