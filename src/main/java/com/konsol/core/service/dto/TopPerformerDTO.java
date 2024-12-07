package com.konsol.core.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for representing top performers in different categories
 * (stores, items, customers, etc.)
 */
public class TopPerformerDTO implements Serializable {

    private String id;
    private String name;
    private String category;
    private BigDecimal totalRevenue;
    private BigDecimal totalProfit;
    private BigDecimal profitMargin;
    private Integer transactionCount;
    private BigDecimal averageTransactionValue;
    private List<TrendPoint> trend = new ArrayList<>();
    private List<MetricValue> metrics = new ArrayList<>();

    @Override
    public String toString() {
        return (
            "TopPerformerDTO{" +
            "id='" +
            id +
            '\'' +
            ", name='" +
            name +
            '\'' +
            ", category='" +
            category +
            '\'' +
            ", totalRevenue=" +
            totalRevenue +
            ", totalProfit=" +
            totalProfit +
            ", profitMargin=" +
            profitMargin +
            ", transactionCount=" +
            transactionCount +
            ", averageTransactionValue=" +
            averageTransactionValue +
            ", trend=" +
            trend +
            ", metrics=" +
            metrics +
            '}'
        );
    }

    // Nested class for trend data points
    public static class TrendPoint implements Serializable {

        private String period;
        private BigDecimal value;
        private BigDecimal growth;

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public BigDecimal getValue() {
            return value;
        }

        public void setValue(BigDecimal value) {
            this.value = value;
        }

        public BigDecimal getGrowth() {
            return growth;
        }

        public void setGrowth(BigDecimal growth) {
            this.growth = growth;
        }

        @Override
        public String toString() {
            return "TrendPoint{" + "period='" + period + '\'' + ", value=" + value + ", growth=" + growth + '}';
        }
    }

    // Nested class for additional metrics
    public static class MetricValue implements Serializable {

        private String name;
        private BigDecimal value;
        private String unit;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getValue() {
            return value;
        }

        public void setValue(BigDecimal value) {
            this.value = value;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        @Override
        public String toString() {
            return "MetricValue{" + "name='" + name + '\'' + ", value=" + value + ", unit='" + unit + '\'' + '}';
        }
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    public BigDecimal getProfitMargin() {
        return profitMargin;
    }

    public void setProfitMargin(BigDecimal profitMargin) {
        this.profitMargin = profitMargin;
    }

    public Integer getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Integer transactionCount) {
        this.transactionCount = transactionCount;
    }

    public BigDecimal getAverageTransactionValue() {
        return averageTransactionValue;
    }

    public void setAverageTransactionValue(BigDecimal averageTransactionValue) {
        this.averageTransactionValue = averageTransactionValue;
    }

    public List<TrendPoint> getTrend() {
        return trend;
    }

    public void setTrend(List<TrendPoint> trend) {
        this.trend = trend;
    }

    public List<MetricValue> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<MetricValue> metrics) {
        this.metrics = metrics;
    }

    // Helper methods
    public void addTrendPoint(String period, BigDecimal value, BigDecimal growth) {
        TrendPoint point = new TrendPoint();
        point.setPeriod(period);
        point.setValue(value);
        point.setGrowth(growth);
        this.trend.add(point);
    }

    public void addMetric(String name, BigDecimal value, String unit) {
        MetricValue metric = new MetricValue();
        metric.setName(name);
        metric.setValue(value);
        metric.setUnit(unit);
        this.metrics.add(metric);
    }
}
