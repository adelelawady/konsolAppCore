package com.konsol.core.domain.VAR;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Base abstract class for entities which will hold definitions for created, last modified, created by,
 * last modified by attributes.
 */
@JsonIgnoreProperties(value = { "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate" }, allowGetters = true)
public abstract class VARAbstractAuditingEntity<T> implements Serializable {

    @Field("var_ref_id")
    private String varRefId;

    public String getVarRefId() {
        return varRefId;
    }

    public void setVarRefId(String varRefId) {
        this.varRefId = varRefId;
    }
}
