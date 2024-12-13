package com.konsol.core.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Product.
 */
@Document(collection = "System_product")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    private String id;

    @NotNull
    @Field("product_id")
    private String productId;

    @NotNull
    @Field("default_main_price")
    private BigDecimal defaultMainPrice;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Product id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return this.productId;
    }

    public Product productId(String productId) {
        this.setProductId(productId);
        return this;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public BigDecimal getDefaultMainPrice() {
        return this.defaultMainPrice;
    }

    public Product defaultMainPrice(BigDecimal defaultMainPrice) {
        this.setDefaultMainPrice(defaultMainPrice);
        return this;
    }

    public void setDefaultMainPrice(BigDecimal defaultMainPrice) {
        this.defaultMainPrice = defaultMainPrice;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return getId() != null && getId().equals(((Product) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", productId='" + getProductId() + "'" +
            ", defaultMainPrice=" + getDefaultMainPrice() +
            "}";
    }
}
