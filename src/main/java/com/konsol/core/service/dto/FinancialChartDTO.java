package com.konsol.core.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Transfer Object for financial dashboard charts and visualizations.
 * Supports various chart types including line, bar, pie, radar, and area charts.
 */
public class FinancialChartDTO implements Serializable {

    private String chartId;
    private String title;
    private String subtitle;
    private String chartType;
    private String xAxisLabel;
    private String yAxisLabel;
    private List<String> labels = new ArrayList<>();
    private List<SeriesData> series = new ArrayList<>();
    private Map<String, Object> options = new LinkedHashMap<>();
    private Map<String, String> metadata = new LinkedHashMap<>();
    private List<BigDecimal> data = new ArrayList<>();

    public List<BigDecimal> getData() {
        return data;
    }

    public void setData(List<BigDecimal> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return (
            "FinancialChartDTO{" +
            "chartId='" +
            chartId +
            '\'' +
            ", title='" +
            title +
            '\'' +
            ", subtitle='" +
            subtitle +
            '\'' +
            ", chartType='" +
            chartType +
            '\'' +
            ", xAxisLabel='" +
            xAxisLabel +
            '\'' +
            ", yAxisLabel='" +
            yAxisLabel +
            '\'' +
            ", labels=" +
            labels +
            ", series=" +
            series +
            ", options=" +
            options +
            ", metadata=" +
            metadata +
            ", data=" +
            data +
            '}'
        );
    }

    /**
     * Represents a data series in the chart
     */
    public static class SeriesData implements Serializable {

        private String name;
        private String type;
        private List<BigDecimal> data = new ArrayList<>();
        private Map<String, Object> style = new LinkedHashMap<>();
        private boolean visible = true;
        private Integer yAxisIndex;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<BigDecimal> getData() {
            return data;
        }

        public void setData(List<BigDecimal> data) {
            this.data = data;
        }

        public Map<String, Object> getStyle() {
            return style;
        }

        public void setStyle(Map<String, Object> style) {
            this.style = style;
        }

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        public Integer getYAxisIndex() {
            return yAxisIndex;
        }

        public void setYAxisIndex(Integer yAxisIndex) {
            this.yAxisIndex = yAxisIndex;
        }

        @Override
        public String toString() {
            return (
                "SeriesData{" +
                "name='" +
                name +
                '\'' +
                ", type='" +
                type +
                '\'' +
                ", data=" +
                data +
                ", style=" +
                style +
                ", visible=" +
                visible +
                ", yAxisIndex=" +
                yAxisIndex +
                '}'
            );
        }
    }

    // Getters and Setters
    public String getChartId() {
        return chartId;
    }

    public void setChartId(String chartId) {
        this.chartId = chartId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public String getXAxisLabel() {
        return xAxisLabel;
    }

    public void setXAxisLabel(String xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
    }

    public String getYAxisLabel() {
        return yAxisLabel;
    }

    public void setYAxisLabel(String yAxisLabel) {
        this.yAxisLabel = yAxisLabel;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<SeriesData> getSeries() {
        return series;
    }

    public void setSeries(List<SeriesData> series) {
        this.series = series;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public void setOptions(Map<String, Object> options) {
        this.options = options;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    // Helper methods
    public void addSeries(String name, List<BigDecimal> data) {
        SeriesData seriesData = new SeriesData();
        seriesData.setName(name);
        seriesData.setData(data);
        seriesData.setType(this.chartType);
        this.series.add(seriesData);
    }

    public void addSeries(String name, String type, List<BigDecimal> data) {
        SeriesData seriesData = new SeriesData();
        seriesData.setName(name);
        seriesData.setType(type);
        seriesData.setData(data);
        this.series.add(seriesData);
    }

    public void addSeries(String name, String type, List<BigDecimal> data, Map<String, Object> style) {
        SeriesData seriesData = new SeriesData();
        seriesData.setName(name);
        seriesData.setType(type);
        seriesData.setData(data);
        seriesData.setStyle(style);
        this.series.add(seriesData);
    }

    public void setChartOption(String key, Object value) {
        this.options.put(key, value);
    }

    public void setMetadataValue(String key, String value) {
        this.metadata.put(key, value);
    }

    /**
     * Creates a new instance of FinancialChartDTO with basic configuration
     */
    public static FinancialChartDTO create(String chartId, String title, String chartType) {
        FinancialChartDTO chart = new FinancialChartDTO();
        chart.setChartId(chartId);
        chart.setTitle(title);
        chart.setChartType(chartType);
        return chart;
    }

    /**
     * Creates a new instance of FinancialChartDTO with detailed configuration
     */
    public static FinancialChartDTO create(
        String chartId,
        String title,
        String subtitle,
        String chartType,
        String xAxisLabel,
        String yAxisLabel
    ) {
        FinancialChartDTO chart = new FinancialChartDTO();
        chart.setChartId(chartId);
        chart.setTitle(title);
        chart.setSubtitle(subtitle);
        chart.setChartType(chartType);
        chart.setXAxisLabel(xAxisLabel);
        chart.setYAxisLabel(yAxisLabel);
        return chart;
    }
}
