package com.konsol.core.service.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the BankBalance entity.
 */
public class BankBalanceDTO {

    private Double totalBalance;
    private Double invoiceProfits;
    private Double moneyProfits;
    private Double totalProfits;
    private Double totalSales;
    private Double totalPurchases;
    private Double moneyInFromMoney;
    private Double moneyOutFromMoney;
    private Double moneyInFromInvoices;
    private Double moneyOutFromInvoices;
    private Double totalMoneyIn;
    private Double totalMoneyOut;

    @Override
    public String toString() {
        return (
            "BankBalanceDTO{" +
            "totalBalance=" +
            totalBalance +
            ", invoiceProfits=" +
            invoiceProfits +
            ", moneyProfits=" +
            moneyProfits +
            ", totalProfits=" +
            totalProfits +
            ", totalSales=" +
            totalSales +
            ", totalPurchases=" +
            totalPurchases +
            ", moneyInFromMoney=" +
            moneyInFromMoney +
            ", moneyOutFromMoney=" +
            moneyOutFromMoney +
            ", moneyInFromInvoices=" +
            moneyInFromInvoices +
            ", moneyOutFromInvoices=" +
            moneyOutFromInvoices +
            ", totalMoneyIn=" +
            totalMoneyIn +
            ", totalMoneyOut=" +
            totalMoneyOut +
            '}'
        );
    }

    public Double getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(Double totalBalance) {
        this.totalBalance = totalBalance;
    }

    public Double getInvoiceProfits() {
        return invoiceProfits;
    }

    public void setInvoiceProfits(Double invoiceProfits) {
        this.invoiceProfits = invoiceProfits;
    }

    public Double getMoneyProfits() {
        return moneyProfits;
    }

    public void setMoneyProfits(Double moneyProfits) {
        this.moneyProfits = moneyProfits;
    }

    public Double getTotalProfits() {
        return totalProfits;
    }

    public void setTotalProfits(Double totalProfits) {
        this.totalProfits = totalProfits;
    }

    public Double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Double totalSales) {
        this.totalSales = totalSales;
    }

    public Double getTotalPurchases() {
        return totalPurchases;
    }

    public void setTotalPurchases(Double totalPurchases) {
        this.totalPurchases = totalPurchases;
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

    public Double getMoneyInFromInvoices() {
        return moneyInFromInvoices;
    }

    public void setMoneyInFromInvoices(Double moneyInFromInvoices) {
        this.moneyInFromInvoices = moneyInFromInvoices;
    }

    public Double getMoneyOutFromInvoices() {
        return moneyOutFromInvoices;
    }

    public void setMoneyOutFromInvoices(Double moneyOutFromInvoices) {
        this.moneyOutFromInvoices = moneyOutFromInvoices;
    }

    public Double getTotalMoneyIn() {
        return totalMoneyIn;
    }

    public void setTotalMoneyIn(Double totalMoneyIn) {
        this.totalMoneyIn = totalMoneyIn;
    }

    public Double getTotalMoneyOut() {
        return totalMoneyOut;
    }

    public void setTotalMoneyOut(Double totalMoneyOut) {
        this.totalMoneyOut = totalMoneyOut;
    }
}
