package com.konsol.core.service.dto;

import com.konsol.core.domain.enumeration.PkKind;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.konsol.core.domain.Pk} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PkDTO implements Serializable {

    private String id;

    private PkKind kind;

    @DecimalMin(value = "0")
    private BigDecimal value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PkKind getKind() {
        return kind;
    }

    public void setKind(PkKind kind) {
        this.kind = kind;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PkDTO)) {
            return false;
        }

        PkDTO pkDTO = (PkDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pkDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PkDTO{" +
            "id='" + getId() + "'" +
            ", kind='" + getKind() + "'" +
            ", value=" + getValue() +
            "}";
    }
}
