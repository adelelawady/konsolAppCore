package com.konsol.core.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

public class CashFlowMetricsDTO implements Serializable {

    private BigDecimal totalMoneyIn;
    private BigDecimal totalMoneyOut;
    private Map<String, BigDecimal> bankBalances; // Map of bank ID to balance
    private BigDecimal currentCashPosition;

    // Getters and Setters
    public BigDecimal getTotalMoneyIn() {
        return totalMoneyIn;
    }

    public void setTotalMoneyIn(BigDecimal totalMoneyIn) {
        this.totalMoneyIn = totalMoneyIn;
    }

    public BigDecimal getTotalMoneyOut() {
        return totalMoneyOut;
    }

    public void setTotalMoneyOut(BigDecimal totalMoneyOut) {
        this.totalMoneyOut = totalMoneyOut;
    }

    public Map<String, BigDecimal> getBankBalances() {
        return bankBalances;
    }

    public void setBankBalances(Map<String, BigDecimal> bankBalances) {
        this.bankBalances = bankBalances;
    }

    public BigDecimal getCurrentCashPosition() {
        return currentCashPosition;
    }

    public void setCurrentCashPosition(BigDecimal currentCashPosition) {
        this.currentCashPosition = currentCashPosition;
    }

    @Override
    public String toString() {
        return (
            "CashFlowMetricsDTO{" +
            "totalMoneyIn=" +
            totalMoneyIn +
            ", totalMoneyOut=" +
            totalMoneyOut +
            ", bankBalances=" +
            bankBalances +
            ", currentCashPosition=" +
            currentCashPosition +
            '}'
        );
    }
}
