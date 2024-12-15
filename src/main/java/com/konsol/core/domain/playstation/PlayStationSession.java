package com.konsol.core.domain.playstation;

import com.konsol.core.domain.Invoice;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
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
    @Field("device")
    private PlaystationDevice device;

    @NotNull
    @Field("type")
    private PlaystationDeviceType type;

    @DBRef
    @Field("invoice")
    private Invoice invoice;

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
        return active;
    }

    public PlayStationSession active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public PlayStationSession startTime(Instant startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public PlayStationSession endTime(Instant endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public PlayStationSession invoice(Invoice invoice) {
        this.setInvoice(invoice);
        return this;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayStationSession)) {
            return false;
        }
        return id != null && id.equals(((PlayStationSession) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "PlayStationSession{" +
            "id=" +
            getId() +
            ", active='" +
            getActive() +
            "'" +
            ", startTime='" +
            getStartTime() +
            "'" +
            ", endTime='" +
            getEndTime() +
            "'" +
            ", invoice='" +
            getInvoice() +
            "'" +
            "}"
        );
    }

    public @NotNull PlaystationDevice getDevice() {
        return device;
    }

    public void setDevice(@NotNull PlaystationDevice device) {
        this.device = device;
    }

    public PlayStationSession device(PlaystationDevice device) {
        this.device = device;
        return this;
    }

    public @NotNull PlaystationDeviceType getType() {
        return type;
    }

    public void setType(@NotNull PlaystationDeviceType type) {
        this.type = type;
    }

    public PlayStationSession type(PlaystationDeviceType type) {
        this.type = type;
        return this;
    }
}
