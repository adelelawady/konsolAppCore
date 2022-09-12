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
 * A ItemUnit.
 */
@Document(collection = "item_units")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("name")
    private String name;

    @DecimalMin(value = "0")
    @Field("pieces")
    private BigDecimal pieces;

    @DecimalMin(value = "0")
    @Field("price")
    private BigDecimal price;

    @Field("basic")
    private boolean basic;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public ItemUnit id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ItemUnit name(String name) {
        this.setName(name);
        return this;
    }

    public boolean isBasic() {
        return basic;
    }

    public void setBasic(boolean basic) {
        this.basic = basic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPieces() {
        return this.pieces;
    }

    public ItemUnit pieces(BigDecimal pieces) {
        this.setPieces(pieces);
        return this;
    }

    public void setPieces(BigDecimal pieces) {
        this.pieces = pieces;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public ItemUnit price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemUnit)) {
            return false;
        }
        return id != null && id.equals(((ItemUnit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemUnit{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", pieces=" + getPieces() +
            ", price=" + getPrice() +
            "}";
    }
}
