package com.konsol.core.domain;

import com.konsol.core.domain.enumeration.PkKind;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Pk.
 */
@Document(collection = "pk")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pk implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("kind")
    private PkKind kind;

    @DecimalMin(value = "0")
    @Field("value")
    private BigDecimal value;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Pk id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PkKind getKind() {
        return this.kind;
    }

    public Pk kind(PkKind kind) {
        this.setKind(kind);
        return this;
    }

    public void setKind(PkKind kind) {
        this.kind = kind;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public Pk value(BigDecimal value) {
        this.setValue(value);
        return this;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pk)) {
            return false;
        }
        return id != null && id.equals(((Pk) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pk{" +
            "id=" + getId() +
            ", kind='" + getKind() + "'" +
            ", value=" + getValue() +
            "}";
    }
}
