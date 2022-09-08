package com.konsol.core.service;

import com.konsol.core.service.dto.InvoiceItemDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.konsol.core.domain.InvoiceItem}.
 */
public interface InvoiceItemService {
    /**
     * Save a invoiceItem.
     *
     * @param invoiceItemDTO the entity to save.
     * @return the persisted entity.
     */
    InvoiceItemDTO save(InvoiceItemDTO invoiceItemDTO);

    /**
     * Updates a invoiceItem.
     *
     * @param invoiceItemDTO the entity to update.
     * @return the persisted entity.
     */
    InvoiceItemDTO update(InvoiceItemDTO invoiceItemDTO);

    /**
     * Partially updates a invoiceItem.
     *
     * @param invoiceItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InvoiceItemDTO> partialUpdate(InvoiceItemDTO invoiceItemDTO);

    /**
     * Get all the invoiceItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InvoiceItemDTO> findAll(Pageable pageable);

    /**
     * Get the "id" invoiceItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InvoiceItemDTO> findOne(String id);

    /**
     * Delete the "id" invoiceItem.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
