package com.konsol.core.domain;

import com.konsol.core.domain.playstation.PlayStationSession;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import javax.annotation.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A CafeTable.
 */
@Document(collection = "cafe_table")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CafeTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("pk")
    private Long pk;

    @NotNull
    @Field("name")
    private String name;

    @NotNull
    @Field("index")
    private Integer index;

    @NotNull
    @Field("active")
    private Boolean active;

    @Nullable
    @DBRef
    @Field("session")
    private PlayStationSession session = null;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public CafeTable id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getPk() {
        return this.pk;
    }

    public CafeTable pk(Long pk) {
        this.setPk(pk);
        return this;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public String getName() {
        return this.name;
    }

    public CafeTable name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIndex() {
        return this.index;
    }

    public CafeTable index(Integer index) {
        this.setIndex(index);
        return this;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Boolean getActive() {
        return this.active;
    }

    public CafeTable active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CafeTable)) {
            return false;
        }
        return getId() != null && getId().equals(((CafeTable) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CafeTable{" +
            "id=" + getId() +
            ", pk=" + getPk() +
            ", name='" + getName() + "'" +
            ", index=" + getIndex() +
            ", active='" + getActive() + "'" +
            "}";
    }
}
