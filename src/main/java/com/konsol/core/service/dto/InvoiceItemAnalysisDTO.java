package com.konsol.core.service.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class InvoiceItemAnalysisDTO {

    private BigDecimal totalItemsSold;
    private BigDecimal totalItemRevenue;
    private BigDecimal averageItemPrice;
    private BigDecimal topSellingItemRevenue;
    private String topSellingItemName;
    private Map<String, BigDecimal> itemCategoryDistribution;
    private List<ItemProfitabilityDTO> itemProfitMargins;
    private List<ItemSalesDTO> topSellingItems;
    private List<FinancialChartDTO> itemSalesCharts;

    public List<FinancialChartDTO> getItemSalesCharts() {
        return itemSalesCharts;
    }

    public void setItemSalesCharts(List<FinancialChartDTO> itemSalesCharts) {
        this.itemSalesCharts = itemSalesCharts;
    }

    @Override
    public String toString() {
        return (
            "InvoiceItemAnalysisDTO{" +
            "totalItemsSold=" +
            totalItemsSold +
            ", totalItemRevenue=" +
            totalItemRevenue +
            ", averageItemPrice=" +
            averageItemPrice +
            ", topSellingItemRevenue=" +
            topSellingItemRevenue +
            ", topSellingItemName='" +
            topSellingItemName +
            '\'' +
            ", itemCategoryDistribution=" +
            itemCategoryDistribution +
            ", itemProfitMargins=" +
            itemProfitMargins +
            ", topSellingItems=" +
            topSellingItems +
            ", itemSalesCharts=" +
            itemSalesCharts +
            '}'
        );
    }

    // Inner class for item sales details
    public static class ItemSalesDTO {

        private String itemName;
        private String itemCode;
        private BigDecimal quantity;
        private BigDecimal revenue;
        private BigDecimal profit;
        private BigDecimal profitMargin;

        // Getters and setters
        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getItemCode() {
            return itemCode;
        }

        public void setItemCode(String itemCode) {
            this.itemCode = itemCode;
        }

        public BigDecimal getQuantity() {
            return quantity;
        }

        public void setQuantity(BigDecimal quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getRevenue() {
            return revenue;
        }

        public void setRevenue(BigDecimal revenue) {
            this.revenue = revenue;
        }

        public BigDecimal getProfit() {
            return profit;
        }

        public void setProfit(BigDecimal profit) {
            this.profit = profit;
        }

        public BigDecimal getProfitMargin() {
            return profitMargin;
        }

        public void setProfitMargin(BigDecimal profitMargin) {
            this.profitMargin = profitMargin;
        }

        @Override
        public String toString() {
            return (
                "ItemSalesDTO{" +
                "itemName='" +
                itemName +
                '\'' +
                ", itemCode='" +
                itemCode +
                '\'' +
                ", quantity=" +
                quantity +
                ", revenue=" +
                revenue +
                ", profit=" +
                profit +
                ", profitMargin=" +
                profitMargin +
                '}'
            );
        }
    }

    // Getters and setters
    public BigDecimal getTotalItemsSold() {
        return totalItemsSold;
    }

    public void setTotalItemsSold(BigDecimal totalItemsSold) {
        this.totalItemsSold = totalItemsSold;
    }

    public BigDecimal getTotalItemRevenue() {
        return totalItemRevenue;
    }

    public void setTotalItemRevenue(BigDecimal totalItemRevenue) {
        this.totalItemRevenue = totalItemRevenue;
    }

    public BigDecimal getAverageItemPrice() {
        return averageItemPrice;
    }

    public void setAverageItemPrice(BigDecimal averageItemPrice) {
        this.averageItemPrice = averageItemPrice;
    }

    public BigDecimal getTopSellingItemRevenue() {
        return topSellingItemRevenue;
    }

    public void setTopSellingItemRevenue(BigDecimal topSellingItemRevenue) {
        this.topSellingItemRevenue = topSellingItemRevenue;
    }

    public String getTopSellingItemName() {
        return topSellingItemName;
    }

    public void setTopSellingItemName(String topSellingItemName) {
        this.topSellingItemName = topSellingItemName;
    }

    public Map<String, BigDecimal> getItemCategoryDistribution() {
        return itemCategoryDistribution;
    }

    public void setItemCategoryDistribution(Map<String, BigDecimal> itemCategoryDistribution) {
        this.itemCategoryDistribution = itemCategoryDistribution;
    }

    public List<ItemProfitabilityDTO> getItemProfitMargins() {
        return itemProfitMargins;
    }

    public void setItemProfitMargins(List<ItemProfitabilityDTO> list) {
        this.itemProfitMargins = list;
    }

    public List<ItemSalesDTO> getTopSellingItems() {
        return topSellingItems;
    }

    public void setTopSellingItems(List<ItemSalesDTO> topSellingItems) {
        this.topSellingItems = topSellingItems;
    }
}
