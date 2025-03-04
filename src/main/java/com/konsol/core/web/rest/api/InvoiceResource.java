package com.konsol.core.web.rest.api;

import com.konsol.core.domain.Invoice;
import com.konsol.core.domain.enumeration.InvoiceKind;
import com.konsol.core.repository.InvoiceRepository;
import com.konsol.core.security.AuthoritiesConstants;
import com.konsol.core.service.InvoiceService;
import com.konsol.core.service.UserService;
import com.konsol.core.service.api.dto.*;
import com.konsol.core.web.api.InvoicesApiDelegate;
import com.konsol.core.web.rest.errors.BadRequestAlertException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.konsol.core.domain.Invoice}.
 */
@Service
public class InvoiceResource implements InvoicesApiDelegate {

    private final Logger log = LoggerFactory.getLogger(InvoiceResource.class);

    private static final String ENTITY_NAME = "invoice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceService invoiceService;

    private final InvoiceRepository invoiceRepository;

    public InvoiceResource(InvoiceService invoiceService, InvoiceRepository invoiceRepository) {
        this.invoiceService = invoiceService;
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * {@code PATCH  /invoices/:id} : Partial updates given fields of an existing invoice, field will ignore if it is null
     *
     * @param id the id of the invoiceDTO to save.
     * @param invoiceUpdateDTO the invoiceUpdateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.UPDATE_INVOICE +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<InvoiceViewSimpleDTO> updateInvoice(String id, InvoiceUpdateDTO invoiceUpdateDTO) {
        log.debug("REST request to partial update Invoice partially : {}, {}", id, invoiceUpdateDTO);
        if (invoiceUpdateDTO.getId() == null) {
            if (id == null) {
                throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
            } else {
                invoiceUpdateDTO.id(id);
            }
        }
        if (!Objects.equals(id, invoiceUpdateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        invoiceRepository
            .findById(id)
            .ifPresent(invoice -> {
                switch (invoice.getKind()) {
                    case SALE:
                        UserService.checkAuthority(AuthoritiesConstants.UPDATE_SALE);
                    case PURCHASE:
                        UserService.checkAuthority(AuthoritiesConstants.UPDATE_PURCHASE);
                }
            });

        Invoice result = invoiceService.updateInvoice(invoiceUpdateDTO);

        return ResponseEntity.ok(invoiceService.getMapper().toInvoiceViewSimpleDTO(result));
    }

    /**
     * {@code GET  /invoices/:id} : get the "id" invoice.
     *
     * @param id the id of the invoiceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceDTO, or with status {@code 404 (Not Found)}.
     */
    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.VIEW_INVOICE +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<InvoiceDTO> getInvoice(@PathVariable String id) {
        log.debug("REST request to get Invoice : {}", id);
        Optional<InvoiceDTO> invoiceDTO = invoiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceDTO);
    }

    /**
     * {@code DELETE  /invoices/:id} : delete the "id" invoice.
     *
     * @param id the id of the invoiceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.DELETE_INVOICE +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<Void> deleteInvoice(@PathVariable String id) {
        log.debug("REST request to delete Invoice : {}", id);
        invoiceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.CREATE_INVOICE +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<InvoiceDTO> initializeNewInvoice(String kind) {
        switch (kind) {
            case "SALE":
                UserService.checkAuthority(AuthoritiesConstants.UPDATE_SALE);
            case "PURCHASE":
                UserService.checkAuthority(AuthoritiesConstants.UPDATE_PURCHASE);
        }

        return ResponseEntity.ok().body(this.invoiceService.initializeNewInvoice(InvoiceKind.valueOf(kind)));
    }

    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.UPDATE_INVOICE +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<InvoiceDTO> addInvoiceItem(String id, CreateInvoiceItemDTO createInvoiceItemDTO) {
        invoiceRepository
            .findById(id)
            .ifPresent(invoice -> {
                switch (invoice.getKind()) {
                    case SALE:
                        UserService.checkAuthority(AuthoritiesConstants.UPDATE_SALE);
                    case PURCHASE:
                        UserService.checkAuthority(AuthoritiesConstants.UPDATE_PURCHASE);
                }
            });

        return ResponseEntity.ok(invoiceService.addInvoiceItem(id, createInvoiceItemDTO));
    }

    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.UPDATE_INVOICE +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<InvoiceViewSimpleDTO> saveInvoice(String id) {
        invoiceRepository
            .findById(id)
            .ifPresent(invoice -> {
                switch (invoice.getKind()) {
                    case SALE:
                        UserService.checkAuthority(AuthoritiesConstants.UPDATE_SALE);
                    case PURCHASE:
                        UserService.checkAuthority(AuthoritiesConstants.UPDATE_PURCHASE);
                }
            });

        return ResponseEntity.ok(invoiceService.getMapper().toInvoiceViewSimpleDTO(invoiceService.saveInvoice(id)));
    }

    @Override
    public ResponseEntity<InvoicePrintDTO> getPrintInvoiceObject(String id) {
        return ResponseEntity.ok(invoiceService.getPrintInvoiceObject(id));
    }

    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.UPDATE_INVOICE +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<Void> deleteInvoiceItemFromInvoice(String id) {
        invoiceRepository
            .findById(id)
            .ifPresent(invoice -> {
                switch (invoice.getKind()) {
                    case SALE:
                        UserService.checkAuthority(AuthoritiesConstants.UPDATE_SALE);
                    case PURCHASE:
                        UserService.checkAuthority(AuthoritiesConstants.UPDATE_PURCHASE);
                }
            });

        invoiceService.deleteInvoiceItem(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.UPDATE_INVOICE +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<InvoiceItemViewDTO> updateInvoiceItem(String id, InvoiceItemUpdateDTO invoiceItemUpdateDTO) {
        invoiceRepository
            .findById(id)
            .ifPresent(invoice -> {
                switch (invoice.getKind()) {
                    case SALE:
                        UserService.checkAuthority(AuthoritiesConstants.UPDATE_SALE);
                    case PURCHASE:
                        UserService.checkAuthority(AuthoritiesConstants.UPDATE_PURCHASE);
                }
            });

        return ResponseEntity.ok(invoiceService.updateInvoiceItem(id, invoiceItemUpdateDTO));
    }

    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.VIEW_INVOICE +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<List<InvoiceDTO>> getAllInvoices(Integer page, Integer size, List<String> sort, Boolean eagerload) {
        log.info("Get all invoices");
        return ResponseEntity.ok(invoiceService.findAll(PageRequest.of(0, 1000)).getContent());
    }

    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.VIEW_INVOICE +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<InvoiceViewDTOContainer> invoicesViewSearchPaginate(InvoicesSearchModel invoicesSearchModel) {
        return ResponseEntity.ok(invoiceService.invoicesViewSearch(invoicesSearchModel));
    }

    @Override
    @PreAuthorize(
        "hasAnyAuthority('" +
        AuthoritiesConstants.VIEW_INVOICE +
        "') || hasAnyAuthority('" +
        AuthoritiesConstants.ADMIN +
        "','" +
        AuthoritiesConstants.SUPER_ADMIN +
        "')"
    )
    public ResponseEntity<List<InvoiceItemDTO>> getInvoiceItems(String id) {
        return ResponseEntity.ok(invoiceService.getInvoiceItems(id));
    }
}
