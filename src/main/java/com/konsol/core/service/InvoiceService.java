package com.konsol.core.service;

import com.konsol.core.domain.Invoice;
import com.konsol.core.domain.InvoiceItem;
import com.konsol.core.domain.enumeration.InvoiceKind;
import com.konsol.core.service.api.dto.CreateInvoiceItemDTO;
import com.konsol.core.service.api.dto.InvoiceDTO;
import com.konsol.core.service.api.dto.InvoicePrintDTO;
import com.konsol.core.service.api.dto.InvoiceUpdateDTO;
import com.konsol.core.service.mapper.InvoiceMapper;
import com.konsol.core.service.mapper.ItemMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.konsol.core.domain.Invoice}.
 */
public interface InvoiceService {
    /**
     * Partially updates a invoice.
     *
     * @param invoiceUpdateDTO the entity to update partially.
     * @return the persisted entity.
     */
    Invoice updateInvoice(InvoiceUpdateDTO invoiceUpdateDTO);

    Invoice saveInvoice(Invoice invoice);

    Invoice saveInvoice(String invoiceId);
    /**
     * Get all the invoices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InvoiceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" invoice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InvoiceDTO> findOne(String id);

    /**
     * Get the "id" invoice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Invoice> findOneDomain(String id);

    /**
     * Delete the "id" invoice.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    InvoiceMapper getMapper();

    /**
     * INVOICE
     */

    InvoiceDTO initializeNewInvoice(InvoiceKind kind);
    InvoiceDTO addInvoiceItem(String id, CreateInvoiceItemDTO createInvoiceItemDTO);

    void deleteInvoiceItem(String id);

    Invoice calcInvoice(Invoice invoice, boolean save);

    Invoice calcInvoiceDiscount(Invoice invoice);

    Invoice addInvoiceAddititon(Invoice invoice);

    InvoicePrintDTO getPrintInvoiceObject(String id);

    /**
     * invoice items
     */

    InvoiceItem setInvoiceItemUnit(InvoiceItem invoiceItem, String unitId, BigDecimal userQty, BigDecimal userPrice);

    InvoiceItem initializeNewInvoiceItem(String ItemId, String unitId, BigDecimal userQty, BigDecimal userPrice);
    InvoiceItem createInvoiceItem(Invoice invoice, CreateInvoiceItemDTO createInvoiceItemDTO);

    BigDecimal calcItemQtyOutInInvoiceItems(String invoiceId, String itemId);

    InvoiceItem findInvoiceItemByItemIdAndPrice(String invoiceId, String itemId, BigDecimal price);
    InvoiceItem findInvoiceItemByItemId(String invoiceId, String itemId);

    List<InvoiceItem> findInvoiceItemsByItemId(String invoiceId, String itemId);

    InvoiceItem findInvoiceItemByItemIdAndUnitId(String invoiceId, String itemId, String unitId);
    InvoiceItem findInvoiceItemByItemIdAndUnitIdAndPrice(String invoiceId, String itemId, String unitId, BigDecimal price);

    InvoiceItem AddQtyToInvoiceItem(Invoice invoice, String invoiceItemId, BigDecimal qty);
    InvoiceItem setInvoiceItemNullUnit(InvoiceItem invoiceItem);
}
