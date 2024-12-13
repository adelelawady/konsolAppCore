package com.konsol.core.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A PlayStationSession.
 */
@Document(collection = "ps_session")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlayStationSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    private Long id;

    @NotNull
    @Field("active")
    private Boolean active;

    @NotNull
    @Field("start_time")
    private Instant startTime;

    @Field("end_time")
    private Instant endTime;

    @NotNull
    @Field("invoice_id")
    private String invoiceId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PlayStationSession id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return this.active;
    }

    public PlayStationSession active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public PlayStationSession startTime(Instant startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return this.endTime;
    }

    public PlayStationSession endTime(Instant endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getInvoiceId() {
        return this.invoiceId;
    }

    public PlayStationSession invoiceId(String invoiceId) {
        this.setInvoiceId(invoiceId);
        return this;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayStationSession)) {
            return false;
        }
        return getId() != null && getId().equals(((PlayStationSession) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlayStationSession{" +
            "id=" + getId() +
            ", active='" + getActive() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", invoiceId='" + getInvoiceId() + "'" +
            "}";
    }
}
