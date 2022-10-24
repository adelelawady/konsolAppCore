package com.konsol.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A InvoiceItem.
 */
@Document(collection = "invoice_items")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Field("pk")
    private String pk;

    @Id
    private String id;

    @DBRef
    @Field("item")
    private Item item;

    @DBRef
    @Field("ItemUnit")
    private ItemUnit ItemUnit;

    @Field("unit")
    private String unit;

    @DecimalMin(value = "0")
    @Field("unit_pieces")
    private BigDecimal unitPieces;

    @DecimalMin(value = "0")
    @Field("user_qty")
    private BigDecimal userQty;

    @DecimalMin(value = "0")
    @Field("unit_qty_in")
    private BigDecimal unitQtyIn;

    @DecimalMin(value = "0")
    @Field("unit_qty_out")
    private BigDecimal unitQtyOut;

    @DecimalMin(value = "0")
    @Field("unit_cost")
    private BigDecimal unitCost;

    @DecimalMin(value = "0")
    @Field("unit_price")
    private BigDecimal unitPrice;

    @Min(value = 0)
    @Max(value = 100)
    @Field("discount_per")
    private Integer discountPer;

    @DecimalMin(value = "0")
    @Field("discount")
    private BigDecimal discount;

    @DecimalMin(value = "0")
    @Field("total_cost")
    private BigDecimal totalCost;

    @DecimalMin(value = "0")
    @Field("total_price")
    private BigDecimal totalPrice;

    @DecimalMin(value = "0")
    @Field("qty_in")
    private BigDecimal qtyIn;

    @DecimalMin(value = "0")
    @Field("qty_out")
    private BigDecimal qtyOut;

    @DecimalMin(value = "0")
    @Field("cost")
    private BigDecimal cost;

    @DecimalMin(value = "0")
    @Field("price")
    private BigDecimal price;

    @DecimalMin(value = "0")
    @Field("net_cost")
    private BigDecimal netCost;

    @DecimalMin(value = "0")
    @Field("net_price")
    private BigDecimal netPrice;

    @Field("invoice_id")
    private String invoiceId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getPk() {
        return this.pk;
    }

    public InvoiceItem pk(String pk) {
        this.setPk(pk);
        return this;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getId() {
        return this.id;
    }

    public InvoiceItem id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public com.konsol.core.domain.ItemUnit getItemUnit() {
        return ItemUnit;
    }

    public void setItemUnit(com.konsol.core.domain.ItemUnit itemUnit) {
        ItemUnit = itemUnit;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getUnitPieces() {
        return this.unitPieces;
    }

    public InvoiceItem unitPieces(BigDecimal unitPieces) {
        this.setUnitPieces(unitPieces);
        return this;
    }

    public void setUnitPieces(BigDecimal unitPieces) {
        this.unitPieces = unitPieces;
    }

    public BigDecimal getUserQty() {
        return this.userQty;
    }

    public InvoiceItem userQty(BigDecimal userQty) {
        this.setUserQty(userQty);
        return this;
    }

    public void setUserQty(BigDecimal userQty) {
        this.userQty = userQty;
    }

    public BigDecimal getUnitQtyIn() {
        return this.unitQtyIn;
    }

    public InvoiceItem unitQtyIn(BigDecimal unitQtyIn) {
        this.setUnitQtyIn(unitQtyIn);
        return this;
    }

    public void setUnitQtyIn(BigDecimal unitQtyIn) {
        this.unitQtyIn = unitQtyIn;
    }

    public BigDecimal getUnitQtyOut() {
        return this.unitQtyOut;
    }

    public InvoiceItem unitQtyOut(BigDecimal unitQtyOut) {
        this.setUnitQtyOut(unitQtyOut);
        return this;
    }

    public void setUnitQtyOut(BigDecimal unitQtyOut) {
        this.unitQtyOut = unitQtyOut;
    }

    public BigDecimal getUnitCost() {
        return this.unitCost;
    }

    public InvoiceItem unitCost(BigDecimal unitCost) {
        this.setUnitCost(unitCost);
        return this;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public InvoiceItem unitPrice(BigDecimal unitPrice) {
        this.setUnitPrice(unitPrice);
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getDiscountPer() {
        return this.discountPer;
    }

    public InvoiceItem discountPer(Integer discountPer) {
        this.setDiscountPer(discountPer);
        return this;
    }

    public void setDiscountPer(Integer discountPer) {
        this.discountPer = discountPer;
    }

    public BigDecimal getDiscount() {
        return this.discount;
    }

    public InvoiceItem discount(BigDecimal discount) {
        this.setDiscount(discount);
        return this;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getTotalCost() {
        return this.totalCost;
    }

    public InvoiceItem totalCost(BigDecimal totalCost) {
        this.setTotalCost(totalCost);
        return this;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public InvoiceItem totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getQtyIn() {
        return this.qtyIn;
    }

    public InvoiceItem qtyIn(BigDecimal qtyIn) {
        this.setQtyIn(qtyIn);
        return this;
    }

    public void setQtyIn(BigDecimal qtyIn) {
        this.qtyIn = qtyIn;
    }

    public BigDecimal getQtyOut() {
        return this.qtyOut;
    }

    public InvoiceItem qtyOut(BigDecimal qtyOut) {
        this.setQtyOut(qtyOut);
        return this;
    }

    public void setQtyOut(BigDecimal qtyOut) {
        this.qtyOut = qtyOut;
    }

    public BigDecimal getCost() {
        return this.cost;
    }

    public InvoiceItem cost(BigDecimal cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public InvoiceItem price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getNetCost() {
        return this.netCost;
    }

    public InvoiceItem netCost(BigDecimal netCost) {
        this.setNetCost(netCost);
        return this;
    }

    public void setNetCost(BigDecimal netCost) {
        this.netCost = netCost;
    }

    public BigDecimal getNetPrice() {
        return this.netPrice;
    }

    public InvoiceItem netPrice(BigDecimal netPrice) {
        this.setNetPrice(netPrice);
        return this;
    }

    public void setNetPrice(BigDecimal netPrice) {
        this.netPrice = netPrice;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceItem)) {
            return false;
        }
        return id != null && id.equals(((InvoiceItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceItem{" +
            "id=" + getId() +
            ", pk='" + getPk() + "'" +
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
