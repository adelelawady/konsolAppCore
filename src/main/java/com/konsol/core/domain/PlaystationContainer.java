package com.konsol.core.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A PlaystationContainer.
 */
@Document(collection = "ps_container")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlaystationContainer implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    private String id;

    @NotNull
    @Field("name")
    private String name;

    @NotNull
    @Field("category")
    private String category;

    @Field("default_icon")
    private String defaultIcon;

    @NotNull
    @Field("has_time_management")
    private Boolean hasTimeManagement;

    @NotNull
    @Field("show_type")
    private Boolean showType;

    @NotNull
    @Field("show_time")
    private Boolean showTime;

    @NotNull
    @Field("can_move_device")
    private Boolean canMoveDevice;

    @NotNull
    @Field("can_have_multi_time_management")
    private Boolean canHaveMultiTimeManagement;

    @NotNull
    @Field("accepted_order_categories")
    private String acceptedOrderCategories;

    @NotNull
    @Field("order_selected_price_category")
    private String orderSelectedPriceCategory;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public PlaystationContainer id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public PlaystationContainer name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return this.category;
    }

    public PlaystationContainer category(String category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDefaultIcon() {
        return this.defaultIcon;
    }

    public PlaystationContainer defaultIcon(String defaultIcon) {
        this.setDefaultIcon(defaultIcon);
        return this;
    }

    public void setDefaultIcon(String defaultIcon) {
        this.defaultIcon = defaultIcon;
    }

    public Boolean getHasTimeManagement() {
        return this.hasTimeManagement;
    }

    public PlaystationContainer hasTimeManagement(Boolean hasTimeManagement) {
        this.setHasTimeManagement(hasTimeManagement);
        return this;
    }

    public void setHasTimeManagement(Boolean hasTimeManagement) {
        this.hasTimeManagement = hasTimeManagement;
    }

    public Boolean getShowType() {
        return this.showType;
    }

    public PlaystationContainer showType(Boolean showType) {
        this.setShowType(showType);
        return this;
    }

    public void setShowType(Boolean showType) {
        this.showType = showType;
    }

    public Boolean getShowTime() {
        return this.showTime;
    }

    public PlaystationContainer showTime(Boolean showTime) {
        this.setShowTime(showTime);
        return this;
    }

    public void setShowTime(Boolean showTime) {
        this.showTime = showTime;
    }

    public Boolean getCanMoveDevice() {
        return this.canMoveDevice;
    }

    public PlaystationContainer canMoveDevice(Boolean canMoveDevice) {
        this.setCanMoveDevice(canMoveDevice);
        return this;
    }

    public void setCanMoveDevice(Boolean canMoveDevice) {
        this.canMoveDevice = canMoveDevice;
    }

    public Boolean getCanHaveMultiTimeManagement() {
        return this.canHaveMultiTimeManagement;
    }

    public PlaystationContainer canHaveMultiTimeManagement(Boolean canHaveMultiTimeManagement) {
        this.setCanHaveMultiTimeManagement(canHaveMultiTimeManagement);
        return this;
    }

    public void setCanHaveMultiTimeManagement(Boolean canHaveMultiTimeManagement) {
        this.canHaveMultiTimeManagement = canHaveMultiTimeManagement;
    }

    public String getAcceptedOrderCategories() {
        return this.acceptedOrderCategories;
    }

    public PlaystationContainer acceptedOrderCategories(String acceptedOrderCategories) {
        this.setAcceptedOrderCategories(acceptedOrderCategories);
        return this;
    }

    public void setAcceptedOrderCategories(String acceptedOrderCategories) {
        this.acceptedOrderCategories = acceptedOrderCategories;
    }

    public String getOrderSelectedPriceCategory() {
        return this.orderSelectedPriceCategory;
    }

    public PlaystationContainer orderSelectedPriceCategory(String orderSelectedPriceCategory) {
        this.setOrderSelectedPriceCategory(orderSelectedPriceCategory);
        return this;
    }

    public void setOrderSelectedPriceCategory(String orderSelectedPriceCategory) {
        this.orderSelectedPriceCategory = orderSelectedPriceCategory;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlaystationContainer)) {
            return false;
        }
        return getId() != null && getId().equals(((PlaystationContainer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlaystationContainer{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", category='" + getCategory() + "'" +
            ", defaultIcon='" + getDefaultIcon() + "'" +
            ", hasTimeManagement='" + getHasTimeManagement() + "'" +
            ", showType='" + getShowType() + "'" +
            ", showTime='" + getShowTime() + "'" +
            ", canMoveDevice='" + getCanMoveDevice() + "'" +
            ", canHaveMultiTimeManagement='" + getCanHaveMultiTimeManagement() + "'" +
            ", acceptedOrderCategories='" + getAcceptedOrderCategories() + "'" +
            ", orderSelectedPriceCategory='" + getOrderSelectedPriceCategory() + "'" +
            "}";
    }
}
