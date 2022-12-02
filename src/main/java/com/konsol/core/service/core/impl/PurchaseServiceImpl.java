package com.konsol.core.service.core.impl;

import com.konsol.core.domain.InvoiceItem;
import com.konsol.core.repository.InvoiceItemRepository;
import com.konsol.core.service.PurchaseService;
import com.konsol.core.service.SaleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link InvoiceItem}.
 */
@Service(value = "PURCHASE")
public class PurchaseServiceImpl implements PurchaseService {

    private final Logger log = LoggerFactory.getLogger(PurchaseServiceImpl.class);

    private final InvoiceItemRepository invoiceItemRepository;

    public PurchaseServiceImpl(InvoiceItemRepository invoiceItemRepository) {
        this.invoiceItemRepository = invoiceItemRepository;
    }

    /**
     * calculate Invoice Item for selected invoice
     *
     * @param invoiceItem     selected invoice item
     * @return saved or modified {@link InvoiceItem} object
     */
    @Override
    public InvoiceItem calcInvoiceItem(InvoiceItem invoiceItem) {
        /**
         * SET QTY_OUT
         */
        invoiceItem.unitQtyIn(invoiceItem.getUserQty());
        updateInvoiceItem(invoiceItem);

        return invoiceItem;
    }
}
