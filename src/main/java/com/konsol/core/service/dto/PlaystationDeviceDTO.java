package com.konsol.core.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.konsol.core.domain.PlaystationDevice} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlaystationDeviceDTO implements Serializable {

    private String id;

    @NotNull
    private Long pk;

    @NotNull
    private String name;

    @NotNull
    private Integer index;

    @NotNull
    private Boolean active;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlaystationDeviceDTO)) {
            return false;
        }

        PlaystationDeviceDTO playstationDeviceDTO = (PlaystationDeviceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, playstationDeviceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlaystationDeviceDTO{" +
            "id='" + getId() + "'" +
            ", pk=" + getPk() +
            ", name='" + getName() + "'" +
            ", index=" + getIndex() +
            ", active='" + getActive() + "'" +
            "}";
    }
}
