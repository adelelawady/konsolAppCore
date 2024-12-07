package com.konsol.core.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Data Transfer Object for storing financial performance metrics and KPIs.
 */
public class PerformanceMetricsDTO implements Serializable {

    private BigDecimal totalRevenue;
    private BigDecimal averageInvoiceValue;
    private BigDecimal grossProfit;
    private BigDecimal grossProfitMargin;
    private BigDecimal operatingExpenses;
    private BigDecimal operatingProfit;
    private BigDecimal operatingProfitMargin;
    private BigDecimal netProfit;
    private BigDecimal netProfitMargin;
    private BigDecimal revenueGrowth;
    private Integer totalTransactions;
    private Map<String, BigDecimal> additionalMetrics = new HashMap<>();

    // Getters and Setters
    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getAverageInvoiceValue() {
        return averageInvoiceValue;
    }

    public void setAverageInvoiceValue(BigDecimal averageInvoiceValue) {
        this.averageInvoiceValue = averageInvoiceValue;
    }

    public BigDecimal getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(BigDecimal grossProfit) {
        this.grossProfit = grossProfit;
    }

    public BigDecimal getGrossProfitMargin() {
        return grossProfitMargin;
    }

    public void setGrossProfitMargin(BigDecimal grossProfitMargin) {
        this.grossProfitMargin = grossProfitMargin;
    }

    public BigDecimal getOperatingExpenses() {
        return operatingExpenses;
    }

    public void setOperatingExpenses(BigDecimal operatingExpenses) {
        this.operatingExpenses = operatingExpenses;
    }

    public BigDecimal getOperatingProfit() {
        return operatingProfit;
    }

    public void setOperatingProfit(BigDecimal operatingProfit) {
        this.operatingProfit = operatingProfit;
    }

    public BigDecimal getOperatingProfitMargin() {
        return operatingProfitMargin;
    }

    public void setOperatingProfitMargin(BigDecimal operatingProfitMargin) {
        this.operatingProfitMargin = operatingProfitMargin;
    }

    public BigDecimal getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(BigDecimal netProfit) {
        this.netProfit = netProfit;
    }

    public BigDecimal getNetProfitMargin() {
        return netProfitMargin;
    }

    public void setNetProfitMargin(BigDecimal netProfitMargin) {
        this.netProfitMargin = netProfitMargin;
    }

    public BigDecimal getRevenueGrowth() {
        return revenueGrowth;
    }

    public void setRevenueGrowth(BigDecimal revenueGrowth) {
        this.revenueGrowth = revenueGrowth;
    }

    public Integer getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(Integer totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public Map<String, BigDecimal> getAdditionalMetrics() {
        return additionalMetrics;
    }

    public void setAdditionalMetrics(Map<String, BigDecimal> additionalMetrics) {
        this.additionalMetrics = additionalMetrics;
    }

    // Helper methods
    public void addMetric(String name, BigDecimal value) {
        this.additionalMetrics.put(name, value);
    }

    public BigDecimal getMetric(String name) {
        return this.additionalMetrics.get(name);
    }
}
