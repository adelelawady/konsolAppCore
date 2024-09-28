package com.konsol.core.service.core;

import com.konsol.core.domain.InvoiceItem;
import com.konsol.core.service.core.Interface.SaleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.konsol.core.domain.InvoiceItem}.
 */
@Service(value = "SALES")
public class SaleServiceImpl implements SaleService {

    private final Logger log = LoggerFactory.getLogger(SaleServiceImpl.class);

    public SaleServiceImpl() {}

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
