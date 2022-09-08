package com.konsol.core.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.konsol.core.domain.Item} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemDTO implements Serializable {

    private String id;

    @NotNull
    private String name;

    private String barcode;

    private String price1;

    private String price2;

    private String price3;

    private String category;

    @DecimalMin(value = "0")
    private BigDecimal qty;

    @DecimalMin(value = "0")
    private BigDecimal cost;

    private Integer index;

    private Set<ItemUnitDTO> itemUnits = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getPrice1() {
        return price1;
    }

    public void setPrice1(String price1) {
        this.price1 = price1;
    }

    public String getPrice2() {
        return price2;
    }

    public void setPrice2(String price2) {
        this.price2 = price2;
    }

    public String getPrice3() {
        return price3;
    }

    public void setPrice3(String price3) {
        this.price3 = price3;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Set<ItemUnitDTO> getItemUnits() {
        return itemUnits;
    }

    public void setItemUnits(Set<ItemUnitDTO> itemUnits) {
        this.itemUnits = itemUnits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemDTO)) {
            return false;
        }

        ItemDTO itemDTO = (ItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, itemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", barcode='" + getBarcode() + "'" +
            ", price1='" + getPrice1() + "'" +
            ", price2='" + getPrice2() + "'" +
            ", price3='" + getPrice3() + "'" +
            ", category='" + getCategory() + "'" +
            ", qty=" + getQty() +
            ", cost=" + getCost() +
            ", index=" + getIndex() +
            ", itemUnits=" + getItemUnits() +
            "}";
    }
}
