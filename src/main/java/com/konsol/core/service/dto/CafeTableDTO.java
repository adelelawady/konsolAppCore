package com.konsol.core.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.konsol.core.domain.CafeTable} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CafeTableDTO implements Serializable {

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
        if (!(o instanceof CafeTableDTO)) {
            return false;
        }

        CafeTableDTO cafeTableDTO = (CafeTableDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cafeTableDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CafeTableDTO{" +
            "id='" + getId() + "'" +
            ", pk=" + getPk() +
            ", name='" + getName() + "'" +
            ", index=" + getIndex() +
            ", active='" + getActive() + "'" +
            "}";
    }
}
