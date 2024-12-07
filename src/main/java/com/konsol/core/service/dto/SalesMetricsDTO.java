package com.konsol.core.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class SalesMetricsDTO implements Serializable {

    private BigDecimal totalSales;
    private BigDecimal netSales;
    private BigDecimal totalCost;
    private BigDecimal netCost;
    private BigDecimal netProfit;
    private BigDecimal dailyRevenue;
    private BigDecimal monthlyRevenue;

    // Getters and Setters
    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }

    public BigDecimal getNetSales() {
        return netSales;
    }

    public void setNetSales(BigDecimal netSales) {
        this.netSales = netSales;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getNetCost() {
        return netCost;
    }

    public void setNetCost(BigDecimal netCost) {
        this.netCost = netCost;
    }

    public BigDecimal getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(BigDecimal netProfit) {
        this.netProfit = netProfit;
    }

    public BigDecimal getDailyRevenue() {
        return dailyRevenue;
    }

    public void setDailyRevenue(BigDecimal dailyRevenue) {
        this.dailyRevenue = dailyRevenue;
    }

    public BigDecimal getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public void setMonthlyRevenue(BigDecimal monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }

    @Override
    public String toString() {
        return (
            "SalesMetricsDTO{" +
            "totalSales=" +
            totalSales +
            ", netSales=" +
            netSales +
            ", totalCost=" +
            totalCost +
            ", netCost=" +
            netCost +
            ", netProfit=" +
            netProfit +
            ", dailyRevenue=" +
            dailyRevenue +
            ", monthlyRevenue=" +
            monthlyRevenue +
            '}'
        );
    }
}
