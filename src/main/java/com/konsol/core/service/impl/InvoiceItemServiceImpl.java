package com.konsol.core.service.impl;

import com.konsol.core.domain.InvoiceItem;
import com.konsol.core.repository.InvoiceItemRepository;
import com.konsol.core.service.InvoiceItemService;
import com.konsol.core.service.api.dto.InvoiceItemDTO;
import com.konsol.core.service.mapper.InvoiceItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link InvoiceItem}.
 */
@Service
public class InvoiceItemServiceImpl implements InvoiceItemService {

    private final Logger log = LoggerFactory.getLogger(InvoiceItemServiceImpl.class);

    private final InvoiceItemRepository invoiceItemRepository;

    private final InvoiceItemMapper invoiceItemMapper;

    public InvoiceItemServiceImpl(InvoiceItemRepository invoiceItemRepository, InvoiceItemMapper invoiceItemMapper) {
        this.invoiceItemRepository = invoiceItemRepository;
        this.invoiceItemMapper = invoiceItemMapper;
    }

    @Override
    public InvoiceItemDTO save(InvoiceItemDTO invoiceItemDTO) {
        log.debug("Request to save InvoiceItem : {}", invoiceItemDTO);
        InvoiceItem invoiceItem = invoiceItemMapper.toEntity(invoiceItemDTO);
        invoiceItem = invoiceItemRepository.save(invoiceItem);
        return invoiceItemMapper.toDto(invoiceItem);
    }

    /**
     * Updates an InvoiceItem entity based on the provided InvoiceItemDTO.
     *
     * @param invoiceItemDTO The DTO containing the updated information for the InvoiceItem
     * @return The updated InvoiceItemDTO after saving the changes
     */
    @Override
    public InvoiceItemDTO update(InvoiceItemDTO invoiceItemDTO) {
        log.debug("Request to update InvoiceItem : {}", invoiceItemDTO);
        InvoiceItem invoiceItem = invoiceItemMapper.toEntity(invoiceItemDTO);
        invoiceItem = invoiceItemRepository.save(invoiceItem);
        return invoiceItemMapper.toDto(invoiceItem);
    }

    /**
     * Partially updates an InvoiceItem based on the provided InvoiceItemDTO.
     *
     * @param invoiceItemDTO The DTO containing the updated information for the InvoiceItem
     * @return An Optional containing the updated InvoiceItemDTO if found and updated, otherwise empty
     */
    @Override
    public Optional<InvoiceItemDTO> partialUpdate(InvoiceItemDTO invoiceItemDTO) {
        log.debug("Request to partially update InvoiceItem : {}", invoiceItemDTO);

        return invoiceItemRepository
            .findById(invoiceItemDTO.getId())
            .map(existingInvoiceItem -> {
                invoiceItemMapper.partialUpdate(existingInvoiceItem, invoiceItemDTO);

                return existingInvoiceItem;
            })
            .map(invoiceItemRepository::save)
            .map(invoiceItemMapper::toDto);
    }

    /**
     * Retrieve all invoice items with pagination.
     *
     * @param pageable Pagination information
     * @return A page of InvoiceItemDTO objects
     */
    @Override
    public Page<InvoiceItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InvoiceItems");
        return invoiceItemRepository.findAll(pageable).map(invoiceItemMapper::toDto);
    }

    /**
     * Retrieves an InvoiceItemDTO by its ID.
     *
     * @param id The ID of the InvoiceItem to retrieve
     * @return An Optional containing the InvoiceItemDTO if found, otherwise empty
     */
    @Override
    public Optional<InvoiceItemDTO> findOne(String id) {
        log.debug("Request to get InvoiceItem : {}", id);
        return invoiceItemRepository.findById(id).map(invoiceItemMapper::toDto);
    }

    /**
     * Deletes an InvoiceItem by its ID.
     *
     * @param id The ID of the InvoiceItem to delete
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete InvoiceItem : {}", id);
        invoiceItemRepository.deleteById(id);
    }
}
