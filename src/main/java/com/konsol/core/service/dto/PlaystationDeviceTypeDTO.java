package com.konsol.core.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.konsol.core.domain.PlaystationDeviceType} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlaystationDeviceTypeDTO implements Serializable {

    private String id;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal defaultMainPrice;

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

    public BigDecimal getDefaultMainPrice() {
        return defaultMainPrice;
    }

    public void setDefaultMainPrice(BigDecimal defaultMainPrice) {
        this.defaultMainPrice = defaultMainPrice;
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
        if (!(o instanceof PlaystationDeviceTypeDTO)) {
            return false;
        }

        PlaystationDeviceTypeDTO playstationDeviceTypeDTO = (PlaystationDeviceTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, playstationDeviceTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlaystationDeviceTypeDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", defaultMainPrice=" + getDefaultMainPrice() +
            ", productId='" + getProductId() + "'" +
            "}";
    }
}
