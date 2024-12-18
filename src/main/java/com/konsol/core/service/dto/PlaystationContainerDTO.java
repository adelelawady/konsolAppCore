package com.konsol.core.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.konsol.core.domain.PlaystationContainer} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlaystationContainerDTO implements Serializable {

    @NotNull
    private String id;

    @NotNull
    private String name;

    @NotNull
    private String category;

    private String defaultIcon;

    @NotNull
    private Boolean hasTimeManagement;

    @NotNull
    private Boolean showType;

    @NotNull
    private Boolean showTime;

    @NotNull
    private Boolean canMoveDevice;

    @NotNull
    private Boolean canHaveMultiTimeManagement;

    @NotNull
    private String acceptedOrderCategories;

    @NotNull
    private String orderSelectedPriceCategory;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDefaultIcon() {
        return defaultIcon;
    }

    public void setDefaultIcon(String defaultIcon) {
        this.defaultIcon = defaultIcon;
    }

    public Boolean getHasTimeManagement() {
        return hasTimeManagement;
    }

    public void setHasTimeManagement(Boolean hasTimeManagement) {
        this.hasTimeManagement = hasTimeManagement;
    }

    public Boolean getShowType() {
        return showType;
    }

    public void setShowType(Boolean showType) {
        this.showType = showType;
    }

    public Boolean getShowTime() {
        return showTime;
    }

    public void setShowTime(Boolean showTime) {
        this.showTime = showTime;
    }

    public Boolean getCanMoveDevice() {
        return canMoveDevice;
    }

    public void setCanMoveDevice(Boolean canMoveDevice) {
        this.canMoveDevice = canMoveDevice;
    }

    public Boolean getCanHaveMultiTimeManagement() {
        return canHaveMultiTimeManagement;
    }

    public void setCanHaveMultiTimeManagement(Boolean canHaveMultiTimeManagement) {
        this.canHaveMultiTimeManagement = canHaveMultiTimeManagement;
    }

    public String getAcceptedOrderCategories() {
        return acceptedOrderCategories;
    }

    public void setAcceptedOrderCategories(String acceptedOrderCategories) {
        this.acceptedOrderCategories = acceptedOrderCategories;
    }

    public String getOrderSelectedPriceCategory() {
        return orderSelectedPriceCategory;
    }

    public void setOrderSelectedPriceCategory(String orderSelectedPriceCategory) {
        this.orderSelectedPriceCategory = orderSelectedPriceCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlaystationContainerDTO)) {
            return false;
        }

        PlaystationContainerDTO playstationContainerDTO = (PlaystationContainerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, playstationContainerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlaystationContainerDTO{" +
            "id='" + getId() + "'" +
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
