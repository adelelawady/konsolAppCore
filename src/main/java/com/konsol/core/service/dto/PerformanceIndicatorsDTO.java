package com.konsol.core.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class PerformanceIndicatorsDTO implements Serializable {

    private BigDecimal grossProfitMargin;
    private BigDecimal netProfitMargin;
    private BigDecimal operatingExpensesRatio;
    private BigDecimal currentRatio;
    private BigDecimal quickRatio;
    private BigDecimal revenueGrowth;

    // Getters and Setters
    public BigDecimal getGrossProfitMargin() {
        return grossProfitMargin;
    }

    public void setGrossProfitMargin(BigDecimal grossProfitMargin) {
        this.grossProfitMargin = grossProfitMargin;
    }

    public BigDecimal getNetProfitMargin() {
        return netProfitMargin;
    }

    public void setNetProfitMargin(BigDecimal netProfitMargin) {
        this.netProfitMargin = netProfitMargin;
    }

    public BigDecimal getOperatingExpensesRatio() {
        return operatingExpensesRatio;
    }

    public void setOperatingExpensesRatio(BigDecimal operatingExpensesRatio) {
        this.operatingExpensesRatio = operatingExpensesRatio;
    }

    public BigDecimal getCurrentRatio() {
        return currentRatio;
    }

    public void setCurrentRatio(BigDecimal currentRatio) {
        this.currentRatio = currentRatio;
    }

    public BigDecimal getQuickRatio() {
        return quickRatio;
    }

    public void setQuickRatio(BigDecimal quickRatio) {
        this.quickRatio = quickRatio;
    }

    public BigDecimal getRevenueGrowth() {
        return revenueGrowth;
    }

    public void setRevenueGrowth(BigDecimal revenueGrowth) {
        this.revenueGrowth = revenueGrowth;
    }

    @Override
    public String toString() {
        return (
            "PerformanceIndicatorsDTO{" +
            "grossProfitMargin=" +
            grossProfitMargin +
            ", netProfitMargin=" +
            netProfitMargin +
            ", operatingExpensesRatio=" +
            operatingExpensesRatio +
            ", currentRatio=" +
            currentRatio +
            ", quickRatio=" +
            quickRatio +
            ", revenueGrowth=" +
            revenueGrowth +
            '}'
        );
    }
}
