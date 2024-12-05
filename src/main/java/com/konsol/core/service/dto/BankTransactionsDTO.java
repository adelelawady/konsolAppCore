package com.konsol.core.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Data Transfer Object for Bank Movements
 */
public class BankTransactionsDTO implements Serializable {

    private String id;
    private String pk;
    private String bankId;
    private String sourceType; // "INVOICE" or "MONEY"
    private String sourceKind;
    private String sourceId;
    private String sourcePk;
    private BigDecimal moneyIn;
    private BigDecimal moneyOut;
    private String details;
    private Instant createdDate;
    private String accountId;
    private String accountName;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourcePk() {
        return sourcePk;
    }

    public void setSourcePk(String sourcePk) {
        this.sourcePk = sourcePk;
    }

    public BigDecimal getMoneyIn() {
        return moneyIn;
    }

    public void setMoneyIn(BigDecimal moneyIn) {
        this.moneyIn = moneyIn;
    }

    public BigDecimal getMoneyOut() {
        return moneyOut;
    }

    public void setMoneyOut(BigDecimal moneyOut) {
        this.moneyOut = moneyOut;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

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

    public String getSourceKind() {
        return sourceKind;
    }

    public void setSourceKind(String sourceKind) {
        this.sourceKind = sourceKind;
    }

    @Override
    public String toString() {
        return (
            "BankMovementDTO{" +
            "id='" +
            id +
            '\'' +
            ", pk='" +
            pk +
            '\'' +
            ", bankId='" +
            bankId +
            '\'' +
            ", sourceType='" +
            sourceType +
            '\'' +
            ", sourceKind='" +
            sourceKind +
            '\'' +
            ", sourceId='" +
            sourceId +
            '\'' +
            ", sourcePk='" +
            sourcePk +
            '\'' +
            ", moneyIn=" +
            moneyIn +
            ", moneyOut=" +
            moneyOut +
            ", details='" +
            details +
            '\'' +
            ", createdDate=" +
            createdDate +
            ", accountId='" +
            accountId +
            '\'' +
            ", accountName='" +
            accountName +
            '\'' +
            '}'
        );
    }
}
