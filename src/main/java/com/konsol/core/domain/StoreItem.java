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
 * A StoreItem.
 */
@Document(collection = "store_items")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StoreItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @DecimalMin(value = "0")
    @Field("qty")
    private BigDecimal qty;

    @DBRef
    @Field("item")
    private Item item;

    @DBRef
    @Field("storeIds")
    @JsonIgnoreProperties(value = { "items" }, allowSetters = true)
    private Set<Store> storeIds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public StoreItem id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getQty() {
        return this.qty;
    }

    public StoreItem qty(BigDecimal qty) {
        this.setQty(qty);
        return this;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public StoreItem item(Item item) {
        this.setItem(item);
        return this;
    }

    public Set<Store> getStoreIds() {
        return this.storeIds;
    }

    public void setStoreIds(Set<Store> stores) {
        if (this.storeIds != null) {
            this.storeIds.forEach(i -> i.removeItems(this));
        }
        if (stores != null) {
            stores.forEach(i -> i.addItems(this));
        }
        this.storeIds = stores;
    }

    public StoreItem storeIds(Set<Store> stores) {
        this.setStoreIds(stores);
        return this;
    }

    public StoreItem addStoreId(Store store) {
        this.storeIds.add(store);
        store.getItems().add(this);
        return this;
    }

    public StoreItem removeStoreId(Store store) {
        this.storeIds.remove(store);
        store.getItems().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StoreItem)) {
            return false;
        }
        return id != null && id.equals(((StoreItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StoreItem{" +
            "id=" + getId() +
            ", qty=" + getQty() +
            "}";
    }
}
