package com.konsol.core.domain.playstation;

import com.konsol.core.domain.AbstractAuditingEntity;
import com.konsol.core.domain.Invoice;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A PlayStationSession.
 */
@Document(collection = "ps_session")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlayStationSession extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

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

    @Field("device_sessions")
    private List<PlayStationSession> deviceSessions = new ArrayList<>();

    @Field("device_sessions_net_price")
    private BigDecimal deviceSessionsNetPrice = new BigDecimal(0);

    @Field("containerId")
    private String containerId;

    @DBRef
    @Field("invoice")
    private Invoice invoice;

    public String getId() {
        return this.id;
    }

    public PlayStationSession id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
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

    public List<PlayStationSession> getDeviceSessions() {
        return deviceSessions;
    }

    public void setDeviceSessions(List<PlayStationSession> deviceSessions) {
        this.deviceSessions = deviceSessions;
    }

    public BigDecimal getDeviceSessionsNetPrice() {
        return deviceSessionsNetPrice;
    }

    public void setDeviceSessionsNetPrice(BigDecimal deviceSessionsNetPrice) {
        this.deviceSessionsNetPrice = deviceSessionsNetPrice;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public PlayStationSession containerId(@javax.validation.constraints.NotNull String containerId) {
        this.setContainerId(containerId);
        return this;
    }
}
