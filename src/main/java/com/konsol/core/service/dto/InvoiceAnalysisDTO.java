package com.konsol.core.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

public class InvoiceAnalysisDTO implements Serializable {

    private BigDecimal averageDiscount;
    private Integer averageDiscountPercentage;
    private BigDecimal totalAdditions;
    private BigDecimal totalExpenses;
    private Long deferredInvoicesCount;
    private Map<String, BigDecimal> expensesByType;

    // Getters and Setters
    public BigDecimal getAverageDiscount() {
        return averageDiscount;
    }

    public void setAverageDiscount(BigDecimal averageDiscount) {
        this.averageDiscount = averageDiscount;
    }

    public Integer getAverageDiscountPercentage() {
        return averageDiscountPercentage;
    }

    public void setAverageDiscountPercentage(Integer averageDiscountPercentage) {
        this.averageDiscountPercentage = averageDiscountPercentage;
    }

    public BigDecimal getTotalAdditions() {
        return totalAdditions;
    }

    public void setTotalAdditions(BigDecimal totalAdditions) {
        this.totalAdditions = totalAdditions;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(BigDecimal totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public Long getDeferredInvoicesCount() {
        return deferredInvoicesCount;
    }

    public void setDeferredInvoicesCount(Long deferredInvoicesCount) {
        this.deferredInvoicesCount = deferredInvoicesCount;
    }

    public Map<String, BigDecimal> getExpensesByType() {
        return expensesByType;
    }

    public void setExpensesByType(Map<String, BigDecimal> expensesByType) {
        this.expensesByType = expensesByType;
    }

    @Override
    public String toString() {
        return (
            "InvoiceAnalysisDTO{" +
            "averageDiscount=" +
            averageDiscount +
            ", averageDiscountPercentage=" +
            averageDiscountPercentage +
            ", totalAdditions=" +
            totalAdditions +
            ", totalExpenses=" +
            totalExpenses +
            ", deferredInvoicesCount=" +
            deferredInvoicesCount +
            ", expensesByType=" +
            expensesByType +
            '}'
        );
    }
}
