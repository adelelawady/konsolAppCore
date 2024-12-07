package com.konsol.core.service.dto;

import com.konsol.core.service.api.dto.CashFlowMetricsDTO;
import com.konsol.core.service.api.dto.FinancialChartDTO;
import com.konsol.core.service.api.dto.InvoiceAnalysisDTO;
import com.konsol.core.service.api.dto.InvoiceItemAnalysisDTO;
import com.konsol.core.service.api.dto.PerformanceIndicatorsDTO;
import com.konsol.core.service.api.dto.SalesMetricsDTO;
import com.konsol.core.service.api.dto.StoreAccountAnalysisDTO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class FinancialDashboardDTO implements Serializable {

    // Sales and Revenue Metrics
    private com.konsol.core.service.api.dto.SalesMetricsDTO salesMetrics;
    private List<com.konsol.core.service.api.dto.FinancialChartDTO> salesCharts;

    // Cash Flow Metrics
    private com.konsol.core.service.api.dto.CashFlowMetricsDTO cashFlowMetrics;
    private List<com.konsol.core.service.api.dto.FinancialChartDTO> cashFlowCharts;

    @Override
    public String toString() {
        return (
            "FinancialDashboardDTO{" +
            "salesMetrics=" +
            salesMetrics +
            ", salesCharts=" +
            salesCharts +
            ", cashFlowMetrics=" +
            cashFlowMetrics +
            ", cashFlowCharts=" +
            cashFlowCharts +
            ", invoiceAnalysis=" +
            invoiceAnalysis +
            ", invoiceCharts=" +
            invoiceCharts +
            ", invoiceItemAnalysis=" +
            invoiceItemAnalysis +
            ", itemSalesCharts=" +
            itemSalesCharts +
            ", performanceIndicators=" +
            performanceIndicators +
            ", performanceCharts=" +
            performanceCharts +
            ", storeAccountAnalysis=" +
            storeAccountAnalysis +
            ", analysisCharts=" +
            analysisCharts +
            ", startDate=" +
            startDate +
            ", endDate=" +
            endDate +
            ", storeId='" +
            storeId +
            '\'' +
            ", accountId='" +
            accountId +
            '\'' +
            ", bankId='" +
            bankId +
            '\'' +
            '}'
        );
    }

    // Invoice Analysis
    private com.konsol.core.service.api.dto.InvoiceAnalysisDTO invoiceAnalysis;
    private List<com.konsol.core.service.api.dto.FinancialChartDTO> invoiceCharts;

    // Invoice Items Analysis
    private com.konsol.core.service.api.dto.InvoiceItemAnalysisDTO invoiceItemAnalysis;
    private List<com.konsol.core.service.api.dto.FinancialChartDTO> itemSalesCharts;

    // Performance Indicators
    private com.konsol.core.service.api.dto.PerformanceIndicatorsDTO performanceIndicators;
    private List<com.konsol.core.service.api.dto.FinancialChartDTO> performanceCharts;

    // Store and Account Analysis
    private com.konsol.core.service.api.dto.StoreAccountAnalysisDTO storeAccountAnalysis;
    private List<com.konsol.core.service.api.dto.FinancialChartDTO> analysisCharts;

    // Filter Parameters
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String storeId;
    private String accountId;
    private String bankId;

    // Getters and Setters

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public SalesMetricsDTO getSalesMetrics() {
        return salesMetrics;
    }

    public void setSalesMetrics(SalesMetricsDTO salesMetrics) {
        this.salesMetrics = salesMetrics;
    }

    public List<FinancialChartDTO> getInvoiceCharts() {
        return invoiceCharts;
    }

    public void setInvoiceCharts(List<FinancialChartDTO> invoiceCharts) {
        this.invoiceCharts = invoiceCharts;
    }

    public List<FinancialChartDTO> getSalesCharts() {
        return salesCharts;
    }

    public void setSalesCharts(List<FinancialChartDTO> salesCharts) {
        this.salesCharts = salesCharts;
    }

    public CashFlowMetricsDTO getCashFlowMetrics() {
        return cashFlowMetrics;
    }

    public void setCashFlowMetrics(CashFlowMetricsDTO cashFlowMetrics) {
        this.cashFlowMetrics = cashFlowMetrics;
    }

    public List<FinancialChartDTO> getCashFlowCharts() {
        return cashFlowCharts;
    }

    public void setCashFlowCharts(List<FinancialChartDTO> cashFlowCharts) {
        this.cashFlowCharts = cashFlowCharts;
    }

    public InvoiceAnalysisDTO getInvoiceAnalysis() {
        return invoiceAnalysis;
    }

    public void setInvoiceAnalysis(InvoiceAnalysisDTO invoiceAnalysis) {
        this.invoiceAnalysis = invoiceAnalysis;
    }

    public InvoiceItemAnalysisDTO getInvoiceItemAnalysis() {
        return invoiceItemAnalysis;
    }

    public void setInvoiceItemAnalysis(InvoiceItemAnalysisDTO invoiceItemAnalysis) {
        this.invoiceItemAnalysis = invoiceItemAnalysis;
    }

    public List<FinancialChartDTO> getItemSalesCharts() {
        return itemSalesCharts;
    }

    public void setItemSalesCharts(List<FinancialChartDTO> itemSalesCharts) {
        this.itemSalesCharts = itemSalesCharts;
    }

    public PerformanceIndicatorsDTO getPerformanceIndicators() {
        return performanceIndicators;
    }

    public void setPerformanceIndicators(PerformanceIndicatorsDTO performanceIndicators) {
        this.performanceIndicators = performanceIndicators;
    }

    public List<FinancialChartDTO> getPerformanceCharts() {
        return performanceCharts;
    }

    public void setPerformanceCharts(List<FinancialChartDTO> performanceCharts) {
        this.performanceCharts = performanceCharts;
    }

    public StoreAccountAnalysisDTO getStoreAccountAnalysis() {
        return storeAccountAnalysis;
    }

    public void setStoreAccountAnalysis(StoreAccountAnalysisDTO storeAccountAnalysis) {
        this.storeAccountAnalysis = storeAccountAnalysis;
    }

    public List<FinancialChartDTO> getAnalysisCharts() {
        return analysisCharts;
    }

    public void setAnalysisCharts(List<FinancialChartDTO> analysisCharts) {
        this.analysisCharts = analysisCharts;
    }
}
