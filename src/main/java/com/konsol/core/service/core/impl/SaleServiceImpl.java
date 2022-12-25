package com.konsol.core.service.core.impl;

import com.konsol.core.domain.Invoice;
import com.konsol.core.domain.InvoiceItem;
import com.konsol.core.repository.InvoiceItemRepository;
import com.konsol.core.service.SaleService;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.konsol.core.domain.InvoiceItem}.
 */
@Service(value = "SALES")
public class SaleServiceImpl implements SaleService {

    private final Logger log = LoggerFactory.getLogger(SaleServiceImpl.class);

    private final InvoiceItemRepository invoiceItemRepository;

    public SaleServiceImpl(InvoiceItemRepository invoiceItemRepository) {
        this.invoiceItemRepository = invoiceItemRepository;
    }

    /**
     * calculate Invoice Item for selected invoice
     *
     * @param invoiceItem     selected invoice item
    // * @param saveInvoiceItem if true save invoiceItem to db
     * @return saved or modified {@link InvoiceItem} object
     */
    @Override
    public InvoiceItem calcInvoiceItem(InvoiceItem invoiceItem) {
        /**
         * SET QTY_OUT
         */
        invoiceItem.unitQtyOut(invoiceItem.getUserQty());
        updateInvoiceItem(invoiceItem);

        return invoiceItem;
    }
}
