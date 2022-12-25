package com.konsol.core.service;

import com.konsol.core.domain.Invoice;
import com.konsol.core.domain.InvoiceItem;
import com.konsol.core.domain.enumeration.InvoiceKind;
import com.konsol.core.service.api.dto.CreateInvoiceItemDTO;
import com.konsol.core.service.api.dto.InvoiceDTO;
import com.konsol.core.service.api.dto.InvoiceItemDTO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.context.annotation.Primary;

/**
 * Service Interface for managing Sales {@link com.konsol.core.domain.Invoice}.
 */

public interface InvoiceController {
    /**
     * calculate Invoice Item for selected invoice
     *
     * @param invoiceItem selected invoice item
     * @return saved or modified {@link InvoiceItem} object
     */
    InvoiceItem calcInvoiceItem(InvoiceItem invoiceItem);

    /**
     * add qty to invoice item
     *
     * @param invoiceItem selected invoice item
     * @param qty         qty to add
     * @return saved or modified {@link InvoiceItem} object
     */
    default InvoiceItem AddQtyToInvoiceItem(InvoiceItem invoiceItem, BigDecimal qty, boolean out) {
        if (out) {
            invoiceItem.setUserQty(invoiceItem.getUnitQtyOut().add(qty));
        } else {
            invoiceItem.setUserQty(invoiceItem.getUnitQtyIn().add(qty));
        }

        return calcInvoiceItem(invoiceItem);
    }

    default InvoiceItem calcInvoiceDiscount(InvoiceItem invoiceItem, boolean netPrice) {
        BigDecimal requiredTotal = netPrice ? invoiceItem.getTotalPrice() : invoiceItem.getTotalCost();

        if (
            (invoiceItem.getDiscount() == null || (invoiceItem.getDiscount().compareTo(new BigDecimal(0))) == 0) &&
            (invoiceItem.getDiscountPer() == null || (invoiceItem.getDiscountPer() == 0))
        ) {
            if (netPrice) {
                invoiceItem.netPrice(requiredTotal);
            } else {
                invoiceItem.netCost(requiredTotal);
            }
        }

        if (!(invoiceItem.getDiscountPer() == null || (invoiceItem.getDiscountPer() == 0))) {
            BigDecimal discount =
                (BigDecimal.valueOf(invoiceItem.getDiscountPer()).divide(BigDecimal.valueOf(100), 4, RoundingMode.CEILING)).multiply(
                        requiredTotal
                    );
            if (discount.compareTo(requiredTotal) < 0) {
                invoiceItem.discount(discount);

                if (netPrice) {
                    invoiceItem.netPrice(requiredTotal.subtract(discount));
                } else {
                    invoiceItem.netCost(requiredTotal.subtract(discount));
                }
            } else {
                invoiceItem.discountPer(0);
                invoiceItem.discount(new BigDecimal(0));

                if (netPrice) {
                    invoiceItem.netPrice(requiredTotal);
                } else {
                    invoiceItem.netCost(requiredTotal);
                }
            }
        }

        if (!(invoiceItem.getDiscount() == null || (invoiceItem.getDiscount().compareTo(new BigDecimal(0))) == 0)) {
            BigDecimal discountPer =
                (invoiceItem.getDiscount().divide(requiredTotal, 4, RoundingMode.CEILING)).multiply(BigDecimal.valueOf(100));

            if (invoiceItem.getDiscount().compareTo(requiredTotal) < 0) {
                invoiceItem.discountPer(discountPer.intValue());

                if (netPrice) {
                    invoiceItem.netPrice(requiredTotal.subtract(invoiceItem.getDiscount()));
                } else {
                    invoiceItem.netCost(requiredTotal.subtract(invoiceItem.getDiscount()));
                }
            } else {
                invoiceItem.discountPer(0).discount(new BigDecimal(0));

                if (netPrice) {
                    invoiceItem.netPrice(requiredTotal);
                } else {
                    invoiceItem.netCost(requiredTotal);
                }
            }
        }

        return invoiceItem;
    }
}
