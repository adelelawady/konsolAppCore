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

    /**
     * handle invoice saving and activating
     * removes temp tag from invoice and
     * adds it to central invoices data
     * @param invoice invoice to save
     * @return activated invoice after being saved
     */
    Invoice saveInvoice(Invoice invoice);

    /**
     * handle invoice saving and activating
     * removes temp tag from invoice and
     * adds it to central invoices data
     * @param invoiceId invoiceId to find and save
     * @return activated invoice after being saved
     */
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

    /**
     * calculates all invoice items discount
     * @param invoice selected invoice to calculate
     * @return Invoice saved
     */
    //Invoice calcInvoiceDiscount(Invoice invoice, boolean save);

    /**
     * Calculates the discounted invoice based on the provided invoice details.
     *
     * @param invoice The original invoice to calculate the discount for
     * @return The invoice with the discount applied
     */
    Invoice calcInvoiceDiscount(Invoice invoice);

    Invoice addInvoiceAddititon(Invoice invoice);

    /**
     * get invoice's Invoice items list
     * @param id invoice id
     * @return list of invoice invoiceitems
     */
    List<InvoiceItemDTO> getInvoiceItems(String id);

    /**
     * Retrieves the printable invoice object based on the provided invoice ID.
     *
     * @param id The ID of the invoice to retrieve
     * @return The printable invoice object
     * @throws InvoiceNotFoundException if the invoice with the given ID is not found
     */
    InvoicePrintDTO getPrintInvoiceObject(String id);

    /**
     * invoice items
     */

    /**
     * @apiNote uses {calcInvoiceItem}
     * updates invoice item
     * @param invoiceItem selected invoice item
     * @param unitId selected unit id
     * @param userQty user quantity
     * @param userPrice user price
     * @return InvoiceItem saved
     */
    InvoiceItem setInvoiceItemUnit(InvoiceItem invoiceItem, String unitId, BigDecimal userQty, BigDecimal userPrice);

    /**
     * @apiNote uses {calcInvoiceItem}
     * @param kind selected invoice kind
     * @param ItemId selected item id
     * @param unitId selected unit id
     * @param userQty user quantity
     * @param userPrice
     * @return
     */

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

    /**
     * Not activated temp Invoices should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    void removeTempInvoices();

    InvoiceViewDTOContainer invoicesViewSearch(InvoicesSearchModel invoicesSearchModel);
}
