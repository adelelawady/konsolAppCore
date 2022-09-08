package com.konsol.core.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.konsol.core.domain.StoreItem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StoreItemDTO implements Serializable {

    private String id;

    @DecimalMin(value = "0")
    private BigDecimal qty;

    private ItemDTO item;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public ItemDTO getItem() {
        return item;
    }

    public void setItem(ItemDTO item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StoreItemDTO)) {
            return false;
        }

        StoreItemDTO storeItemDTO = (StoreItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, storeItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StoreItemDTO{" +
            "id='" + getId() + "'" +
            ", qty=" + getQty() +
            ", item=" + getItem() +
            "}";
    }
}
