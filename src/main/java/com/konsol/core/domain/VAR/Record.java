package com.konsol.core.domain.VAR;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.konsol.core.domain.AbstractAuditingEntity;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Record.
 */
@Document(collection = "record")
public class Record extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("start")
    private Instant start;

    @Field("end")
    private Instant end;

    @Field("total_price")
    private Double totalPrice = 0.0;

    @Field("total_price_time")
    private Double totalPriceTime = 0.0;

    @Field("total_price_orders")
    private Double totalPriceOrders = 0.0;

    @Field("total_price_user")
    private Double totalPriceUser = 0.0;

    @Field("duration")
    private Duration duration;

    //Duration

    @Field("device")
    @JsonIgnoreProperties(value = { "session" }, allowSetters = true)
    private Device device;

    @Field("multi")
    private boolean multi = false;

    @Field("ordersDiscount")
    private Double ordersDiscount = 0.0;

    @Field("timeDiscount")
    private Double timeDiscount = 0.0;

    @Field("totalNetPriceCalculated")
    private Double totalNetPriceCalculated = 0.0;

    @Field("total_discount_price")
    private Double totalDiscountPrice = 0.0;

    public void setTotalDiscountPrice(Double totalDiscountPrice) {
        this.totalDiscountPrice = totalDiscountPrice;
    }

    public Double getTotalDiscountPrice() {
        return totalDiscountPrice;
    }

    @Field("ordersData")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private Set<Product> ordersData = new HashSet<>();

    @Field("ordersQuantity")
    private HashMap<String, Integer> ordersQuantity = new HashMap<>();

    @Field("previousSessionsTotalPrice")
    private Double previousSessionsTotalPrice = 0.0;

    public Double getPreviousSessionsTotalPrice() {
        return previousSessionsTotalPrice;
    }

    public void setPreviousSessionsTotalPrice(Double previousSessionsTotalPrice) {
        this.previousSessionsTotalPrice = previousSessionsTotalPrice;
    }

    @Field("previousSessions")
    @JsonIgnoreProperties(
        value = {
            "device.session",
            "device.category",
            "device.type",
            "ordersData",
            "ordersQuantity",
            "previousSessions",
            "id",
            "totalPriceOrders",
            "totalPriceUser",
            "ordersDiscount",
            "timeDiscount",
            "duration",
            "createdDate",
            "totalPrice",
        },
        allowSetters = true
    )
    //@JsonIgnore
    private List<Record> previousSessions = new ArrayList<>();

    public List<Record> getPreviousSessions() {
        return previousSessions;
    }

    public void setPreviousSessions(List<Record> previousSessions) {
        this.previousSessions = previousSessions;
    }

    private int minutes = 0;
    private int hours = 0;

    public int getMinutes() {
        if (this.duration == null) {
            return 0;
        }
        this.minutes = this.duration.toMinutesPart();
        return this.duration.toMinutesPart();
    }

    public int getHours() {
        if (this.duration == null) {
            return 0;
        }
        this.hours = this.duration.toHoursPart();
        return this.duration.toHoursPart();
    }

    public Double getTotalNetPriceCalculated() {
        return totalNetPriceCalculated;
    }

    public void setTotalNetPriceCalculated(Double totalNetPriceCalculated) {
        this.totalNetPriceCalculated = totalNetPriceCalculated;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public HashMap<String, Integer> getOrdersQuantity() {
        return ordersQuantity;
    }

    public void setOrdersQuantity(HashMap<String, Integer> ordersQuantity) {
        this.ordersQuantity = ordersQuantity;
    }

    public String getId() {
        return this.id;
    }

    public Record id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getStart() {
        return this.start;
    }

    public Record start(Instant start) {
        this.setStart(start);
        return this;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Instant getEnd() {
        return this.end;
    }

    public Record end(Instant end) {
        this.setEnd(end);
        return this;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    public Double getTotalPrice() {
        return this.totalPrice;
    }

    public Record totalPrice(Double totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Device getDevice() {
        return this.device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Record device(Device device) {
        this.setDevice(device);
        return this;
    }

    public Double getTotalPriceUser() {
        return totalPriceUser;
    }

    public void setTotalPriceUser(Double totalPriceUser) {
        this.totalPriceUser = totalPriceUser;
    }

    public boolean isMulti() {
        return multi;
    }

    public void setMulti(boolean multi) {
        this.multi = multi;
    }

    public Double getOrdersDiscount() {
        return ordersDiscount;
    }

    public void setOrdersDiscount(Double ordersDiscount) {
        this.ordersDiscount = ordersDiscount;
    }

    public Double getTimeDiscount() {
        return timeDiscount;
    }

    public void setTimeDiscount(Double timeDiscount) {
        this.timeDiscount = timeDiscount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Record)) {
            return false;
        }
        return id != null && id.equals(((Record) o).id);
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    public Double getTotalPriceTime() {
        return totalPriceTime;
    }

    public void setTotalPriceTime(Double totalPriceTime) {
        this.totalPriceTime = totalPriceTime;
    }

    public Double getTotalPriceOrders() {
        return totalPriceOrders;
    }

    public void setTotalPriceOrders(Double totalPriceOrders) {
        this.totalPriceOrders = totalPriceOrders;
    }

    public Set<Product> getOrdersData() {
        return ordersData;
    }

    public void setOrdersData(Set<Product> ordersData) {
        this.ordersData = ordersData;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Record{" +
            "id=" + getId() +
            ", start='" + getStart() + "'" +
            ", end='" + getEnd() + "'" +
            ", totalPrice=" + getTotalPrice() +
            "}";
    }
}
