package com.konsol.core.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

public class DeletedInvoiceItem extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @DBRef
    @Field("item")
    private Item item;

    @Field("deleted_qty")
    private BigDecimal deletedQty;

    @Field("deleted_date")
    private Instant deletedDate;

    @Field("unit")
    private String unit;

    @Field("unit_pieces")
    private BigDecimal unitPieces;

    public DeletedInvoiceItem() {}

    public DeletedInvoiceItem(InvoiceItem invoiceItem, BigDecimal deletedQty) {
        this.item = invoiceItem.getItem();
        this.deletedQty = deletedQty;
        this.deletedDate = Instant.now();
        this.unit = invoiceItem.getUnit();
        this.unitPieces = invoiceItem.getUnitPieces();
    }

    @Override
    public String getId() {
        return item.getId();
    }

    // Getters and Setters
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public BigDecimal getDeletedQty() {
        return deletedQty;
    }

    public void setDeletedQty(BigDecimal deletedQty) {
        this.deletedQty = deletedQty;
    }

    public Instant getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Instant deletedDate) {
        this.deletedDate = deletedDate;
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
}
