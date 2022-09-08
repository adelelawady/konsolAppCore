package com.konsol.core.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.konsol.core.domain.ItemUnit} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemUnitDTO implements Serializable {

    private String id;

    @NotNull
    private String name;

    @DecimalMin(value = "0")
    private BigDecimal pieces;

    @DecimalMin(value = "0")
    private BigDecimal price;

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

    public BigDecimal getPieces() {
        return pieces;
    }

    public void setPieces(BigDecimal pieces) {
        this.pieces = pieces;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemUnitDTO)) {
            return false;
        }

        ItemUnitDTO itemUnitDTO = (ItemUnitDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, itemUnitDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemUnitDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", pieces=" + getPieces() +
            ", price=" + getPrice() +
            "}";
    }
}
