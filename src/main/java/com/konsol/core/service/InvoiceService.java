package com.konsol.core.service;

import com.konsol.core.domain.Invoice;
import com.konsol.core.domain.InvoiceItem;
import com.konsol.core.domain.enumeration.InvoiceKind;
import com.konsol.core.service.api.dto.*;
import com.konsol.core.service.mapper.InvoiceMapper;
import com.konsol.core.service.mapper.ItemMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

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

    /**
     * creates new empty invoice item as temp not included till it's active and saved invoice
     * @param kind kind of new created invoice
     * @return invoice saved , mapped to InvoiceDTO
     */
    InvoiceDTO initializeNewInvoice(InvoiceKind kind);

    /**
     * add item to invoice
     * @param id id of invoice
     * @param createInvoiceItemDTO dto contains new item price , unit , and qty
     * @return invoice saved , mapped to InvoiceDTO
     */
    InvoiceDTO addInvoiceItem(String id, CreateInvoiceItemDTO createInvoiceItemDTO);

    /**
     * @apiNote uses {calcInvoice}
     * deletes item from invoice
     * @param id invoiceItem id to delete from invoice
     */
    void deleteInvoiceItem(String id);

    /**
     * calculates all invoice items totalPrice , totalCost ,
     * netPrice , netCost , discount , and additions for selected invoice
     * @param invoice selected invoice to calculate
     * @param save if true the invoice will be saved after calculations
     * @return Invoice saved
     */
    Invoice calcInvoice(Invoice invoice, boolean save);

    /**
     * Adds to the items in the invoice the ascending number
     * @param invoiceId selected invoice id
     */
    void regenerateInvoiceItemsPk(String invoiceId);

    Invoice calcInvoiceDiscount(Invoice invoice);

    Invoice addInvoiceAddititon(Invoice invoice);

    InvoicePrintDTO getPrintInvoiceObject(String id);

    /**
     * invoice items
     */

    InvoiceItem setInvoiceItemUnit(InvoiceItem invoiceItem, String unitId, BigDecimal userQty, BigDecimal userPrice);

    InvoiceItem initializeNewInvoiceItem(InvoiceKind kind, String ItemId, String unitId, BigDecimal userQty, BigDecimal userPrice);

    InvoiceItem createInvoiceItem(Invoice invoice, CreateInvoiceItemDTO createInvoiceItemDTO);

    InvoiceItem calcInvoiceInvoiceItem(InvoiceItem invoiceItem);

    BigDecimal calcItemQtyOutInInvoiceItems(String invoiceId, String itemId, boolean out);

    InvoiceItem findInvoiceItemByItemId(String invoiceId, String itemId);

    List<InvoiceItem> findInvoiceItemsByItemId(String invoiceId, String itemId);

    InvoiceItem findInvoiceItemByItemIdAndUnitId(String invoiceId, String itemId, String unitId);

    InvoiceItem findInvoiceItemByItemIdAndUnitIdAndMoney(String invoiceId, String itemId, String unitId, BigDecimal money, boolean isPrice);
    InvoiceItem findInvoiceItemByItemIdAndMoney(String invoiceId, String itemId, BigDecimal money, boolean isPrice);

    InvoiceItem AddQtyToInvoiceItem(Invoice invoice, String invoiceItemId, BigDecimal qty);
    InvoiceItem setInvoiceItemNullUnit(InvoiceItem invoiceItem);

    InvoiceItemViewDTO updateInvoiceItem(String id, InvoiceItemUpdateDTO invoiceItemUpdateDTO);
}
