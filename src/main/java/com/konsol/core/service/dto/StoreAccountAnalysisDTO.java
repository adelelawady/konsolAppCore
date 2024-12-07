package com.konsol.core.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreAccountAnalysisDTO implements Serializable {

    private Map<String, BigDecimal> storePerformance = new HashMap<>();
    // Store ID to sales amount
    private Map<String, BigDecimal> accountPerformance = new HashMap<>();
    // Account ID to transaction volume
    private Map<String, BigDecimal> bankTransactions = new HashMap<>();
    // Bank ID to transaction volume
    private Map<String, BigDecimal> storeProfitMargins = new HashMap<>();
    // Store ID to profit margin
    private Map<String, Integer> storeTransactionCounts = new HashMap<>();
    // Store ID to number of transactions
    private Map<String, BigDecimal> storeRevenueGrowth = new HashMap<>();
    // Store ID to revenue growth rate
    private Map<String, BigDecimal> accountBalances = new HashMap<>();
    // Account ID to current balance
    private Map<String, BigDecimal> accountCreditLimits = new HashMap<>(); // Account ID to credit limit
    private List<TopPerformerDTO> topStores = new ArrayList<>();
    private List<TopPerformerDTO> topAccounts = new ArrayList<>();
    private List<StoreMetricsDTO> storeMetrics = new ArrayList<>();
    private List<AccountMetricsDTO> accountMetrics = new ArrayList<>();

    @Override
    public String toString() {
        return (
            "StoreAccountAnalysisDTO{" +
            "storePerformance=" +
            storePerformance +
            ", accountPerformance=" +
            accountPerformance +
            ", bankTransactions=" +
            bankTransactions +
            ", storeProfitMargins=" +
            storeProfitMargins +
            ", storeTransactionCounts=" +
            storeTransactionCounts +
            ", storeRevenueGrowth=" +
            storeRevenueGrowth +
            ", accountBalances=" +
            accountBalances +
            ", accountCreditLimits=" +
            accountCreditLimits +
            ", topStores=" +
            topStores +
            ", topAccounts=" +
            topAccounts +
            ", storeMetrics=" +
            storeMetrics +
            ", accountMetrics=" +
            accountMetrics +
            '}'
        );
    }

    // Inner class for detailed store metrics
    public static class StoreMetricsDTO implements Serializable {

        private String storeId;
        private String storeName;
        private BigDecimal totalRevenue;
        private BigDecimal totalCost;
        private BigDecimal profitMargin;
        private Integer transactionCount;
        private BigDecimal averageTransactionValue;
        private BigDecimal revenueGrowth;

        // Getters and Setters
        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public BigDecimal getTotalRevenue() {
            return totalRevenue;
        }

        public void setTotalRevenue(BigDecimal totalRevenue) {
            this.totalRevenue = totalRevenue;
        }

        public BigDecimal getTotalCost() {
            return totalCost;
        }

        public void setTotalCost(BigDecimal totalCost) {
            this.totalCost = totalCost;
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

        public BigDecimal getRevenueGrowth() {
            return revenueGrowth;
        }

        public void setRevenueGrowth(BigDecimal revenueGrowth) {
            this.revenueGrowth = revenueGrowth;
        }

        @Override
        public String toString() {
            return (
                "StoreMetricsDTO{" +
                "storeId='" +
                storeId +
                '\'' +
                ", storeName='" +
                storeName +
                '\'' +
                ", totalRevenue=" +
                totalRevenue +
                ", totalCost=" +
                totalCost +
                ", profitMargin=" +
                profitMargin +
                ", transactionCount=" +
                transactionCount +
                ", averageTransactionValue=" +
                averageTransactionValue +
                ", revenueGrowth=" +
                revenueGrowth +
                '}'
            );
        }
    }

    // Inner class for detailed account metrics
    public static class AccountMetricsDTO implements Serializable {

        private String accountId;
        private String accountName;
        private BigDecimal totalTransactionVolume;
        private Integer transactionCount;
        private BigDecimal averageTransactionValue;
        private BigDecimal currentBalance;
        private BigDecimal creditLimit;
        private BigDecimal creditUtilization;

        // Getters and Setters
        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public BigDecimal getTotalTransactionVolume() {
            return totalTransactionVolume;
        }

        public void setTotalTransactionVolume(BigDecimal totalTransactionVolume) {
            this.totalTransactionVolume = totalTransactionVolume;
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

        public BigDecimal getCurrentBalance() {
            return currentBalance;
        }

        public void setCurrentBalance(BigDecimal currentBalance) {
            this.currentBalance = currentBalance;
        }

        public BigDecimal getCreditLimit() {
            return creditLimit;
        }

        public void setCreditLimit(BigDecimal creditLimit) {
            this.creditLimit = creditLimit;
        }

        public BigDecimal getCreditUtilization() {
            return creditUtilization;
        }

        public void setCreditUtilization(BigDecimal creditUtilization) {
            this.creditUtilization = creditUtilization;
        }

        @Override
        public String toString() {
            return (
                "AccountMetricsDTO{" +
                "accountId='" +
                accountId +
                '\'' +
                ", accountName='" +
                accountName +
                '\'' +
                ", totalTransactionVolume=" +
                totalTransactionVolume +
                ", transactionCount=" +
                transactionCount +
                ", averageTransactionValue=" +
                averageTransactionValue +
                ", currentBalance=" +
                currentBalance +
                ", creditLimit=" +
                creditLimit +
                ", creditUtilization=" +
                creditUtilization +
                '}'
            );
        }
    }

    // Getters and Setters for main class
    public Map<String, BigDecimal> getStorePerformance() {
        return storePerformance;
    }

    public void setStorePerformance(Map<String, BigDecimal> storePerformance) {
        this.storePerformance = storePerformance;
    }

    public Map<String, BigDecimal> getAccountPerformance() {
        return accountPerformance;
    }

    public void setAccountPerformance(Map<String, BigDecimal> accountPerformance) {
        this.accountPerformance = accountPerformance;
    }

    public Map<String, BigDecimal> getBankTransactions() {
        return bankTransactions;
    }

    public void setBankTransactions(Map<String, BigDecimal> bankTransactions) {
        this.bankTransactions = bankTransactions;
    }

    public Map<String, BigDecimal> getStoreProfitMargins() {
        return storeProfitMargins;
    }

    public void setStoreProfitMargins(Map<String, BigDecimal> storeProfitMargins) {
        this.storeProfitMargins = storeProfitMargins;
    }

    public Map<String, Integer> getStoreTransactionCounts() {
        return storeTransactionCounts;
    }

    public void setStoreTransactionCounts(Map<String, Integer> storeTransactionCounts) {
        this.storeTransactionCounts = storeTransactionCounts;
    }

    public Map<String, BigDecimal> getStoreRevenueGrowth() {
        return storeRevenueGrowth;
    }

    public void setStoreRevenueGrowth(Map<String, BigDecimal> storeRevenueGrowth) {
        this.storeRevenueGrowth = storeRevenueGrowth;
    }

    public Map<String, BigDecimal> getAccountBalances() {
        return accountBalances;
    }

    public void setAccountBalances(Map<String, BigDecimal> accountBalances) {
        this.accountBalances = accountBalances;
    }

    public Map<String, BigDecimal> getAccountCreditLimits() {
        return accountCreditLimits;
    }

    public void setAccountCreditLimits(Map<String, BigDecimal> accountCreditLimits) {
        this.accountCreditLimits = accountCreditLimits;
    }

    public List<TopPerformerDTO> getTopStores() {
        return topStores;
    }

    public void setTopStores(List<TopPerformerDTO> topStores) {
        this.topStores = topStores;
    }

    public List<TopPerformerDTO> getTopAccounts() {
        return topAccounts;
    }

    public void setTopAccounts(List<TopPerformerDTO> topAccounts) {
        this.topAccounts = topAccounts;
    }

    public List<StoreMetricsDTO> getStoreMetrics() {
        return storeMetrics;
    }

    public void setStoreMetrics(List<StoreMetricsDTO> storeMetrics) {
        this.storeMetrics = storeMetrics;
    }

    public List<AccountMetricsDTO> getAccountMetrics() {
        return accountMetrics;
    }

    public void setAccountMetrics(List<AccountMetricsDTO> accountMetrics) {
        this.accountMetrics = accountMetrics;
    }
}
