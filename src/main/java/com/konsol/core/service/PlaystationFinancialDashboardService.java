package com.konsol.core.service;

import com.konsol.core.domain.Invoice;
import com.konsol.core.domain.InvoiceItem;
import com.konsol.core.service.api.dto.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public interface PlaystationFinancialDashboardService {
    /**
     * Get complete dashboard data with all metrics and charts
     */
    FinancialDashboardDTO getDashboardData(
        OffsetDateTime startDate,
        OffsetDateTime endDate,
        String storeId,
        String accountId,
        String bankId
    );

    /**
     * Sales and Revenue Methods
     */
    SalesMetricsDTO getSalesMetrics(List<Invoice> playStationSessionsInvoices); // *
    List<FinancialChartDTO> getSalesCharts(List<Invoice> invoices); //*
    FinancialChartDTO getDailySalesTrend(List<Invoice> invoices);
    FinancialChartDTO getMonthlySalesTrend(List<Invoice> invoices);
    FinancialChartDTO getSalesVsCostsComparison(List<Invoice> invoices);
    FinancialChartDTO getProfitMarginDistribution(List<Invoice> invoices);

    /**
     * Cash Flow Methods
     */
    CashFlowMetricsDTO getCashFlowMetrics(LocalDateTime startDate, LocalDateTime endDate, String bankId); //*
    List<FinancialChartDTO> getCashFlowCharts(LocalDateTime startDate, LocalDateTime endDate, String bankId); //*
    FinancialChartDTO getCashFlowTrend(LocalDateTime startDate, LocalDateTime endDate);
    FinancialChartDTO getMoneyInOutComparison(LocalDateTime startDate, LocalDateTime endDate);
    FinancialChartDTO getBankDistribution();

    /**
     * Invoice Analysis Methods
     */
    InvoiceAnalysisDTO getInvoiceAnalysis(String storeId, List<Invoice> invoices); //*
    List<FinancialChartDTO> getInvoiceCharts(LocalDateTime startDate, LocalDateTime endDate); //*
    FinancialChartDTO getDiscountDistribution(LocalDateTime startDate, LocalDateTime endDate);
    // FinancialChartDTO getExpensesByType();
    FinancialChartDTO getDeferredInvoicesTrend(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Invoice Item Analysis Methods
     */
    // FinancialChartDTO getDailyItemSalesTrend(String itemId, LocalDateTime startDate, LocalDateTime endDate);
    InvoiceItemAnalysisDTO getInvoiceItemAnalysis(List<InvoiceItem> invoiceItems); //*
    List<FinancialChartDTO> getItemSalesCharts(List<InvoiceItem> invoiceItems); //*
    FinancialChartDTO getItemSalesTrend(List<InvoiceItem> invoiceItems);
    List<FinancialChartDTO> getTopSellingItems(List<InvoiceItem> invoiceItems, int limit);
    FinancialChartDTO getItemCategoryDistribution(List<InvoiceItem> invoiceItems);
    FinancialChartDTO getItemProfitMargins(LocalDateTime startDate, LocalDateTime endDate);
    List<ItemSalesDTO> getDetailedItemSalesReport(LocalDateTime startDate, LocalDateTime endDate, String storeId);
    Map<String, BigDecimal> getItemCategoryRevenue(List<InvoiceItem> invoiceItems);
    List<ItemProfitabilityDTO> getItemProfitabilityAnalysis(LocalDateTime startDate, LocalDateTime endDate);
    InvoiceItemAnalysisDTO getItemSalesAnalysis(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Performance Indicators Methods
     */
    PerformanceIndicatorsDTO getPerformanceIndicators(LocalDateTime startDate, LocalDateTime endDate); //*
    List<FinancialChartDTO> getPerformanceCharts(LocalDateTime startDate, LocalDateTime endDate); //*
    FinancialChartDTO getPerformanceRatios();
    FinancialChartDTO getMarginTrends(LocalDateTime startDate, LocalDateTime endDate);
    FinancialChartDTO getProfitBreakdown();

    /**
     * Store and Account Analysis Methods
     */
    // StoreAccountAnalysisDTO getStoreAccountAnalysis(LocalDateTime startDate, LocalDateTime endDate);
    // List<FinancialChartDTO> getAnalysisCharts(LocalDateTime startDate, LocalDateTime endDate);
    // FinancialChartDTO getStorePerformanceHeatmap();
    // FinancialChartDTO getTopAccountsByVolume(int limit);
    // FinancialChartDTO getBankTransactionDistribution();
    List<FinancialChartDTO> getStoreAccountCharts(LocalDateTime startDate, LocalDateTime endDate, String storeId, String accountId); //*
    StoreAccountAnalysisDTO getStoreAccountAnalysis(LocalDateTime startDate, LocalDateTime endDate, String storeId, String accountId); //*
    /**
     * money
     */

    FinancialChartDTO getExpensesByTypeChart(LocalDateTime startDate, LocalDateTime endDate);
    /**
     * Export Methods
     */
    // byte[] exportDashboardData(LocalDateTime startDate, LocalDateTime endDate, String format);
    // byte[] exportChartData(String chartId, String format);
}
