package com.konsol.core.service.dto;

import lombok.Getter;
import lombok.Setter;

/**
 *
 *
 * التعديلات على BankBalanceDTO
 * أضف الحقول الجديدة:
 *
 * java
 * Copy code
 * private Double grossRevenue;   // الإيرادات الإجمالية قبل الخصم
 * private Double netRevenue;     // الإيرادات الصافية بعد الخصم
 * private Double totalCost;      // التكلفة الإجمالية للمشتريات
 * private Double totalDiscounts; // إجمالي الخصومات المقدمة
 * private Double totalAdditions; // إجمالي الإضافات
 *
 * // Getter and Setter methods
 * شرح القيم المحسوبة
 * grossRevenue: الإيرادات الإجمالية قبل الخصم.
 * netRevenue: الإيرادات الصافية بعد الخصم.
 * totalCost: التكلفة الإجمالية النهائية للمشتريات.
 * totalSalesProfits: صافي الربح من المبيعات.
 * totalDiscounts: إجمالي الخصومات المطبقة على المبيعات.
 * totalAdditions: إجمالي الإضافات مثل الضرائب أو الرسوم.
 * totalMoneyIn و totalMoneyOut: الأموال الداخلة والخارجة من معاملات الأموال.
 * totalBalance: الرصيد الإجمالي الحالي (الأموال الداخلة - الأموال الخارجة).
 *
 *
 * A DTO for the BankBalance entity.
 */
public class BankBalanceDTO {

    private Double totalSalesProfits;
    private Double grossRevenue; // الإيرادات الإجمالية قبل الخصم
    private Double netRevenue; // الإيرادات الصافية بعد الخصم
    private Double totalCost; // التكلفة الإجمالية للمشتريات
    private Double totalDiscounts; // إجمالي الخصومات المقدمة
    private Double totalAdditions; // إجمالي الإضافات
    private Double totalBalance;
    private Double moneyInFromMoney;
    private Double moneyOutFromMoney;

    public Double getGrossRevenue() {
        return grossRevenue;
    }

    public void setGrossRevenue(Double grossRevenue) {
        this.grossRevenue = grossRevenue;
    }

    public Double getNetRevenue() {
        return netRevenue;
    }

    public void setNetRevenue(Double netRevenue) {
        this.netRevenue = netRevenue;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Double getTotalDiscounts() {
        return totalDiscounts;
    }

    public void setTotalDiscounts(Double totalDiscounts) {
        this.totalDiscounts = totalDiscounts;
    }

    public Double getTotalAdditions() {
        return totalAdditions;
    }

    public void setTotalAdditions(Double totalAdditions) {
        this.totalAdditions = totalAdditions;
    }

    public Double getTotalSalesProfits() {
        return totalSalesProfits;
    }

    public void setTotalSalesProfits(Double totalSalesProfits) {
        this.totalSalesProfits = totalSalesProfits;
    }

    public Double getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(Double totalBalance) {
        this.totalBalance = totalBalance;
    }

    public Double getMoneyInFromMoney() {
        return moneyInFromMoney;
    }

    public void setMoneyInFromMoney(Double moneyInFromMoney) {
        this.moneyInFromMoney = moneyInFromMoney;
    }

    public Double getMoneyOutFromMoney() {
        return moneyOutFromMoney;
    }

    public void setMoneyOutFromMoney(Double moneyOutFromMoney) {
        this.moneyOutFromMoney = moneyOutFromMoney;
    }

    @Override
    public String toString() {
        return (
            "BankBalanceDTO{" +
            "totalSalesProfits=" +
            totalSalesProfits +
            ", grossRevenue=" +
            grossRevenue +
            ", netRevenue=" +
            netRevenue +
            ", totalCost=" +
            totalCost +
            ", totalDiscounts=" +
            totalDiscounts +
            ", totalAdditions=" +
            totalAdditions +
            ", totalBalance=" +
            totalBalance +
            ", moneyInFromMoney=" +
            moneyInFromMoney +
            ", moneyOutFromMoney=" +
            moneyOutFromMoney +
            '}'
        );
    }
}
