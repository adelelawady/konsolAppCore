package com.konsol.core.service;

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
    SalesMetricsDTO getSalesMetrics(LocalDateTime startDate, LocalDateTime endDate); // *
    List<FinancialChartDTO> getSalesCharts(LocalDateTime startDate, LocalDateTime endDate, String storeId); //*
    FinancialChartDTO getDailySalesTrend(LocalDateTime startDate, LocalDateTime endDate);
    FinancialChartDTO getMonthlySalesTrend(LocalDateTime startDate, LocalDateTime endDate);
    FinancialChartDTO getSalesVsCostsComparison(LocalDateTime startDate, LocalDateTime endDate);
    FinancialChartDTO getProfitMarginDistribution(LocalDateTime startDate, LocalDateTime endDate);

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
    InvoiceAnalysisDTO getInvoiceAnalysis(String storeId, LocalDateTime startDate, LocalDateTime endDate); //*
    List<FinancialChartDTO> getInvoiceCharts(LocalDateTime startDate, LocalDateTime endDate); //*
    FinancialChartDTO getDiscountDistribution();
    // FinancialChartDTO getExpensesByType();
    FinancialChartDTO getDeferredInvoicesTrend(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Invoice Item Analysis Methods
     */
    // FinancialChartDTO getDailyItemSalesTrend(String itemId, LocalDateTime startDate, LocalDateTime endDate);
    InvoiceItemAnalysisDTO getInvoiceItemAnalysis(LocalDateTime startDate, LocalDateTime endDate, String storeId); //*
    List<FinancialChartDTO> getItemSalesCharts(LocalDateTime startDate, LocalDateTime endDate, String storeId); //*
    FinancialChartDTO getItemSalesTrend(LocalDateTime startDate, LocalDateTime endDate);
    List<FinancialChartDTO> getTopSellingItems(LocalDateTime startDate, LocalDateTime endDate, int limit);
    FinancialChartDTO getItemCategoryDistribution();
    FinancialChartDTO getItemProfitMargins(LocalDateTime startDate, LocalDateTime endDate);
    List<ItemSalesDTO> getDetailedItemSalesReport(LocalDateTime startDate, LocalDateTime endDate, String storeId);
    Map<String, BigDecimal> getItemCategoryRevenue(LocalDateTime startDate, LocalDateTime endDate);
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
