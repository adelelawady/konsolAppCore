package com.konsol.core.service.dto;

public class ChartDataDTO {

    private String date; // التاريخ (يوم/شهر/سنة)
    private double totalSales; // إجمالي المبيعات
    private double totalQty; // إجمالي الكمية المباعة
    private double avgPrice; // متوسط السعر

    // Getters and Setters

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    public double getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(double totalQty) {
        this.totalQty = totalQty;
    }

    public double getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(double avgPrice) {
        this.avgPrice = avgPrice;
    }

    @Override
    public String toString() {
        return (
            "ChartDataDTO{" +
            "date='" +
            date +
            '\'' +
            ", totalSales=" +
            totalSales +
            ", totalQty=" +
            totalQty +
            ", avgPrice=" +
            avgPrice +
            '}'
        );
    }
}
