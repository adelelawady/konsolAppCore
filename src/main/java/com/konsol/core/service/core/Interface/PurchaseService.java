package com.konsol.core.service.core.Interface;

import com.konsol.core.domain.Invoice;
import com.konsol.core.domain.InvoiceItem;

/**
 * Service Interface for managing Sales {@link Invoice}.
 */
public interface PurchaseService extends InvoiceController {
    /**
     * sets qty out based on unitQtyOut
     * @param invoiceItem selected invoice item to update
     * @return modified {@link InvoiceItem} object
     */
    default void setQtyIn(InvoiceItem invoiceItem) {
        invoiceItem.qtyIn(invoiceItem.getUnitQtyIn().multiply(invoiceItem.getUnitPieces()));
    }

    /**
     * sets total price of invoice
     * @param invoiceItem selected invoice item to update
     * @return modified {@link InvoiceItem} object
     */
    default void setTotalPrice(InvoiceItem invoiceItem) {
        invoiceItem.setTotalPrice(invoiceItem.getUnitPrice().multiply(invoiceItem.getUnitQtyIn()));
    }

    /**
     * sets total cost of invoice
     * @param invoiceItem selected invoice item to update
     * @return modified {@link InvoiceItem} object
     */
    default void setTotalCost(InvoiceItem invoiceItem) {
        invoiceItem.setTotalCost(invoiceItem.getUnitCost().multiply(invoiceItem.getUnitQtyIn()));
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
        setQtyIn(invoiceItem);
        setTotalPrice(invoiceItem);
        setTotalCost(invoiceItem);
        calcInvoiceDiscount(invoiceItem, false);
        //setTotalNetCost(invoiceItem);
        setTotalNetPrice(invoiceItem);
    }
}
