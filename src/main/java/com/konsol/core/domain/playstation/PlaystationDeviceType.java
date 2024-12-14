package com.konsol.core.domain.playstation;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A PlaystationDeviceType.
 */
@Document(collection = "ps_device_type")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlaystationDeviceType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("name")
    private String name;

    @NotNull
    @Field("price")
    private BigDecimal price;

    @NotNull
    @DBRef
    @Field("product")
    private com.konsol.core.domain.Item Item;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public PlaystationDeviceType id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public PlaystationDeviceType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlaystationDeviceType)) {
            return false;
        }
        return getId() != null && getId().equals(((PlaystationDeviceType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlaystationDeviceType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }

    public @NotNull BigDecimal getPrice() {
        return price;
    }

    public void setPrice(@NotNull BigDecimal price) {
        this.price = price;
    }

    public com.konsol.core.domain.@NotNull Item getItem() {
        return Item;
    }

    public void setItem(com.konsol.core.domain.@NotNull Item item) {
        Item = item;
    }
}
