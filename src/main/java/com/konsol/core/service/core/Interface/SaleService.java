package com.konsol.core.service.core.Interface;

import com.konsol.core.domain.InvoiceItem;

/**
 * Service Interface for managing Sales {@link com.konsol.core.domain.Invoice}.
 */

public interface SaleService extends InvoiceController {
    /**
     * sets qty out based on unitQtyOut
     * @param invoiceItem selected invoice item to update
     * @return modified {@link InvoiceItem} object
     */
    default void setQtyOut(InvoiceItem invoiceItem) {
        invoiceItem.qtyOut(invoiceItem.getUnitQtyOut().multiply(invoiceItem.getUnitPieces()));
    }

    /**
     * sets total price of invoice
     * @param invoiceItem selected invoice item to update
     * @return modified {@link InvoiceItem} object
     */
    default void setTotalPrice(InvoiceItem invoiceItem) {
        invoiceItem.setTotalPrice(invoiceItem.getUnitPrice().multiply(invoiceItem.getUnitQtyOut()));
    }

    /**
     * sets total cost of invoice
     * @param invoiceItem selected invoice item to update
     * @return modified {@link InvoiceItem} object
     */
    default void setTotalCost(InvoiceItem invoiceItem) {
        invoiceItem.setTotalCost(invoiceItem.getUnitCost().multiply(invoiceItem.getUnitQtyOut()));
    }

    /**
     * sets total price of invoice
     * @param invoiceItem selected invoice item to update
     * @return modified {@link InvoiceItem} object
     */
    default void setTotalNetPrice(InvoiceItem invoiceItem) {
        invoiceItem.setNetPrice(invoiceItem.getTotalPrice());
    }

    /**
     * sets total cost of invoice
     * @param invoiceItem selected invoice item to update
     * @return modified {@link InvoiceItem} object
     */
    default void setTotalNetCost(InvoiceItem invoiceItem) {
        invoiceItem.setNetCost(invoiceItem.getTotalCost());
    }

    /**
     * sets qty out based on unitQtyOut
     * sets total price of invoice
     * sets total cost of invoice
     * @param invoiceItem
     */
    default void updateInvoiceItem(InvoiceItem invoiceItem) {
        setQtyOut(invoiceItem);
        setTotalPrice(invoiceItem);
        setTotalCost(invoiceItem);
        calcInvoiceDiscount(invoiceItem, true);
        setTotalNetCost(invoiceItem);
        // setTotalNetPrice(invoiceItem);
    }
}
