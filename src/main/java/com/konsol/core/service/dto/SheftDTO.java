package com.konsol.core.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.konsol.core.domain.Sheft} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SheftDTO implements Serializable {

    @NotNull
    private String id;

    @NotNull
    private LocalDate startTime;

    @NotNull
    private LocalDate endTime;

    @NotNull
    private Boolean active;

    @NotNull
    private String assignedEmployee;

    @NotNull
    private Duration duration;

    @NotNull
    private BigDecimal totalprice;

    @NotNull
    private BigDecimal totalCost;

    @NotNull
    private BigDecimal netPrice;

    @NotNull
    private BigDecimal netCost;

    @NotNull
    private BigDecimal netUserPrice;

    @NotNull
    private BigDecimal totalItemsOut;

    @NotNull
    private BigDecimal discount;

    @NotNull
    private BigDecimal invoicesAdditions;

    @NotNull
    private BigDecimal additions;

    @NotNull
    private BigDecimal additionsNotes;

    @NotNull
    private BigDecimal invoicesExpenses;

    @NotNull
    private BigDecimal sheftExpenses;

    @NotNull
    private BigDecimal totalinvoices;

    @NotNull
    private BigDecimal totaldeletedItems;

    @NotNull
    private BigDecimal totaldeletedItemsPrice;

    private String notes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getAssignedEmployee() {
        return assignedEmployee;
    }

    public void setAssignedEmployee(String assignedEmployee) {
        this.assignedEmployee = assignedEmployee;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public BigDecimal getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(BigDecimal totalprice) {
        this.totalprice = totalprice;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(BigDecimal netPrice) {
        this.netPrice = netPrice;
    }

    public BigDecimal getNetCost() {
        return netCost;
    }

    public void setNetCost(BigDecimal netCost) {
        this.netCost = netCost;
    }

    public BigDecimal getNetUserPrice() {
        return netUserPrice;
    }

    public void setNetUserPrice(BigDecimal netUserPrice) {
        this.netUserPrice = netUserPrice;
    }

    public BigDecimal getTotalItemsOut() {
        return totalItemsOut;
    }

    public void setTotalItemsOut(BigDecimal totalItemsOut) {
        this.totalItemsOut = totalItemsOut;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getInvoicesAdditions() {
        return invoicesAdditions;
    }

    public void setInvoicesAdditions(BigDecimal invoicesAdditions) {
        this.invoicesAdditions = invoicesAdditions;
    }

    public BigDecimal getAdditions() {
        return additions;
    }

    public void setAdditions(BigDecimal additions) {
        this.additions = additions;
    }

    public BigDecimal getAdditionsNotes() {
        return additionsNotes;
    }

    public void setAdditionsNotes(BigDecimal additionsNotes) {
        this.additionsNotes = additionsNotes;
    }

    public BigDecimal getInvoicesExpenses() {
        return invoicesExpenses;
    }

    public void setInvoicesExpenses(BigDecimal invoicesExpenses) {
        this.invoicesExpenses = invoicesExpenses;
    }

    public BigDecimal getSheftExpenses() {
        return sheftExpenses;
    }

    public void setSheftExpenses(BigDecimal sheftExpenses) {
        this.sheftExpenses = sheftExpenses;
    }

    public BigDecimal getTotalinvoices() {
        return totalinvoices;
    }

    public void setTotalinvoices(BigDecimal totalinvoices) {
        this.totalinvoices = totalinvoices;
    }

    public BigDecimal getTotaldeletedItems() {
        return totaldeletedItems;
    }

    public void setTotaldeletedItems(BigDecimal totaldeletedItems) {
        this.totaldeletedItems = totaldeletedItems;
    }

    public BigDecimal getTotaldeletedItemsPrice() {
        return totaldeletedItemsPrice;
    }

    public void setTotaldeletedItemsPrice(BigDecimal totaldeletedItemsPrice) {
        this.totaldeletedItemsPrice = totaldeletedItemsPrice;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SheftDTO)) {
            return false;
        }

        SheftDTO sheftDTO = (SheftDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sheftDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SheftDTO{" +
            "id='" + getId() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", active='" + getActive() + "'" +
            ", assignedEmployee='" + getAssignedEmployee() + "'" +
            ", duration='" + getDuration() + "'" +
            ", totalprice=" + getTotalprice() +
            ", totalCost=" + getTotalCost() +
            ", netPrice=" + getNetPrice() +
            ", netCost=" + getNetCost() +
            ", netUserPrice=" + getNetUserPrice() +
            ", totalItemsOut=" + getTotalItemsOut() +
            ", discount=" + getDiscount() +
            ", invoicesAdditions=" + getInvoicesAdditions() +
            ", additions=" + getAdditions() +
            ", additionsNotes=" + getAdditionsNotes() +
            ", invoicesExpenses=" + getInvoicesExpenses() +
            ", sheftExpenses=" + getSheftExpenses() +
            ", totalinvoices=" + getTotalinvoices() +
            ", totaldeletedItems=" + getTotaldeletedItems() +
            ", totaldeletedItemsPrice=" + getTotaldeletedItemsPrice() +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
