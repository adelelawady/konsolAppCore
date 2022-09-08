package com.konsol.core.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.konsol.core.domain.InvoiceItem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceItemDTO implements Serializable {

    private String pk;

    private String id;

    private String unit;

    @DecimalMin(value = "0")
    private BigDecimal unitPieces;

    @DecimalMin(value = "0")
    private BigDecimal userQty;

    @DecimalMin(value = "0")
    private BigDecimal unitQtyIn;

    @DecimalMin(value = "0")
    private BigDecimal unitQtyOut;

    @DecimalMin(value = "0")
    private BigDecimal unitCost;

    @DecimalMin(value = "0")
    private BigDecimal unitPrice;

    @Min(value = 0)
    @Max(value = 100)
    private Integer discountPer;

    @DecimalMin(value = "0")
    private BigDecimal discount;

    @DecimalMin(value = "0")
    private BigDecimal totalCost;

    @DecimalMin(value = "0")
    private BigDecimal totalPrice;

    @DecimalMin(value = "0")
    private BigDecimal qtyIn;

    @DecimalMin(value = "0")
    private BigDecimal qtyOut;

    @DecimalMin(value = "0")
    private BigDecimal cost;

    @DecimalMin(value = "0")
    private BigDecimal price;

    @DecimalMin(value = "0")
    private BigDecimal netCost;

    @DecimalMin(value = "0")
    private BigDecimal netPrice;

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getUnitPieces() {
        return unitPieces;
    }

    public void setUnitPieces(BigDecimal unitPieces) {
        this.unitPieces = unitPieces;
    }

    public BigDecimal getUserQty() {
        return userQty;
    }

    public void setUserQty(BigDecimal userQty) {
        this.userQty = userQty;
    }

    public BigDecimal getUnitQtyIn() {
        return unitQtyIn;
    }

    public void setUnitQtyIn(BigDecimal unitQtyIn) {
        this.unitQtyIn = unitQtyIn;
    }

    public BigDecimal getUnitQtyOut() {
        return unitQtyOut;
    }

    public void setUnitQtyOut(BigDecimal unitQtyOut) {
        this.unitQtyOut = unitQtyOut;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(Integer discountPer) {
        this.discountPer = discountPer;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getQtyIn() {
        return qtyIn;
    }

    public void setQtyIn(BigDecimal qtyIn) {
        this.qtyIn = qtyIn;
    }

    public BigDecimal getQtyOut() {
        return qtyOut;
    }

    public void setQtyOut(BigDecimal qtyOut) {
        this.qtyOut = qtyOut;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getNetCost() {
        return netCost;
    }

    public void setNetCost(BigDecimal netCost) {
        this.netCost = netCost;
    }

    public BigDecimal getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(BigDecimal netPrice) {
        this.netPrice = netPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceItemDTO)) {
            return false;
        }

        InvoiceItemDTO invoiceItemDTO = (InvoiceItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, invoiceItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceItemDTO{" +
            "pk='" + getPk() + "'" +
            ", id='" + getId() + "'" +
            ", unit='" + getUnit() + "'" +
            ", unitPieces=" + getUnitPieces() +
            ", userQty=" + getUserQty() +
            ", unitQtyIn=" + getUnitQtyIn() +
            ", unitQtyOut=" + getUnitQtyOut() +
            ", unitCost=" + getUnitCost() +
            ", unitPrice=" + getUnitPrice() +
            ", discountPer=" + getDiscountPer() +
            ", discount=" + getDiscount() +
            ", totalCost=" + getTotalCost() +
            ", totalPrice=" + getTotalPrice() +
            ", qtyIn=" + getQtyIn() +
            ", qtyOut=" + getQtyOut() +
            ", cost=" + getCost() +
            ", price=" + getPrice() +
            ", netCost=" + getNetCost() +
            ", netPrice=" + getNetPrice() +
            "}";
    }
}
