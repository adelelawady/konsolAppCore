package com.konsol.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Item.
 */
@Entity
@Table(name = "item")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Item extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("pk")
    @Indexed(unique = true)
    private String pk;

    @NotNull
    @Field("name")
    private String name;

    @Field("barcode")
    private String barcode;

    @Field("price_1")
    private String price1;

    @Field("price_2")
    private String price2;

    @Field("category")
    private String category;

    @DecimalMin(value = "0")
    @Field("qty")
    private BigDecimal qty;

    @DecimalMin(value = "0")
    @Field("cost")
    private BigDecimal cost;

    @Field("checkQty")
    private boolean checkQty = true; //QuantityCheckRequired


    @Field("deletable")
    private boolean deletable = true; //QuantityCheckRequired

    @DBRef
    @Field("itemUnits")
    //@JsonIgnoreProperties(value = { "items" }, allowSetters = true)
    private Set<ItemUnit> itemUnits = new HashSet<>();

    @Field("price_options")
    private List<ItemPriceOptions> PriceOptions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "device_type_id")
    private PlaystationDeviceType deviceType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Item id(String id) {
        this.setId(id);
        return this;
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

    public String getName() {
        return this.name;
    }

    public Item name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return this.barcode;
    }

    public Item barcode(String barcode) {
        this.setBarcode(barcode);
        return this;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getPrice1() {
        return this.price1;
    }

    public Item price1(String price1) {
        this.setPrice1(price1);
        return this;
    }

    public void setPrice1(String price1) {
        this.price1 = price1;
    }

    public String getPrice2() {
        return this.price2;
    }

    public Item price2(String price2) {
        this.setPrice2(price2);
        return this;
    }

    public void setPrice2(String price2) {
        this.price2 = price2;
    }

    public String getCategory() {
        return this.category;
    }

    public Item category(String category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getQty() {
        return this.qty;
    }

    public Item qty(BigDecimal qty) {
        this.setQty(qty);
        return this;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getCost() {
        return this.cost;
    }

    public Item cost(BigDecimal cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Set<ItemUnit> getItemUnits() {
        return this.itemUnits;
    }

    public void setItemUnits(Set<ItemUnit> itemUnits) {
        this.itemUnits = itemUnits;
    }

    public Item itemUnits(Set<ItemUnit> itemUnits) {
        this.setItemUnits(itemUnits);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        return id != null && id.equals(((Item) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Item{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", barcode='" + getBarcode() + "'" +
            ", price1='" + getPrice1() + "'" +
            ", price2='" + getPrice2() + "'" +
            ", category='" + getCategory() + "'" +
            ", qty=" + getQty() +
            ", cost=" + getCost() +
            "}";
    }

    public boolean isCheckQty() {
        return checkQty;
    }

    public void setCheckQty(boolean checkQty) {
        this.checkQty = checkQty;
    }

    public List<ItemPriceOptions> getPriceOptions() {
        return PriceOptions;
    }

    public void setPriceOptions(List<ItemPriceOptions> priceOptions) {
        PriceOptions = priceOptions;
    }

    public PlaystationDeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(PlaystationDeviceType deviceType) {
        this.deviceType = deviceType;
    }
}
