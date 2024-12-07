package com.konsol.core.service.impl;

import com.konsol.core.domain.*;
import com.konsol.core.domain.enumeration.MoneyKind;
import com.konsol.core.repository.*;
import com.konsol.core.service.BankService;
import com.konsol.core.service.FinancialDashboardService;
import com.konsol.core.service.api.dto.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FinancialDashboardServiceImpl implements FinancialDashboardService {

    private final Logger log = LoggerFactory.getLogger(FinancialDashboardServiceImpl.class);

    private final InvoiceRepository invoiceRepository;
    private final MoneyRepository moneyRepository;
    private final StoreRepository storeRepository;
    private final BankRepository bankRepository;
    private final AccountUserRepository accountUserRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final BankService bankService;

    public FinancialDashboardServiceImpl(
        InvoiceRepository invoiceRepository,
        MoneyRepository moneyRepository,
        StoreRepository storeRepository,
        BankRepository bankRepository,
        AccountUserRepository accountUserRepository,
        InvoiceItemRepository invoiceItemRepository,
        BankService bankService
    ) {
        this.invoiceRepository = invoiceRepository;
        this.moneyRepository = moneyRepository;
        this.storeRepository = storeRepository;
        this.bankRepository = bankRepository;
        this.accountUserRepository = accountUserRepository;
        this.invoiceItemRepository = invoiceItemRepository;
        this.bankService = bankService;
    }

    @Override
    public FinancialDashboardDTO getDashboardData(
        OffsetDateTime startDate,
        OffsetDateTime endDate,
        String storeId,
        String accountId,
        String bankId
    ) {
        FinancialDashboardDTO dashboard = new FinancialDashboardDTO();

        // Set filter parameters
        dashboard.setStartDate(startDate);
        dashboard.setEndDate(endDate);
        dashboard.setStoreId(storeId);
        dashboard.setAccountId(accountId);
        dashboard.setBankId(bankId);

        // Get all metrics and charts
        dashboard.setSalesMetrics(getSalesMetrics(startDate.toLocalDateTime(), endDate.toLocalDateTime(), storeId)); //*
        dashboard.setSalesCharts(getSalesCharts(startDate.toLocalDateTime(), endDate.toLocalDateTime(), storeId)); //*

        // Get invoice items analysis
        dashboard.setInvoiceItemAnalysis(getInvoiceItemAnalysis(startDate.toLocalDateTime(), endDate.toLocalDateTime(), storeId));
        dashboard.setItemSalesCharts(getItemSalesCharts(startDate.toLocalDateTime(), endDate.toLocalDateTime(), storeId));

        dashboard.setCashFlowMetrics(getCashFlowMetrics(startDate.toLocalDateTime(), endDate.toLocalDateTime(), bankId));
        dashboard.setCashFlowCharts(getCashFlowCharts(startDate.toLocalDateTime(), endDate.toLocalDateTime(), bankId));

        dashboard.setInvoiceAnalysis(getInvoiceAnalysis(storeId, startDate.toLocalDateTime(), endDate.toLocalDateTime()));
        dashboard.setInvoiceCharts(getInvoiceCharts(startDate.toLocalDateTime(), endDate.toLocalDateTime()));

        dashboard.setPerformanceIndicators(getPerformanceIndicators(startDate.toLocalDateTime(), endDate.toLocalDateTime()));
        dashboard.setPerformanceCharts(getPerformanceCharts(startDate.toLocalDateTime(), endDate.toLocalDateTime()));

        dashboard.setStoreAccountAnalysis(
            getStoreAccountAnalysis(startDate.toLocalDateTime(), endDate.toLocalDateTime(), storeId, accountId)
        );
        dashboard.setAnalysisCharts(getStoreAccountCharts(startDate.toLocalDateTime(), endDate.toLocalDateTime(), storeId, accountId));

        return dashboard;
    }

    @Override
    public SalesMetricsDTO getSalesMetrics(LocalDateTime startDate, LocalDateTime endDate, String storeId) {
        SalesMetricsDTO metrics = new SalesMetricsDTO();
        List<Invoice> invoices = storeId != null
            ? invoiceRepository.findInvoicesByStoreAndCreatedDateRange(storeId, startDate, endDate)
            : invoiceRepository.findByCreatedDateBetween(startDate, endDate);

        metrics.setTotalSales(calculateTotal(invoices, Invoice::getTotalPrice));
        metrics.setNetSales(calculateTotal(invoices, Invoice::getNetPrice));
        metrics.setTotalCost(calculateTotal(invoices, Invoice::getTotalCost));
        metrics.setNetCost(calculateTotal(invoices, Invoice::getNetCost));
        metrics.setNetProfit(calculateTotal(invoices, Invoice::getNetResult));

        // Calculate daily and monthly revenue totals
        Map<String, BigDecimal> dailyRevenue = calculateDailyRevenue(invoices);
        Map<String, BigDecimal> monthlyRevenue = calculateMonthlyRevenue(invoices);

        // Set the total daily and monthly revenue
        metrics.setDailyRevenue(dailyRevenue.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add));
        metrics.setMonthlyRevenue(monthlyRevenue.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add));

        return metrics;
    }

    @Override
    public List<FinancialChartDTO> getSalesCharts(LocalDateTime startDate, LocalDateTime endDate, String storeId) {
        List<FinancialChartDTO> charts = new ArrayList<>();
        charts.add(getDailySalesTrend(startDate, endDate));
        charts.add(getMonthlySalesTrend(startDate, endDate));
        charts.add(getSalesVsCostsComparison(startDate, endDate));
        charts.add(getProfitMarginDistribution());
        return charts;
    }

    @Override
    public FinancialChartDTO getDailySalesTrend(LocalDateTime startDate, LocalDateTime endDate) {
        FinancialChartDTO chart = new FinancialChartDTO();
        chart.setChartType("line");
        chart.setTitle("Daily Sales Trend");

        List<Invoice> invoices = invoiceRepository.findByCreatedDateBetween(startDate, endDate);
        Map<String, BigDecimal> dailyRevenue = new TreeMap<>();

        // Group sales by day
        invoices.forEach(invoice -> {
            String dayKey = LocalDateTime
                .ofInstant(invoice.getCreatedDate(), ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dailyRevenue.merge(dayKey, invoice.getTotalPrice(), BigDecimal::add);
        });

        SeriesData revenueSeries = new SeriesData();
        revenueSeries.setName("Daily Revenue");
        revenueSeries.setType("line");
        revenueSeries.setyAxisIndex(0);
        revenueSeries.setData(new ArrayList<>(dailyRevenue.values()));
        revenueSeries.setType("line");

        if (chart.getSeries() == null) {
            chart.setSeries(new ArrayList<>());
        }
        chart.getSeries().clear();
        chart.getSeries().add(revenueSeries);

        chart.setLabels(new ArrayList<>(dailyRevenue.keySet()));
        return chart;
    }

    @Override
    public FinancialChartDTO getMonthlySalesTrend(LocalDateTime startDate, LocalDateTime endDate) {
        FinancialChartDTO chart = new FinancialChartDTO();
        chart.setChartType("line");
        chart.setTitle("Monthly Sales Trend");

        List<Invoice> invoices = invoiceRepository.findByCreatedDateBetween(startDate, endDate);
        Map<String, BigDecimal> monthlyRevenue = new TreeMap<>();

        // Group sales by month
        invoices.forEach(invoice -> {
            String monthKey = LocalDateTime
                .ofInstant(invoice.getCreatedDate(), ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM"));
            monthlyRevenue.merge(monthKey, invoice.getTotalPrice(), BigDecimal::add);
        });

        SeriesData revenueSeries = new SeriesData();
        revenueSeries.setName("Monthly Revenue");
        revenueSeries.setType("line");
        revenueSeries.setyAxisIndex(0);
        revenueSeries.setData(new ArrayList<>(monthlyRevenue.values()));
        revenueSeries.setType("line");

        if (chart.getSeries() == null) {
            chart.setSeries(new ArrayList<>());
        }
        chart.getSeries().clear();
        chart.getSeries().add(revenueSeries);

        chart.setLabels(new ArrayList<>(monthlyRevenue.keySet()));
        return chart;
    }

    @Override
    public FinancialChartDTO getSalesVsCostsComparison(LocalDateTime startDate, LocalDateTime endDate) {
        FinancialChartDTO chart = new FinancialChartDTO();
        chart.setChartType("bar");
        chart.setTitle("Sales vs Costs Comparison");

        List<Invoice> invoices = invoiceRepository.findByCreatedDateBetween(startDate, endDate);
        Map<String, SalesVsCosts> monthlyData = new TreeMap<>();

        // Group sales and costs by month
        invoices.forEach(invoice -> {
            String monthKey = LocalDateTime
                .ofInstant(invoice.getCreatedDate(), ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM"));
            SalesVsCosts data = monthlyData.computeIfAbsent(monthKey, k -> new SalesVsCosts());
            data.addSales(invoice.getTotalPrice());
            data.addCosts(invoice.getTotalCost());
        });

        List<String> labels = new ArrayList<>(monthlyData.keySet());
        List<BigDecimal> sales = monthlyData.values().stream().map(SalesVsCosts::getSales).collect(Collectors.toList());
        List<BigDecimal> costs = monthlyData.values().stream().map(SalesVsCosts::getCosts).collect(Collectors.toList());

        SeriesData salesSeries = new SeriesData();
        salesSeries.setName("Sales");
        salesSeries.setType("line");
        salesSeries.setyAxisIndex(0);
        salesSeries.setData(sales);

        SeriesData costsSeries = new SeriesData();
        costsSeries.setName("Costs");
        costsSeries.setType("line");
        costsSeries.setyAxisIndex(1);
        costsSeries.setData(costs);

        if (chart.getSeries() == null) {
            chart.setSeries(new ArrayList<>());
        }
        chart.getSeries().clear();
        chart.getSeries().add(salesSeries);
        chart.getSeries().add(costsSeries);

        chart.setLabels(labels);
        return chart;
    }

    @Override
    public FinancialChartDTO getProfitMarginDistribution() {
        FinancialChartDTO chart = new FinancialChartDTO();
        chart.setChartType("pie");
        chart.setTitle("Profit Margin Distribution");

        // Get all invoices from the last month
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusMonths(1);
        List<Invoice> invoices = invoiceRepository.findByCreatedDateBetween(startDate, endDate);

        // Calculate profit margins and group them into ranges
        Map<String, Integer> marginRanges = new TreeMap<>();
        marginRanges.put("0-10%", 0);
        marginRanges.put("11-20%", 0);
        marginRanges.put("21-30%", 0);
        marginRanges.put("31-40%", 0);
        marginRanges.put("41%+", 0);

        invoices.forEach(invoice -> {
            BigDecimal revenue = invoice.getTotalPrice();
            BigDecimal cost = invoice.getTotalCost();
            if (revenue.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal margin = cost.subtract(revenue).multiply(new BigDecimal(100)).divide(revenue, 2, RoundingMode.HALF_UP);

                String range = getMarginRange(margin);
                marginRanges.merge(range, 1, Integer::sum);
            }
        });

        SeriesData marginSeries = new SeriesData();
        marginSeries.setName("Profit Margin");
        marginSeries.setData(new ArrayList<>(marginRanges.values().stream().map(BigDecimal::valueOf).collect(Collectors.toList())));
        marginSeries.setType("pie");

        if (chart.getSeries() == null) {
            chart.setSeries(new ArrayList<>());
        }
        chart.getSeries().clear();
        chart.getSeries().add(marginSeries);

        chart.setLabels(new ArrayList<>(marginRanges.keySet()));
        return chart;
    }

    private String getMarginRange(BigDecimal margin) {
        int marginInt = margin.intValue();
        if (marginInt <= 10) return "0-10%";
        if (marginInt <= 20) return "11-20%";
        if (marginInt <= 30) return "21-30%";
        if (marginInt <= 40) return "31-40%";
        return "41%+";
    }

    private static class SalesVsCosts {

        private BigDecimal sales = BigDecimal.ZERO;
        private BigDecimal costs = BigDecimal.ZERO;

        public void addSales(BigDecimal amount) {
            sales = sales.add(amount);
        }

        public void addCosts(BigDecimal amount) {
            costs = costs.add(amount);
        }

        public BigDecimal getSales() {
            return sales;
        }

        public BigDecimal getCosts() {
            return costs;
        }
    }

    @Override
    public InvoiceItemAnalysisDTO getInvoiceItemAnalysis(LocalDateTime startDate, LocalDateTime endDate, String storeId) {
        InvoiceItemAnalysisDTO analysis = new InvoiceItemAnalysisDTO();

        // Get all invoice items for the period
        List<InvoiceItem> invoiceItems = invoiceItemRepository.findByCreatedDateBetween(startDate, endDate);

        /*
        // Filter by store if specified
        if (storeId != null && !storeId.isEmpty()) {
            invoiceItems = invoiceItems.stream()
                .filter(item -> item.getStoreId() != null && item.getStoreId().equals(storeId))
                .collect(Collectors.toList());
        }
        */

        // Calculate metrics for each item
        Map<String, List<InvoiceItem>> itemGroups = invoiceItems
            .stream()
            .filter(item -> item.getItem() != null)
            .collect(Collectors.groupingBy(item -> item.getItem().getId()));

        // Calculate profit margins and convert to ItemProfitabilityDTO list
        List<ItemProfitabilityDTO> profitabilityList = itemGroups
            .entrySet()
            .stream()
            .map(entry -> {
                List<InvoiceItem> items = entry.getValue();
                Item item = items.get(0).getItem(); // Get item details from first invoice item

                BigDecimal totalRevenue = items
                    .stream()
                    .map(InvoiceItem::getTotalPrice)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalCost = items
                    .stream()
                    .map(InvoiceItem::getTotalCost)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalQuantity = items
                    .stream()
                    .map(InvoiceItem::getUnitQtyIn)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal grossProfit = totalRevenue.subtract(totalCost);
                BigDecimal profitMargin = totalRevenue.compareTo(BigDecimal.ZERO) > 0
                    ? grossProfit.multiply(BigDecimal.valueOf(100)).divide(totalRevenue, 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

                BigDecimal averagePrice = totalQuantity.compareTo(BigDecimal.ZERO) > 0
                    ? totalRevenue.divide(totalQuantity, 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

                BigDecimal averageCost = totalQuantity.compareTo(BigDecimal.ZERO) > 0
                    ? totalCost.divide(totalQuantity, 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

                ItemProfitabilityDTO profitability = new ItemProfitabilityDTO();
                profitability.setItemId(item.getId());
                profitability.setItemName(item.getName());
                profitability.setTotalRevenue(totalRevenue);
                profitability.setTotalCost(totalCost);
                profitability.setGrossProfit(grossProfit);
                profitability.setProfitMargin(profitMargin);
                profitability.setSalesVolume(totalQuantity.longValue());
                profitability.setAveragePrice(averagePrice);
                profitability.setAverageCost(averageCost);

                return profitability;
            })
            .sorted(Comparator.comparing(ItemProfitabilityDTO::getProfitMargin).reversed())
            .collect(Collectors.toList());

        // Set profit margins in analysis
        analysis.setItemProfitMargins(profitabilityList);

        // Find top selling item
        Optional<InvoiceItem> topItem = invoiceItems
            .stream()
            .filter(item -> item.getItem() != null)
            .max(Comparator.comparing(InvoiceItem::getTotalPrice));

        if (topItem.isPresent()) {
            analysis.setTopSellingItemRevenue(topItem.get().getTotalPrice());
            analysis.setTopSellingItemName(topItem.get().getItem().getName());
        }

        // Get category distribution
        analysis.setItemCategoryDistribution(getItemCategoryRevenue(startDate, endDate));

        // Get top selling items
        List<ItemSalesDTO> topItems = invoiceItems
            .stream()
            .filter(item -> item.getItem() != null)
            .collect(
                Collectors.groupingBy(
                    item -> item.getItem().getName(),
                    Collectors.collectingAndThen(
                        Collectors.toList(),
                        items -> {
                            ItemSalesDTO dto = new ItemSalesDTO();
                            dto.setItemName(items.get(0).getItem().getName());

                            BigDecimal quantity = items
                                .stream()
                                .map(InvoiceItem::getUnitQtyIn)
                                .filter(Objects::nonNull)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                            dto.setQuantity(quantity);

                            BigDecimal revenue = items
                                .stream()
                                .map(InvoiceItem::getTotalPrice)
                                .filter(Objects::nonNull)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                            dto.setRevenue(revenue);

                            BigDecimal cost = items
                                .stream()
                                .map(InvoiceItem::getTotalCost)
                                .filter(Objects::nonNull)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                            BigDecimal profit = revenue.subtract(cost);
                            dto.setProfit(profit);

                            BigDecimal profitMargin = revenue.compareTo(BigDecimal.ZERO) > 0
                                ? profit.multiply(BigDecimal.valueOf(100)).divide(revenue, 2, RoundingMode.HALF_UP)
                                : BigDecimal.ZERO;
                            dto.setProfitMargin(profitMargin);

                            return dto;
                        }
                    )
                )
            )
            .values()
            .stream()
            .sorted(Comparator.comparing(ItemSalesDTO::getRevenue).reversed())
            .limit(10)
            .collect(Collectors.toList());

        analysis.setTopSellingItems(topItems);

        // Get sales charts
        analysis.setItemSalesCharts(getItemSalesCharts(startDate, endDate, storeId));

        return analysis;
    }

    @Override
    public List<FinancialChartDTO> getItemSalesCharts(LocalDateTime startDate, LocalDateTime endDate, String storeId) {
        List<FinancialChartDTO> charts = new ArrayList<>();

        // Add daily sales trend chart
        charts.add(getItemSalesTrend(startDate, endDate));

        // Add top selling items charts (both revenue and quantity)
        charts.addAll(getTopSellingItems(startDate, endDate, 10));

        return charts;
    }

    @Override
    public FinancialChartDTO getItemSalesTrend(LocalDateTime startDate, LocalDateTime endDate) {
        // Get all invoice items for the period
        List<InvoiceItem> invoiceItems = invoiceItemRepository.findByCreatedDateBetween(startDate, endDate);

        // Group by date and calculate metrics
        Map<LocalDate, ItemSalesMetrics> dailyMetrics = invoiceItems
            .stream()
            .filter(item -> item.getCreatedDate() != null)
            .collect(
                Collectors.groupingBy(
                    item -> item.getCreatedDate().atZone(ZoneId.systemDefault()).toLocalDate(),
                    Collectors.collectingAndThen(
                        Collectors.toList(),
                        items -> {
                            BigDecimal totalRevenue = items
                                .stream()
                                .map(InvoiceItem::getTotalPrice)
                                .filter(Objects::nonNull)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                            BigDecimal totalQuantity = items
                                .stream()
                                .map(InvoiceItem::getUnitQtyIn)
                                .filter(Objects::nonNull)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                            return new ItemSalesMetrics(totalRevenue, totalQuantity);
                        }
                    )
                )
            );

        // Fill in missing dates with zero values
        LocalDate currentDate = startDate.toLocalDate();
        LocalDate endLocalDate = endDate.toLocalDate();

        while (!currentDate.isAfter(endLocalDate)) {
            dailyMetrics.putIfAbsent(currentDate, new ItemSalesMetrics(BigDecimal.ZERO, BigDecimal.ZERO));
            currentDate = currentDate.plusDays(1);
        }

        // Sort by date
        List<Map.Entry<LocalDate, ItemSalesMetrics>> sortedMetrics = new ArrayList<>(dailyMetrics.entrySet());
        sortedMetrics.sort(Map.Entry.comparingByKey());

        // Create chart
        FinancialChartDTO chart = new FinancialChartDTO();
        chart.setChartType("line");
        chart.setTitle("Daily Sales Trend");

        // Revenue series
        SeriesData revenueSeries = new SeriesData();
        revenueSeries.setName("Revenue (₺)");
        revenueSeries.setType("line");
        revenueSeries.setyAxisIndex(0);
        revenueSeries.setData(sortedMetrics.stream().map(entry -> entry.getValue().revenue).collect(Collectors.toList()));

        // Set style for revenue series
        Map<String, Object> revenueStyle = new HashMap<>();
        revenueStyle.put("color", "#2f4554");
        revenueStyle.put("smooth", true);
        revenueSeries.setStyle(revenueStyle);

        // Quantity series
        SeriesData quantitySeries = new SeriesData();
        quantitySeries.setName("Quantity");
        quantitySeries.setType("line");
        quantitySeries.setyAxisIndex(1);
        quantitySeries.setData(sortedMetrics.stream().map(entry -> entry.getValue().quantity).collect(Collectors.toList()));

        // Set style for quantity series
        Map<String, Object> quantityStyle = new HashMap<>();
        quantityStyle.put("color", "#61a0a8");
        quantityStyle.put("smooth", true);
        quantitySeries.setStyle(quantityStyle);

        if (chart.getSeries() == null) {
            chart.setSeries(new ArrayList<>());
        }
        chart.getSeries().clear();
        chart.getSeries().add(revenueSeries);
        chart.getSeries().add(quantitySeries);

        // Set labels (dates)
        chart.setLabels(
            sortedMetrics
                .stream()
                .map(entry -> entry.getKey().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .collect(Collectors.toList())
        );

        // Configure chart options
        Map<String, Object> options = new HashMap<>();
        options.put("showLegend", true);

        // Configure axes
        List<Map<String, Object>> yAxes = new ArrayList<>();

        // Revenue axis (left)
        Map<String, Object> revenueAxis = new HashMap<>();
        revenueAxis.put("name", "Revenue (₺)");
        revenueAxis.put("position", "left");
        revenueAxis.put("axisLabel", Map.of("formatter", "₺{value}"));
        yAxes.add(revenueAxis);

        // Quantity axis (right)
        Map<String, Object> quantityAxis = new HashMap<>();
        quantityAxis.put("name", "Quantity");
        quantityAxis.put("position", "right");
        yAxes.add(quantityAxis);

        options.put("yAxis", yAxes);

        // Configure tooltips
        options.put("tooltip", Map.of("trigger", "axis", "formatter", "{b}<br/>{a0}: ₺{c0}<br/>{a1}: {c1}"));

        chart.setOptions(options);

        return chart;
    }

    @Override
    public List<FinancialChartDTO> getTopSellingItems(LocalDateTime startDate, LocalDateTime endDate, int limit) {
        List<FinancialChartDTO> charts = new ArrayList<>();

        // Get invoice items for the date range
        List<InvoiceItem> invoiceItems = invoiceItemRepository.findByCreatedDateBetween(startDate, endDate);

        // Group and sum by item
        Map<String, ItemSalesMetrics> itemMetrics = invoiceItems
            .stream()
            .filter(item -> item.getItem() != null && item.getItem().getName() != null)
            .collect(
                Collectors.groupingBy(
                    item -> item.getItem().getName(),
                    Collectors.collectingAndThen(
                        Collectors.toList(),
                        items -> {
                            BigDecimal totalRevenue = items
                                .stream()
                                .map(InvoiceItem::getTotalPrice)
                                .filter(Objects::nonNull)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                            BigDecimal totalQuantity = items
                                .stream()
                                .map(InvoiceItem::getUnitQtyIn)
                                .filter(Objects::nonNull)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                            return new ItemSalesMetrics(totalRevenue, totalQuantity);
                        }
                    )
                )
            );

        // Sort by revenue and get top items
        List<Map.Entry<String, ItemSalesMetrics>> topItems = itemMetrics
            .entrySet()
            .stream()
            .sorted(Map.Entry.<String, ItemSalesMetrics>comparingByValue().reversed())
            .limit(limit)
            .collect(Collectors.toList());

        // Create revenue chart
        FinancialChartDTO revenueChart = new FinancialChartDTO();
        revenueChart.setChartType("bar");
        revenueChart.setTitle("Top " + limit + " Items by Revenue");

        SeriesData revenueSeries = new SeriesData();
        revenueSeries.setName("Revenue (₺)");
        revenueSeries.setType("bar");
        revenueSeries.setData(topItems.stream().map(entry -> entry.getValue().revenue).collect(Collectors.toList()));

        Map<String, Object> revenueStyle = new HashMap<>();
        revenueStyle.put("color", "#2f4554");
        revenueSeries.setStyle(revenueStyle);

        if (revenueChart.getSeries() == null) {
            revenueChart.setSeries(new ArrayList<>());
        }
        revenueChart.getSeries().clear();
        revenueChart.getSeries().add(revenueSeries);
        revenueChart.setLabels(topItems.stream().map(Map.Entry::getKey).collect(Collectors.toList()));

        // Configure revenue chart options
        Map<String, Object> revenueOptions = new HashMap<>();
        revenueOptions.put("showLegend", true);
        revenueOptions.put("yAxis", List.of(Map.of("name", "Revenue (₺)", "axisLabel", Map.of("formatter", "₺{value}"))));
        revenueOptions.put("tooltip", Map.of("trigger", "axis", "formatter", "{b}<br/>Revenue: ₺{c}"));
        revenueChart.setOptions(revenueOptions);

        // Create quantity chart
        FinancialChartDTO quantityChart = new FinancialChartDTO();
        quantityChart.setChartType("bar");
        quantityChart.setTitle("Top " + limit + " Items by Quantity");

        SeriesData quantitySeries = new SeriesData();
        quantitySeries.setName("Quantity");
        quantitySeries.setType("bar");
        quantitySeries.setData(topItems.stream().map(entry -> entry.getValue().quantity).collect(Collectors.toList()));

        Map<String, Object> quantityStyle = new HashMap<>();
        quantityStyle.put("color", "#61a0a8");
        quantitySeries.setStyle(quantityStyle);

        if (quantityChart.getSeries() == null) {
            quantityChart.setSeries(new ArrayList<>());
        }
        quantityChart.getSeries().clear();
        quantityChart.getSeries().add(quantitySeries);
        quantityChart.setLabels(topItems.stream().map(Map.Entry::getKey).collect(Collectors.toList()));

        // Configure quantity chart options
        Map<String, Object> quantityOptions = new HashMap<>();
        quantityOptions.put("showLegend", true);
        quantityOptions.put("yAxis", List.of(Map.of("name", "Quantity")));
        quantityOptions.put("tooltip", Map.of("trigger", "axis", "formatter", "{b}<br/>Quantity: {c}"));
        quantityChart.setOptions(quantityOptions);

        charts.add(revenueChart);
        charts.add(quantityChart);

        return charts;
    }

    @Override
    public FinancialChartDTO getItemCategoryDistribution() {
        FinancialChartDTO chart = new FinancialChartDTO();
        chart.setChartType("pie");
        chart.setTitle("Item Category Distribution");

        // Get current date range (last 30 days)
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(30);

        // Get category revenue distribution
        Map<String, BigDecimal> categoryRevenue = getItemCategoryRevenue(startDate, endDate);

        // Sort categories by revenue
        List<Map.Entry<String, BigDecimal>> sortedCategories = categoryRevenue
            .entrySet()
            .stream()
            .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
            .collect(Collectors.toList());

        // Create chart series
        SeriesData series = new SeriesData();
        series.setName("Revenue Distribution");
        series.setType("pie");

        // Set data
        series.setData(sortedCategories.stream().map(Map.Entry::getValue).collect(Collectors.toList()));

        if (chart.getSeries() == null) {
            chart.setSeries(new ArrayList<>());
        }
        chart.getSeries().clear();
        chart.getSeries().add(series);

        // Set labels (category names)
        chart.setLabels(sortedCategories.stream().map(Map.Entry::getKey).collect(Collectors.toList()));

        // Calculate percentages for tooltips
        BigDecimal totalRevenue = sortedCategories.stream().map(Map.Entry::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);

        // Add chart options
        Map<String, Object> options = new HashMap<>();
        options.put("showLegend", true);
        options.put("showPercentage", true);
        options.put(
            "tooltips",
            sortedCategories
                .stream()
                .map(entry -> {
                    BigDecimal percentage = entry
                        .getValue()
                        .multiply(BigDecimal.valueOf(100))
                        .divide(totalRevenue, 2, RoundingMode.HALF_UP);
                    return String.format(
                        "%s: %s%% (₺%s)",
                        entry.getKey(),
                        percentage.toString(),
                        entry.getValue().setScale(2, RoundingMode.HALF_UP).toString()
                    );
                })
                .collect(Collectors.toList())
        );
        chart.setOptions(options);

        return chart;
    }

    @Override
    public FinancialChartDTO getItemProfitMargins(LocalDateTime startDate, LocalDateTime endDate) {
        FinancialChartDTO chart = new FinancialChartDTO();
        chart.setChartType("bar");
        chart.setTitle("Item Profit Margins");

        // Get all invoice items in the date range
        List<InvoiceItem> invoiceItems = invoiceItemRepository.findByCreatedDateBetween(startDate, endDate);

        // Calculate profit margins by item
        Map<String, BigDecimal> profitMargins = invoiceItems
            .stream()
            .filter(item -> item.getItem() != null)
            .collect(
                Collectors.groupingBy(
                    item -> item.getItem().getName(),
                    Collectors.collectingAndThen(
                        Collectors.toList(),
                        items -> {
                            BigDecimal totalRevenue = items
                                .stream()
                                .map(InvoiceItem::getTotalPrice)
                                .filter(Objects::nonNull)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                            BigDecimal totalCost = items
                                .stream()
                                .map(InvoiceItem::getTotalCost)
                                .filter(Objects::nonNull)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                            if (totalRevenue.compareTo(BigDecimal.ZERO) > 0) {
                                return totalRevenue
                                    .subtract(totalCost)
                                    .multiply(BigDecimal.valueOf(100))
                                    .divide(totalRevenue, 2, RoundingMode.HALF_UP);
                            }
                            return BigDecimal.ZERO;
                        }
                    )
                )
            );

        // Sort by profit margin descending and limit to top 20
        List<Map.Entry<String, BigDecimal>> topMargins = profitMargins
            .entrySet()
            .stream()
            .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
            .limit(20)
            .collect(Collectors.toList());

        // Create chart series
        SeriesData series = new SeriesData();
        series.setName("Profit Margin (%)");
        series.setType("bar");
        series.setData(topMargins.stream().map(Map.Entry::getValue).collect(Collectors.toList()));

        if (chart.getSeries() == null) {
            chart.setSeries(new ArrayList<>());
        }
        chart.getSeries().clear();
        chart.getSeries().add(series);

        // Set labels (item names)
        chart.setLabels(topMargins.stream().map(Map.Entry::getKey).collect(Collectors.toList()));

        // Add chart options
        Map<String, Object> options = new HashMap<>();
        options.put("yAxisTitle", "Profit Margin (%)");
        options.put("xAxisTitle", "Items");
        options.put("showValues", true);
        chart.setOptions(options);

        return chart;
    }

    @Override
    public List<ItemProfitabilityDTO> getItemProfitabilityAnalysis(LocalDateTime startDate, LocalDateTime endDate) {
        // Get all invoice items in the date range
        List<InvoiceItem> invoiceItems = invoiceItemRepository.findByCreatedDateBetween(startDate, endDate);

        // Group by item and calculate metrics
        return invoiceItems
            .stream()
            .filter(item -> item.getItem() != null)
            .collect(Collectors.groupingBy(InvoiceItem::getItem))
            .entrySet()
            .stream()
            .map(entry -> {
                Item item = entry.getKey();
                List<InvoiceItem> items = entry.getValue();

                ItemProfitabilityDTO profitability = new ItemProfitabilityDTO();
                profitability.setItemId(item.getId());
                profitability.setItemName(item.getName());

                // Calculate total revenue
                BigDecimal totalRevenue = items
                    .stream()
                    .map(InvoiceItem::getTotalPrice)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                profitability.setTotalRevenue(totalRevenue);

                // Calculate total cost
                BigDecimal totalCost = items
                    .stream()
                    .map(InvoiceItem::getTotalCost)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                profitability.setTotalCost(totalCost);

                // Calculate gross profit
                BigDecimal grossProfit = totalRevenue.subtract(totalCost);
                profitability.setGrossProfit(grossProfit);

                // Calculate profit margin
                if (totalRevenue.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal profitMargin = grossProfit.multiply(BigDecimal.valueOf(100)).divide(totalRevenue, 2, RoundingMode.HALF_UP);
                    profitability.setProfitMargin(profitMargin);
                } else {
                    profitability.setProfitMargin(BigDecimal.ZERO);
                }

                // Calculate sales volume using unitQtyIn
                BigDecimal salesVolume = items
                    .stream()
                    .map(InvoiceItem::getUnitQtyIn)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                profitability.setSalesVolume(salesVolume.longValue());

                // Calculate average price and cost
                if (salesVolume.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal averagePrice = totalRevenue.divide(salesVolume, 2, RoundingMode.HALF_UP);
                    profitability.setAveragePrice(averagePrice);

                    BigDecimal averageCost = totalCost.divide(salesVolume, 2, RoundingMode.HALF_UP);
                    profitability.setAverageCost(averageCost);
                }

                return profitability;
            })
            .sorted((a, b) -> b.getGrossProfit().compareTo(a.getGrossProfit()))
            .collect(Collectors.toList());
    }

    @Override
    public List<ItemSalesDTO> getDetailedItemSalesReport(LocalDateTime startDate, LocalDateTime endDate, String storeId) {
        // Get all invoice items for the date range
        List<InvoiceItem> invoiceItems = invoiceItemRepository.findByCreatedDateBetween(startDate, endDate);

        // Group by item and calculate metrics
        return invoiceItems
            .stream()
            .filter(item -> item.getItem() != null)
            .collect(Collectors.groupingBy(InvoiceItem::getItem))
            .entrySet()
            .stream()
            .map(entry -> {
                Item item = entry.getKey();
                List<InvoiceItem> items = entry.getValue();

                ItemSalesDTO salesDTO = new ItemSalesDTO();
                salesDTO.setItemName(item.getName());
                salesDTO.setItemCode(item.getPk());

                // Calculate total quantity sold
                BigDecimal totalQuantity = items
                    .stream()
                    .map(InvoiceItem::getUnitQtyIn)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                salesDTO.setQuantity(totalQuantity);

                // Calculate total revenue
                BigDecimal totalRevenue = items
                    .stream()
                    .map(InvoiceItem::getTotalPrice)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                salesDTO.setRevenue(totalRevenue);

                // Calculate total cost and profit
                BigDecimal totalCost = items
                    .stream()
                    .map(InvoiceItem::getTotalCost)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal profit = totalRevenue.subtract(totalCost);
                salesDTO.setProfit(profit);

                // Calculate profit margin
                if (totalRevenue.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal profitMargin = profit.multiply(BigDecimal.valueOf(100)).divide(totalRevenue, 2, RoundingMode.HALF_UP);
                    salesDTO.setProfitMargin(profitMargin);
                } else {
                    salesDTO.setProfitMargin(BigDecimal.ZERO);
                }

                return salesDTO;
            })
            .sorted((a, b) -> b.getRevenue().compareTo(a.getRevenue()))
            .collect(Collectors.toList());
    }

    @Override
    public InvoiceItemAnalysisDTO getItemSalesAnalysis(LocalDateTime startDate, LocalDateTime endDate) {
        // Get all invoice items
        List<InvoiceItem> invoiceItems = invoiceItemRepository.findByCreatedDateBetween(startDate, endDate);

        InvoiceItemAnalysisDTO analysis = new InvoiceItemAnalysisDTO();

        // Calculate total items sold
        BigDecimal totalItemsSold = invoiceItems
            .stream()
            .map(InvoiceItem::getUnitQtyIn)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        analysis.setTotalItemsSold(totalItemsSold);

        // Calculate total revenue
        BigDecimal totalRevenue = invoiceItems
            .stream()
            .map(InvoiceItem::getTotalPrice)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        analysis.setTotalItemRevenue(totalRevenue);

        // Calculate average item price
        if (totalItemsSold.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal averagePrice = totalRevenue.divide(totalItemsSold, 2, RoundingMode.HALF_UP);
            analysis.setAverageItemPrice(averagePrice);
        }

        // Find top selling item by revenue
        Optional<InvoiceItem> topSellingItem = invoiceItems
            .stream()
            .filter(item -> item.getItem() != null)
            .max(Comparator.comparing(item -> item.getTotalPrice() != null ? item.getTotalPrice() : BigDecimal.ZERO));

        if (topSellingItem.isPresent()) {
            analysis.setTopSellingItemRevenue(topSellingItem.get().getTotalPrice());
            analysis.setTopSellingItemName(topSellingItem.get().getItem().getName());
        }

        // Calculate item category distribution
        Map<String, BigDecimal> categoryDistribution = invoiceItems
            .stream()
            .filter(item -> item.getItem() != null && item.getItem().getCategory() != null)
            .collect(
                Collectors.groupingBy(
                    item -> item.getItem().getCategory(),
                    Collectors.mapping(
                        InvoiceItem::getTotalPrice,
                        Collectors.reducing(
                            BigDecimal.ZERO,
                            (a, b) -> (a == null ? BigDecimal.ZERO : a).add(b == null ? BigDecimal.ZERO : b)
                        )
                    )
                )
            );
        analysis.setItemCategoryDistribution(categoryDistribution);

        // Calculate profit margins by item
        Map<String, BigDecimal> profitMargins = invoiceItems
            .stream()
            .filter(item -> item.getItem() != null)
            .collect(
                Collectors.groupingBy(
                    item -> item.getItem().getName(),
                    Collectors.collectingAndThen(
                        Collectors.toList(),
                        items -> {
                            BigDecimal totalRevenueX = items
                                .stream()
                                .map(InvoiceItem::getTotalPrice)
                                .filter(Objects::nonNull)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                            BigDecimal totalCost = items
                                .stream()
                                .map(InvoiceItem::getTotalCost)
                                .filter(Objects::nonNull)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                            if (totalRevenueX.compareTo(BigDecimal.ZERO) > 0) {
                                return totalRevenueX
                                    .subtract(totalCost)
                                    .multiply(BigDecimal.valueOf(100))
                                    .divide(totalRevenueX, 2, RoundingMode.HALF_UP);
                            }
                            return BigDecimal.ZERO;
                        }
                    )
                )
            );
        // analysis.setItemProfitMargins(profitMargins);

        // Get top selling items
        List<ItemSalesDTO> topItems = getDetailedItemSalesReport(startDate, endDate, null).stream().limit(10).collect(Collectors.toList());
        analysis.setTopSellingItems(topItems);

        return analysis;
    }

    @Override
    public PerformanceIndicatorsDTO getPerformanceIndicators(LocalDateTime startDate, LocalDateTime endDate) {
        PerformanceIndicatorsDTO indicators = new PerformanceIndicatorsDTO();

        List<Invoice> invoices = invoiceRepository.findByCreatedDateBetween(startDate, endDate);
        if (invoices.isEmpty()) {
            return indicators;
        }

        // Calculate Gross Profit Margin
        BigDecimal totalRevenue = calculateTotal(invoices, Invoice::getTotalPrice);
        BigDecimal totalCost = calculateTotal(invoices, Invoice::getTotalCost);
        if (totalRevenue.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal grossProfit = totalRevenue.subtract(totalCost);
            BigDecimal grossProfitMargin = grossProfit.multiply(BigDecimal.valueOf(100)).divide(totalRevenue, 2, RoundingMode.HALF_UP);
            indicators.setGrossProfitMargin(grossProfitMargin);
        }

        // Calculate Operating Expenses Ratio
        BigDecimal totalExpenses = calculateTotal(invoices, Invoice::getExpenses);
        if (totalRevenue.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal operatingExpensesRatio = totalExpenses
                .multiply(BigDecimal.valueOf(100))
                .divide(totalRevenue, 2, RoundingMode.HALF_UP);
            indicators.setOperatingExpensesRatio(operatingExpensesRatio);
        }

        // Calculate Net Profit Margin
        if (totalRevenue.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal netProfit = totalRevenue.subtract(totalCost).subtract(totalExpenses);
            BigDecimal netProfitMargin = netProfit.multiply(BigDecimal.valueOf(100)).divide(totalRevenue, 2, RoundingMode.HALF_UP);
            indicators.setNetProfitMargin(netProfitMargin);
        }

        // Calculate Revenue Growth
        BigDecimal revenueGrowth = calculateRevenueGrowth(startDate, endDate);
        indicators.setRevenueGrowth(revenueGrowth);

        // Calculate Current Ratio (Current Assets / Current Liabilities)
        // For simplicity, using bank balances as current assets and unpaid invoices as liabilities
        BigDecimal currentAssets = calculateCurrentAssets();
        BigDecimal currentLiabilities = calculateCurrentLiabilities(endDate);
        if (currentLiabilities.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal currentRatio = currentAssets.divide(currentLiabilities, 2, RoundingMode.HALF_UP);
            indicators.setCurrentRatio(currentRatio);
        }

        // Calculate Quick Ratio ((Current Assets - Inventory) / Current Liabilities)
        // Since we don't have inventory data, using the same as current ratio for now
        indicators.setQuickRatio(indicators.getCurrentRatio());

        return indicators;
    }

    @Override
    public List<FinancialChartDTO> getPerformanceCharts(LocalDateTime startDate, LocalDateTime endDate) {
        List<FinancialChartDTO> charts = new ArrayList<>();
        charts.add(getPerformanceRatios());
        charts.add(getMarginTrends(startDate, endDate));
        charts.add(getProfitBreakdown());
        return charts;
    }

    @Override
    public FinancialChartDTO getPerformanceRatios() {
        FinancialChartDTO chart = new FinancialChartDTO();
        chart.setChartType("radar");
        chart.setTitle("Performance Ratios");

        // Calculate various performance ratios
        List<String> labels = Arrays.asList("Gross Margin", "Net Margin", "Operating Margin", "ROI", "Cash Flow Ratio");
        List<BigDecimal> data = new ArrayList<>();
        // Add ratio calculations here

        SeriesData ratioSeries = new SeriesData();
        ratioSeries.setName("Ratios");
        ratioSeries.setData(data);
        ratioSeries.setType("radar");

        if (chart.getSeries() == null) {
            chart.setSeries(new ArrayList<>());
        }
        chart.getSeries().clear();
        chart.getSeries().add(ratioSeries);

        chart.setLabels(labels);
        return chart;
    }

    @Override
    public FinancialChartDTO getMarginTrends(LocalDateTime startDate, LocalDateTime endDate) {
        FinancialChartDTO chart = new FinancialChartDTO();
        chart.setChartType("line");
        chart.setTitle("Margin Trends");

        // Calculate margin trends over time
        List<String> labels = new ArrayList<>();
        List<BigDecimal> data = new ArrayList<>();
        // Add trend calculations here

        SeriesData marginSeries = new SeriesData();
        marginSeries.setName("Margin");
        marginSeries.setData(data);
        marginSeries.setType("line");

        if (chart.getSeries() == null) {
            chart.setSeries(new ArrayList<>());
        }
        chart.getSeries().clear();
        chart.getSeries().add(marginSeries);

        chart.setLabels(labels);
        return chart;
    }

    @Override
    public FinancialChartDTO getProfitBreakdown() {
        FinancialChartDTO chart = new FinancialChartDTO();
        chart.setChartType("pie");
        chart.setTitle("Profit Breakdown");

        // Calculate profit components
        List<String> labels = Arrays.asList("Gross Profit", "Operating Expenses", "Net Profit");
        List<BigDecimal> data = new ArrayList<>();
        // Add profit breakdown calculations here

        SeriesData profitSeries = new SeriesData();
        profitSeries.setName("Profit");
        profitSeries.setData(data);
        profitSeries.setType("pie");

        if (chart.getSeries() == null) {
            chart.setSeries(new ArrayList<>());
        }
        chart.getSeries().clear();
        chart.getSeries().add(profitSeries);

        chart.setLabels(labels);
        return chart;
    }

    @Override
    public StoreAccountAnalysisDTO getStoreAccountAnalysis(
        LocalDateTime startDate,
        LocalDateTime endDate,
        String storeId,
        String accountId
    ) {
        StoreAccountAnalysisDTO analysis = new StoreAccountAnalysisDTO();

        // Get store performance metrics
        if (storeId != null) {
            List<Invoice> storeInvoices = invoiceRepository.findByStoreIdAndCreatedDateBetween(storeId, startDate, endDate);

            // Create store metrics
            StoreMetricsDTO storeMetrics = new StoreMetricsDTO();
            storeMetrics.setStoreId(storeId);
            storeMetrics.setTotalRevenue(calculateTotal(storeInvoices, Invoice::getTotalPrice));
            storeMetrics.setTotalCost(calculateTotal(storeInvoices, Invoice::getTotalCost));
            storeMetrics.setTransactionCount(storeInvoices.size());

            // Calculate average transaction value
            if (!storeInvoices.isEmpty()) {
                BigDecimal avgValue = storeMetrics.getTotalRevenue().divide(new BigDecimal(storeInvoices.size()), 2, RoundingMode.HALF_UP);
                storeMetrics.setAverageTransactionValue(avgValue);
            }

            // Calculate profit margin
            if (storeMetrics.getTotalRevenue().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal profitMargin = storeMetrics
                    .getTotalRevenue()
                    .subtract(storeMetrics.getTotalCost())
                    .multiply(new BigDecimal("100"))
                    .divide(storeMetrics.getTotalRevenue(), 2, RoundingMode.HALF_UP);
                storeMetrics.setProfitMargin(profitMargin);
            }

            if (analysis.getStoreMetrics() == null) {
                analysis.setStoreMetrics(new ArrayList<>());
                // Add metrics to analysis
            }
            analysis.getStoreMetrics().add(storeMetrics);
        }

        // Get account performance metrics
        if (accountId != null) {
            List<Invoice> accountInvoices = invoiceRepository.findByAccountIdAndCreatedDateBetween(accountId, startDate, endDate);

            // Create account metrics
            AccountMetricsDTO accountMetrics = new AccountMetricsDTO();
            accountMetrics.setAccountId(accountId);
            accountMetrics.setTotalTransactionVolume(calculateTotal(accountInvoices, Invoice::getTotalPrice));
            accountMetrics.setTransactionCount(accountInvoices.size());

            // Calculate average transaction value
            if (!accountInvoices.isEmpty()) {
                BigDecimal avgValue = accountMetrics
                    .getTotalTransactionVolume()
                    .divide(new BigDecimal(accountInvoices.size()), 2, RoundingMode.HALF_UP);
                accountMetrics.setAverageTransactionValue(avgValue);
            }

            if (analysis.getAccountMetrics() == null) {
                analysis.setAccountMetrics(new ArrayList<>());
            }
            // Add metrics to analysis
            analysis.getAccountMetrics().add(accountMetrics);
        }

        return analysis;
    }

    @Override
    public List<FinancialChartDTO> getStoreAccountCharts(LocalDateTime startDate, LocalDateTime endDate, String storeId, String accountId) {
        List<FinancialChartDTO> charts = new ArrayList<>();

        if (storeId != null) {
            charts.add(getStorePerformanceChart(startDate, endDate, storeId));
            charts.add(getStoreRevenueDistribution(storeId));
        }

        if (accountId != null) {
            charts.add(getAccountPerformanceChart(startDate, endDate, accountId));
            charts.add(getAccountRevenueDistribution(accountId));
        }

        return charts;
    }

    private FinancialChartDTO getStorePerformanceChart(LocalDateTime startDate, LocalDateTime endDate, String storeId) {
        FinancialChartDTO chart = new FinancialChartDTO();
        chart.setChartType("line");
        chart.setTitle("Store Performance");

        List<Invoice> invoices = invoiceRepository.findByStoreIdAndCreatedDateBetween(storeId, startDate, endDate);
        Map<String, BigDecimal> dailyRevenue = calculateDailyRevenue(invoices);

        SeriesData revenueSeries = new SeriesData();
        revenueSeries.setName("Daily Revenue");
        revenueSeries.setData(new ArrayList<>(dailyRevenue.values()));
        revenueSeries.setType("line");

        if (chart.getSeries() == null) {
            chart.setSeries(new ArrayList<>());
        }
        chart.getSeries().clear();
        chart.getSeries().add(revenueSeries);

        chart.setLabels(new ArrayList<>(dailyRevenue.keySet()));
        return chart;
    }

    private FinancialChartDTO getStoreRevenueDistribution(String storeId) {
        FinancialChartDTO chart = new FinancialChartDTO();
        chart.setChartType("pie");
        chart.setTitle("Store Revenue Distribution");

        // Calculate revenue distribution by product category or department
        List<String> labels = new ArrayList<>();
        List<BigDecimal> data = new ArrayList<>();
        // Add distribution calculations here

        SeriesData revenueSeries = new SeriesData();
        revenueSeries.setName("Revenue");
        revenueSeries.setData(data);
        revenueSeries.setType("pie");

        if (chart.getSeries() == null) {
            chart.setSeries(new ArrayList<>());
        }
        chart.getSeries().clear();
        chart.getSeries().add(revenueSeries);

        chart.setLabels(labels);
        return chart;
    }

    private FinancialChartDTO getAccountPerformanceChart(LocalDateTime startDate, LocalDateTime endDate, String accountId) {
        FinancialChartDTO chart = new FinancialChartDTO();
        chart.setChartType("line");
        chart.setTitle("Account Performance");

        List<Invoice> invoices = invoiceRepository.findByAccountIdAndCreatedDateBetween(accountId, startDate, endDate);
        Map<String, BigDecimal> dailyRevenue = calculateDailyRevenue(invoices);

        SeriesData revenueSeries = new SeriesData();
        revenueSeries.setName("Daily Revenue");
        revenueSeries.setData(new ArrayList<>(dailyRevenue.values()));
        revenueSeries.setType("line");

        if (chart.getSeries() == null) {
            chart.setSeries(new ArrayList<>());
        }
        chart.getSeries().clear();
        chart.getSeries().add(revenueSeries);

        chart.setLabels(new ArrayList<>(dailyRevenue.keySet()));
        return chart;
    }

    private FinancialChartDTO getAccountRevenueDistribution(String accountId) {
        FinancialChartDTO chart = new FinancialChartDTO();
        chart.setChartType("pie");
        chart.setTitle("Account Revenue Distribution");

        // Calculate revenue distribution by transaction type or category
        List<String> labels = new ArrayList<>();
        List<BigDecimal> data = new ArrayList<>();
        // Add distribution calculations here

        SeriesData revenueSeries = new SeriesData();
        revenueSeries.setName("Revenue");
        revenueSeries.setData(data);
        revenueSeries.setType("pie");

        if (chart.getSeries() == null) {
            chart.setSeries(new ArrayList<>());
        }
        chart.getSeries().clear();
        chart.getSeries().add(revenueSeries);

        chart.setLabels(labels);
        return chart;
    }

    @Override
    public CashFlowMetricsDTO getCashFlowMetrics(LocalDateTime startDate, LocalDateTime endDate, String bankId) {
        CashFlowMetricsDTO metrics = new CashFlowMetricsDTO();

        // Get money transactions
        List<Money> moneyTransactions = moneyRepository.findByCreatedDateBetween(startDate, endDate);
        if (bankId != null) {
            moneyTransactions = moneyTransactions.stream().filter(m -> bankId.equals(m.getBank().getId())).collect(Collectors.toList());
        }

        // Calculate money in/out
        BigDecimal moneyIn = moneyTransactions
            .stream()
            .filter(m -> m.getKind() == MoneyKind.RECEIPT)
            .map(Money::getMoneyIn)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal moneyOut = moneyTransactions
            .stream()
            .filter(m -> m.getKind() == MoneyKind.PAYMENT)
            .map(Money::getMoneyOut)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Get bank balances
        List<Bank> banks = bankId != null
            ? Collections.singletonList(bankRepository.findById(bankId).orElse(null))
            : bankRepository.findAll();

        // Create bank balances map
        Map<String, BigDecimal> bankBalancesMap = new HashMap<>();
        banks
            .stream()
            .filter(Objects::nonNull)
            .forEach(bank -> {
                com.konsol.core.service.dto.BankBalanceDTO bankBalance = bankService.calculateBankBalance(bank.getId());
                bankBalancesMap.put(
                    bank.getId(),
                    bankBalance.getTotalBalance() != null ? BigDecimal.valueOf(bankBalance.getTotalBalance()) : BigDecimal.ZERO
                );
            });

        // Set metrics
        metrics.setTotalMoneyIn(moneyIn);
        metrics.setTotalMoneyOut(moneyOut);
        metrics.setCurrentCashPosition(moneyIn.subtract(moneyOut)); // Net cash flow is stored as current cash position
        metrics.setBankBalances(bankBalancesMap);

        return metrics;
    }

    @Override
    public List<FinancialChartDTO> getCashFlowCharts(LocalDateTime startDate, LocalDateTime endDate, String bankId) {
        List<FinancialChartDTO> charts = new ArrayList<>();
        charts.add(getCashFlowTrend(startDate, endDate));
        charts.add(getMoneyInOutComparison(startDate, endDate));
        charts.add(getBankDistribution());
        return charts;
    }

    public com.konsol.core.service.api.dto.FinancialChartDTO create(String chartId, String title, String chartType) {
        com.konsol.core.service.api.dto.FinancialChartDTO chart = new com.konsol.core.service.api.dto.FinancialChartDTO();
        chart.setChartId(chartId);
        chart.setTitle(title);
        chart.setChartType(chartType);
        return chart;
    }

    public com.konsol.core.service.api.dto.FinancialChartDTO create(
        String chartId,
        String title,
        String subtitle,
        String chartType,
        String xAxisLabel,
        String yAxisLabel
    ) {
        com.konsol.core.service.api.dto.FinancialChartDTO chart = new com.konsol.core.service.api.dto.FinancialChartDTO();
        chart.setChartId(chartId);
        chart.setTitle(title);
        chart.setSubtitle(subtitle);
        chart.setChartType(chartType);
        chart.setxAxisLabel(xAxisLabel);
        chart.setyAxisLabel(yAxisLabel);
        return chart;
    }

    @Override
    public FinancialChartDTO getCashFlowTrend(LocalDateTime startDate, LocalDateTime endDate) {
        FinancialChartDTO chart = this.create("cashflow-trend", "Cash Flow Trend", "line");

        List<Money> transactions = moneyRepository.findByCreatedDateBetween(startDate, endDate);
        Map<String, BigDecimal> dailyNet = new TreeMap<>(); // TreeMap for sorted dates

        transactions.forEach(money -> {
            String dateKey = LocalDateTime.ofInstant(money.getCreatedDate(), ZoneId.systemDefault()).format(DateTimeFormatter.ISO_DATE);
            BigDecimal amount = money.getKind() == MoneyKind.RECEIPT ? money.getMoneyIn() : money.getMoneyOut().negate();
            dailyNet.merge(dateKey, amount, BigDecimal::add);
        });

        SeriesData netSeries = new SeriesData();
        netSeries.setName("Net Cash Flow");
        netSeries.setData(new ArrayList<>(dailyNet.values()));
        netSeries.setType("line");

        if (chart.getSeries() == null) {
            chart.setSeries(new ArrayList<>());
        }
        chart.getSeries().clear();
        chart.getSeries().add(netSeries);

        chart.setLabels(new ArrayList<>(dailyNet.keySet()));
        return chart;
    }

    @Override
    public FinancialChartDTO getMoneyInOutComparison(LocalDateTime startDate, LocalDateTime endDate) {
        FinancialChartDTO chart =
            this.create("money-in-out", "Money In/Out Comparison", "Monthly Income vs Expenses", "bar", "Month", "Amount");

        List<Money> transactions = moneyRepository.findByCreatedDateBetween(startDate, endDate);
        Map<String, Map<MoneyKind, BigDecimal>> monthlyComparison = new TreeMap<>();

        transactions.forEach(money -> {
            String monthKey = LocalDateTime
                .ofInstant(money.getCreatedDate(), ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM"));
            monthlyComparison.computeIfAbsent(monthKey, k -> new EnumMap<>(MoneyKind.class));
            if (money.getKind() == MoneyKind.RECEIPT) {
                monthlyComparison.get(monthKey).merge(MoneyKind.RECEIPT, money.getMoneyIn(), BigDecimal::add);
            } else {
                monthlyComparison.get(monthKey).merge(MoneyKind.PAYMENT, money.getMoneyOut(), BigDecimal::add);
            }
        });

        List<String> labels = new ArrayList<>(monthlyComparison.keySet());
        List<BigDecimal> moneyIn = new ArrayList<>();
        List<BigDecimal> moneyOut = new ArrayList<>();

        monthlyComparison.forEach((month, typeMap) -> {
            moneyIn.add(typeMap.getOrDefault(MoneyKind.RECEIPT, BigDecimal.ZERO));
            moneyOut.add(typeMap.getOrDefault(MoneyKind.PAYMENT, BigDecimal.ZERO));
        });

        SeriesData moneyInSeries = new SeriesData();
        moneyInSeries.setName("Money In");
        moneyInSeries.setType("bar");
        moneyInSeries.setyAxisIndex(0);
        moneyInSeries.setData(moneyIn);

        SeriesData moneyOutSeries = new SeriesData();
        moneyOutSeries.setName("Money Out");
        moneyOutSeries.setType("bar");
        moneyOutSeries.setyAxisIndex(1);
        moneyOutSeries.setData(moneyOut);

        if (chart.getSeries() == null) {
            chart.setSeries(new ArrayList<>());
        }
        chart.getSeries().clear();
        chart.getSeries().add(moneyInSeries);
        chart.getSeries().add(moneyOutSeries);

        Map<String, Object> incomeStyle = new HashMap<>();
        incomeStyle.put("color", "#4CAF50");
        Map<String, Object> expenseStyle = new HashMap<>();
        expenseStyle.put("color", "#F44336");

        chart.getSeries().get(0).setStyle(incomeStyle);
        chart.getSeries().get(1).setStyle(expenseStyle);

        chart.setLabels(labels);
        return chart;
    }

    @Override
    public FinancialChartDTO getBankDistribution() {
        FinancialChartDTO chart = new FinancialChartDTO();
        chart.setChartType("pie");
        chart.setTitle("Bank Balance Distribution");

        Map<String, BigDecimal> bankTransactions = new HashMap<>();
        List<Bank> banks = bankRepository.findAll();

        banks.forEach(bank -> {
            com.konsol.core.service.dto.BankBalanceDTO bankBalance = bankService.calculateBankBalance(bank.getId());
            bankTransactions.put(
                bank.getName(),
                bankBalance.getTotalBalance() != null ? BigDecimal.valueOf(bankBalance.getTotalBalance()) : BigDecimal.ZERO
            );
        });

        SeriesData transactionSeries = new SeriesData();
        transactionSeries.setName("Bank Balances");
        transactionSeries.setData(new ArrayList<>(bankTransactions.values()));
        transactionSeries.setType("pie");

        if (chart.getSeries() == null) {
            chart.setSeries(new ArrayList<>());
        }
        chart.getSeries().clear();
        chart.getSeries().add(transactionSeries);

        // Add pie chart specific options
        Map<String, Object> options = new HashMap<>();
        options.put("showLegend", true);
        options.put("showPercentage", true);
        chart.setOptions(options);

        chart.setLabels(new ArrayList<>(bankTransactions.keySet()));
        return chart;
    }

    @Override
    public InvoiceAnalysisDTO getInvoiceAnalysis(String storeId, LocalDateTime startDate, LocalDateTime endDate) {
        InvoiceAnalysisDTO analysis = new InvoiceAnalysisDTO();

        List<Invoice> invoices = storeId != null
            ? invoiceRepository.findByStoreIdAndCreatedDateBetween(storeId, startDate, endDate)
            : invoiceRepository.findByCreatedDateBetween(startDate, endDate);

        if (invoices.isEmpty()) {
            return analysis;
        }

        // Calculate average discount percentage
        int totalDiscountPercentage = invoices.stream().filter(i -> i.getDiscountPer() != null).mapToInt(Invoice::getDiscountPer).sum();
        analysis.setAverageDiscountPercentage(invoices.isEmpty() ? 0 : totalDiscountPercentage / invoices.size());

        // Calculate average discount amount
        BigDecimal totalDiscount = invoices
            .stream()
            .filter(i -> i.getDiscount() != null)
            .map(Invoice::getDiscount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        analysis.setAverageDiscount(
            invoices.isEmpty() ? BigDecimal.ZERO : totalDiscount.divide(BigDecimal.valueOf(invoices.size()), 2, RoundingMode.HALF_UP)
        );

        // Calculate total additions
        BigDecimal totalAdditions = invoices
            .stream()
            .filter(i -> i.getAdditions() != null)
            .map(Invoice::getAdditions)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        analysis.setTotalAdditions(totalAdditions);

        // Calculate total expenses
        BigDecimal totalExpenses = invoices
            .stream()
            .filter(i -> i.getExpenses() != null)
            .map(Invoice::getExpenses)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        analysis.setTotalExpenses(totalExpenses);

        // Count deferred invoices
        long deferredCount = invoices.stream().filter(Invoice::isDeferred).count();
        analysis.setDeferredInvoicesCount(deferredCount);

        // Group expenses by type
        Map<String, BigDecimal> expensesByType = invoices
            .stream()
            .filter(i -> i.getExpensesType() != null && i.getExpenses() != null)
            .collect(
                Collectors.groupingBy(Invoice::getExpensesType, Collectors.reducing(BigDecimal.ZERO, Invoice::getExpenses, BigDecimal::add))
            );
        analysis.setExpensesByType(expensesByType);

        return analysis;
    }

    @Override
    public List<FinancialChartDTO> getInvoiceCharts(LocalDateTime startDate, LocalDateTime endDate) {
        List<FinancialChartDTO> charts = new ArrayList<>();
        charts.add(getDiscountDistribution());
        charts.add(getExpensesByTypeChart(startDate, endDate));
        charts.add(getDeferredInvoicesTrend(startDate, endDate));
        return charts;
    }

    @Override
    public FinancialChartDTO getDiscountDistribution() {
        FinancialChartDTO chart = new FinancialChartDTO();
        chart.setChartType("pie");
        chart.setTitle("Discount Distribution");

        List<Invoice> invoices = invoiceRepository.findAll();
        Map<String, BigDecimal> discountRanges = new TreeMap<>();

        invoices.forEach(invoice -> {
            Integer discountPerInt = invoice.getDiscountPer();
            BigDecimal discountPercentage = discountPerInt != null ? BigDecimal.valueOf(discountPerInt.longValue()) : BigDecimal.ZERO;
            String range = getDiscountRange(discountPercentage);
            discountRanges.merge(range, BigDecimal.ONE, BigDecimal::add);
        });

        SeriesData discountSeries = new SeriesData();
        discountSeries.setName("Discount");
        discountSeries.setData(new ArrayList<>(discountRanges.values()));
        discountSeries.setType("pie");

        if (chart.getSeries() == null) {
            chart.setSeries(new ArrayList<>());
        }
        chart.getSeries().clear();
        chart.getSeries().add(discountSeries);

        chart.setLabels(new ArrayList<>(discountRanges.keySet()));
        return chart;
    }

    @Override
    public FinancialChartDTO getExpensesByTypeChart(LocalDateTime startDate, LocalDateTime endDate) {
        FinancialChartDTO chart = new FinancialChartDTO();
        chart.setChartType("bar");
        chart.setTitle("Expenses by Type");

        List<Money> expenses = moneyRepository.findByKindAndCreatedDateBetween(MoneyKind.PAYMENT, startDate, endDate);

        Map<String, BigDecimal> expenseTypes = expenses
            .stream()
            .filter(money -> money.getDetails() != null && money.getMoneyOut() != null)
            .collect(Collectors.groupingBy(Money::getDetails, Collectors.reducing(BigDecimal.ZERO, Money::getMoneyOut, BigDecimal::add)));

        // Sort by amount descending
        List<Map.Entry<String, BigDecimal>> sortedExpenses = new ArrayList<>(expenseTypes.entrySet());
        sortedExpenses.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        // Create series data
        SeriesData series = new SeriesData();
        series.setName("Amount");
        series.setType("bar");
        series.setData(sortedExpenses.stream().map(Map.Entry::getValue).collect(Collectors.toList()));

        // Set chart data
        if (chart.getSeries() == null) {
            chart.setSeries(new ArrayList<>());
        }
        chart.getSeries().clear();
        chart.getSeries().add(series);
        chart.setLabels(sortedExpenses.stream().map(Map.Entry::getKey).collect(Collectors.toList()));

        return chart;
    }

    @Override
    public FinancialChartDTO getDeferredInvoicesTrend(LocalDateTime startDate, LocalDateTime endDate) {
        FinancialChartDTO chart = new FinancialChartDTO();
        chart.setChartType("line");
        chart.setTitle("Deferred Invoices Trend");

        List<Invoice> deferredInvoices = invoiceRepository.findByDeferredAndCreatedDateBetween(true, startDate, endDate);
        Map<String, BigDecimal> monthlyDeferred = new TreeMap<>();

        deferredInvoices.forEach(invoice -> {
            String monthKey = LocalDateTime
                .ofInstant(invoice.getCreatedDate(), ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM"));
            monthlyDeferred.merge(monthKey, invoice.getTotalPrice(), BigDecimal::add);
        });

        SeriesData deferredSeries = new SeriesData();
        deferredSeries.setName("Deferred Invoices");
        deferredSeries.setData(new ArrayList<>(monthlyDeferred.values()));
        deferredSeries.setType("line");

        if (chart.getSeries() == null) {
            chart.setSeries(new ArrayList<>());
        }
        chart.getSeries().clear();
        chart.getSeries().add(deferredSeries);

        chart.setLabels(new ArrayList<>(monthlyDeferred.keySet()));
        return chart;
    }

    private String getDiscountRange(BigDecimal percentage) {
        if (percentage.compareTo(BigDecimal.ZERO) == 0) return "No Discount";
        if (percentage.compareTo(new BigDecimal("5")) <= 0) return "0-5%";
        if (percentage.compareTo(new BigDecimal("10")) <= 0) return "5-10%";
        if (percentage.compareTo(new BigDecimal("20")) <= 0) return "10-20%";
        if (percentage.compareTo(new BigDecimal("30")) <= 0) return "20-30%";
        return "30%+";
    }

    // Helper methods for calculations
    private BigDecimal calculateTotal(List<Invoice> invoices, java.util.function.Function<Invoice, BigDecimal> valueExtractor) {
        return invoices.stream().map(valueExtractor).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Map<String, BigDecimal> calculateDailyRevenue(List<Invoice> invoices) {
        Map<String, BigDecimal> dailyRevenue = new TreeMap<>(); // TreeMap for sorted dates

        invoices.forEach(invoice -> {
            String dateKey = LocalDateTime.ofInstant(invoice.getCreatedDate(), ZoneId.systemDefault()).format(DateTimeFormatter.ISO_DATE);
            dailyRevenue.merge(dateKey, invoice.getTotalPrice(), BigDecimal::add);
        });

        return dailyRevenue;
    }

    private Map<String, BigDecimal> calculateMonthlyRevenue(List<Invoice> invoices) {
        Map<String, BigDecimal> monthlyRevenue = new TreeMap<>(); // TreeMap for sorted months

        invoices.forEach(invoice -> {
            String monthKey = LocalDateTime
                .ofInstant(invoice.getCreatedDate(), ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM"));
            monthlyRevenue.merge(monthKey, invoice.getTotalPrice(), BigDecimal::add);
        });

        return monthlyRevenue;
    }

    private Map<String, BigDecimal> calculateCategoryRevenue(List<Invoice> invoices) {
        Map<String, BigDecimal> categoryRevenue = new HashMap<>();

        invoices.forEach(invoice -> {
            invoice
                .getInvoiceItems()
                .forEach(item -> {
                    categoryRevenue.merge(item.getItem().getCategory(), item.getPrice().multiply(item.getQtyOut()), BigDecimal::add);
                });
        });

        return categoryRevenue;
    }

    private Map<String, BigDecimal> calculateCategoryProfitMargins(List<Invoice> invoices) {
        Map<String, BigDecimalSummaryStatistics> categoryStats = new HashMap<>();

        // Collect revenue and cost by category
        invoices.forEach(invoice -> {
            invoice
                .getInvoiceItems()
                .forEach(item -> {
                    String category = item.getItem().getCategory();
                    BigDecimal revenue = item.getPrice().multiply(item.getQtyOut());
                    BigDecimal cost = item.getCost().multiply(item.getQtyIn());

                    categoryStats.computeIfAbsent(category, k -> new BigDecimalSummaryStatistics()).accept(revenue, cost);
                });
        });

        // Calculate profit margins
        Map<String, BigDecimal> profitMargins = new HashMap<>();
        categoryStats.forEach((category, stats) -> {
            if (stats.getRevenue().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal margin = stats.getProfit().divide(stats.getRevenue(), 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
                profitMargins.put(category, margin);
            }
        });

        return profitMargins;
    }

    // Helper class for category statistics
    private static class BigDecimalSummaryStatistics {

        private BigDecimal revenue = BigDecimal.ZERO;
        private BigDecimal cost = BigDecimal.ZERO;

        public void accept(BigDecimal revenue, BigDecimal cost) {
            this.revenue = this.revenue.add(revenue);
            this.cost = this.cost.add(cost);
        }

        public BigDecimal getRevenue() {
            return revenue;
        }

        public BigDecimal getCost() {
            return cost;
        }

        public BigDecimal getProfit() {
            return revenue.subtract(cost);
        }
    }

    private BigDecimal calculateRevenueGrowth(LocalDateTime startDate, LocalDateTime endDate) {
        // Calculate revenue for current period
        List<Invoice> currentPeriodInvoices = invoiceRepository.findByCreatedDateBetween(startDate, endDate);
        BigDecimal currentRevenue = calculateTotal(currentPeriodInvoices, Invoice::getTotalPrice);

        // Calculate revenue for previous period
        long periodDays = ChronoUnit.DAYS.between(startDate, endDate);
        LocalDateTime previousStartDate = startDate.minusDays(periodDays);
        LocalDateTime previousEndDate = startDate;
        List<Invoice> previousPeriodInvoices = invoiceRepository.findByCreatedDateBetween(previousStartDate, previousEndDate);
        BigDecimal previousRevenue = calculateTotal(previousPeriodInvoices, Invoice::getTotalPrice);

        // Calculate growth percentage
        if (previousRevenue.compareTo(BigDecimal.ZERO) > 0) {
            return currentRevenue
                .subtract(previousRevenue)
                .divide(previousRevenue, 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal calculateCurrentAssets() {
        // Get total bank balances as current assets
        List<Bank> banks = bankRepository.findAll();
        BigDecimal totalBankBalance = BigDecimal.ZERO;

        for (Bank bank : banks) {
            com.konsol.core.service.dto.BankBalanceDTO bankBalance = bankService.calculateBankBalance(bank.getId());
            if (bankBalance.getTotalBalance() != null) {
                totalBankBalance = totalBankBalance.add(BigDecimal.valueOf(bankBalance.getTotalBalance()));
            }
        }

        return totalBankBalance;
    }

    private BigDecimal calculateCurrentLiabilities(LocalDateTime endDate) {
        // Get unpaid invoices and money owed as current liabilities
        LocalDateTime startDate = endDate.minusMonths(3); // Consider liabilities from last 3 months

        // Get unpaid invoices
        List<Invoice> unpaidInvoices = invoiceRepository.findByCreatedDateBetweenAndDeferred(startDate, endDate, true);
        BigDecimal totalUnpaidInvoices = unpaidInvoices
            .stream()
            .map(Invoice::getTotalPrice)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Get outstanding payments
        List<Money> outstandingPayments = moneyRepository.findByKindAndCreatedDateBetween(MoneyKind.PAYMENT, startDate, endDate);
        BigDecimal totalOutstandingPayments = outstandingPayments
            .stream()
            .map(Money::getMoneyOut)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalUnpaidInvoices.add(totalOutstandingPayments);
    }

    @Override
    public Map<String, BigDecimal> getItemCategoryRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        // Get all invoice items in the date range
        List<InvoiceItem> invoiceItems = invoiceItemRepository.findByCreatedDateBetween(startDate, endDate);

        // Group by category and sum up total revenue
        return invoiceItems
            .stream()
            .filter(item -> item.getItem() != null && item.getItem().getCategory() != null)
            .collect(
                Collectors.groupingBy(
                    item -> item.getItem().getCategory(),
                    Collectors.mapping(
                        InvoiceItem::getTotalPrice,
                        Collectors.reducing(
                            BigDecimal.ZERO,
                            (a, b) -> (a == null ? BigDecimal.ZERO : a).add(b == null ? BigDecimal.ZERO : b)
                        )
                    )
                )
            );
    }

    // Helper class for item metrics
    private static class ItemSalesMetrics implements Comparable<ItemSalesMetrics> {

        final BigDecimal revenue;
        final BigDecimal quantity;

        ItemSalesMetrics(BigDecimal revenue, BigDecimal quantity) {
            this.revenue = revenue;
            this.quantity = quantity;
        }

        @Override
        public int compareTo(ItemSalesMetrics other) {
            return this.revenue.compareTo(other.revenue);
        }
    }
}
