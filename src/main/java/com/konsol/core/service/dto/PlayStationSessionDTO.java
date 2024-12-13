package com.konsol.core.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.konsol.core.domain.PlayStationSession} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlayStationSessionDTO implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    private Boolean active;

    @NotNull
    private Instant startTime;

    private Instant endTime;

    @NotNull
    private String invoiceId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayStationSessionDTO)) {
            return false;
        }

        PlayStationSessionDTO playStationSessionDTO = (PlayStationSessionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, playStationSessionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlayStationSessionDTO{" +
            "id=" + getId() +
            ", active='" + getActive() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", invoiceId='" + getInvoiceId() + "'" +
            "}";
    }
}
