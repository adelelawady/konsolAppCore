package com.konsol.core.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.konsol.core.domain.PriceOption} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PriceOptionDTO implements Serializable {

    private String id;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal value;

    @NotNull
    private String productId;

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

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PriceOptionDTO)) {
            return false;
        }

        PriceOptionDTO priceOptionDTO = (PriceOptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, priceOptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PriceOptionDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", value=" + getValue() +
            ", productId='" + getProductId() + "'" +
            "}";
    }
}
