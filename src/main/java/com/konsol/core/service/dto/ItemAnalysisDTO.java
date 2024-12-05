package com.konsol.core.service.dto;

public class ItemAnalysisDTO {

    private double totalSales; // إجمالي المبيعات الإجمالية
    private double netSales; // إجمالي المبيعات الصافية
    private double totalCost; // إجمالي التكلفة الإجمالية
    private double netCost; // إجمالي التكلفة الصافية
    private double totalProfit; // إجمالي الأرباح
    private double totalDiscount; // إجمالي الخصومات
    private double totalQtyOut; // إجمالي الكمية المباعة
    private double totalQtyIn; // إجمالي الكمية المشتراة

    // Getters and Setters

    @Override
    public String toString() {
        return (
            "ItemAnalysisDTO{" +
            "totalSales=" +
            totalSales +
            ", netSales=" +
            netSales +
            ", totalCost=" +
            totalCost +
            ", netCost=" +
            netCost +
            ", totalProfit=" +
            totalProfit +
            ", totalDiscount=" +
            totalDiscount +
            ", totalQtyOut=" +
            totalQtyOut +
            ", totalQtyIn=" +
            totalQtyIn +
            '}'
        );
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    public double getNetSales() {
        return netSales;
    }

    public void setNetSales(double netSales) {
        this.netSales = netSales;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getNetCost() {
        return netCost;
    }

    public void setNetCost(double netCost) {
        this.netCost = netCost;
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public double getTotalQtyOut() {
        return totalQtyOut;
    }

    public void setTotalQtyOut(double totalQtyOut) {
        this.totalQtyOut = totalQtyOut;
    }

    public double getTotalQtyIn() {
        return totalQtyIn;
    }

    public void setTotalQtyIn(double totalQtyIn) {
        this.totalQtyIn = totalQtyIn;
    }
}
