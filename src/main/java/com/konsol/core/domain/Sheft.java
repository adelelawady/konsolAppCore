package com.konsol.core.domain;

import com.konsol.core.domain.playstation.PlayStationSession;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Sheft.
 */
@Document(collection = "shefts")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Sheft implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    private String id;

    @NotNull
    @Field("start_time")
    private Instant startTime;

    @NotNull
    @Field("end_time")
    private Instant endTime;

    @NotNull
    @Field("active")
    private Boolean active;

    @NotNull
    @Field("assigned_employee")
    private String assignedEmployee;

    @NotNull
    @Field("assigned_employee_user")
    private User assignedEmployeeUser = null;

    @NotNull
    @Field("duration")
    private Duration duration;

    @NotNull
    @Field("totalprice")
    private BigDecimal totalprice;

    @NotNull
    @Field("total_cost")
    private BigDecimal totalCost;

    @NotNull
    @Field("net_price")
    private BigDecimal netPrice;

    @NotNull
    @Field("net_cost")
    private BigDecimal netCost;

    @NotNull
    @Field("net_user_price")
    private BigDecimal netUserPrice;

    @NotNull
    @Field("total_items_out")
    private BigDecimal totalItemsOut;

    @NotNull
    @Field("discount")
    private BigDecimal discount;

    @NotNull
    @Field("invoices_additions")
    private BigDecimal invoicesAdditions;

    @NotNull
    @Field("additions")
    private BigDecimal additions;

    @NotNull
    @Field("additions_notes")
    private BigDecimal additionsNotes;

    @NotNull
    @Field("invoices_expenses")
    private BigDecimal invoicesExpenses;

    @NotNull
    @Field("sheft_expenses")
    private BigDecimal sheftExpenses;

    @NotNull
    @Field("totalinvoices")
    private BigDecimal totalinvoices;

    @NotNull
    @Field("totaldeleted_items")
    private BigDecimal totaldeletedItems;

    @NotNull
    @Field("totaldeleted_items_price")
    private BigDecimal totaldeletedItemsPrice;

    @Field("notes")
    private String notes;

    @DBRef
    @Field("sessions")
    private List<PlayStationSession> sessions = new ArrayList<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Sheft id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public Sheft startTime(Instant startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return this.endTime;
    }

    public Sheft endTime(Instant endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Sheft active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getAssignedEmployee() {
        return this.assignedEmployee;
    }

    public Sheft assignedEmployee(String assignedEmployee) {
        this.setAssignedEmployee(assignedEmployee);
        return this;
    }

    public void setAssignedEmployee(String assignedEmployee) {
        this.assignedEmployee = assignedEmployee;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public Sheft duration(Duration duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public BigDecimal getTotalprice() {
        return this.totalprice;
    }

    public Sheft totalprice(BigDecimal totalprice) {
        this.setTotalprice(totalprice);
        return this;
    }

    public void setTotalprice(BigDecimal totalprice) {
        this.totalprice = totalprice;
    }

    public BigDecimal getTotalCost() {
        return this.totalCost;
    }

    public Sheft totalCost(BigDecimal totalCost) {
        this.setTotalCost(totalCost);
        return this;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getNetPrice() {
        return this.netPrice;
    }

    public Sheft netPrice(BigDecimal netPrice) {
        this.setNetPrice(netPrice);
        return this;
    }

    public void setNetPrice(BigDecimal netPrice) {
        this.netPrice = netPrice;
    }

    public BigDecimal getNetCost() {
        return this.netCost;
    }

    public Sheft netCost(BigDecimal netCost) {
        this.setNetCost(netCost);
        return this;
    }

    public void setNetCost(BigDecimal netCost) {
        this.netCost = netCost;
    }

    public BigDecimal getNetUserPrice() {
        return this.netUserPrice;
    }

    public Sheft netUserPrice(BigDecimal netUserPrice) {
        this.setNetUserPrice(netUserPrice);
        return this;
    }

    public void setNetUserPrice(BigDecimal netUserPrice) {
        this.netUserPrice = netUserPrice;
    }

    public BigDecimal getTotalItemsOut() {
        return this.totalItemsOut;
    }

    public Sheft totalItemsOut(BigDecimal totalItemsOut) {
        this.setTotalItemsOut(totalItemsOut);
        return this;
    }

    public void setTotalItemsOut(BigDecimal totalItemsOut) {
        this.totalItemsOut = totalItemsOut;
    }

    public BigDecimal getDiscount() {
        return this.discount;
    }

    public Sheft discount(BigDecimal discount) {
        this.setDiscount(discount);
        return this;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getInvoicesAdditions() {
        return this.invoicesAdditions;
    }

    public Sheft invoicesAdditions(BigDecimal invoicesAdditions) {
        this.setInvoicesAdditions(invoicesAdditions);
        return this;
    }

    public void setInvoicesAdditions(BigDecimal invoicesAdditions) {
        this.invoicesAdditions = invoicesAdditions;
    }

    public BigDecimal getAdditions() {
        return this.additions;
    }

    public Sheft additions(BigDecimal additions) {
        this.setAdditions(additions);
        return this;
    }

    public void setAdditions(BigDecimal additions) {
        this.additions = additions;
    }

    public BigDecimal getAdditionsNotes() {
        return this.additionsNotes;
    }

    public Sheft additionsNotes(BigDecimal additionsNotes) {
        this.setAdditionsNotes(additionsNotes);
        return this;
    }

    public void setAdditionsNotes(BigDecimal additionsNotes) {
        this.additionsNotes = additionsNotes;
    }

    public BigDecimal getInvoicesExpenses() {
        return this.invoicesExpenses;
    }

    public Sheft invoicesExpenses(BigDecimal invoicesExpenses) {
        this.setInvoicesExpenses(invoicesExpenses);
        return this;
    }

    public void setInvoicesExpenses(BigDecimal invoicesExpenses) {
        this.invoicesExpenses = invoicesExpenses;
    }

    public BigDecimal getSheftExpenses() {
        return this.sheftExpenses;
    }

    public Sheft sheftExpenses(BigDecimal sheftExpenses) {
        this.setSheftExpenses(sheftExpenses);
        return this;
    }

    public void setSheftExpenses(BigDecimal sheftExpenses) {
        this.sheftExpenses = sheftExpenses;
    }

    public BigDecimal getTotalinvoices() {
        return this.totalinvoices;
    }

    public Sheft totalinvoices(BigDecimal totalinvoices) {
        this.setTotalinvoices(totalinvoices);
        return this;
    }

    public void setTotalinvoices(BigDecimal totalinvoices) {
        this.totalinvoices = totalinvoices;
    }

    public BigDecimal getTotaldeletedItems() {
        return this.totaldeletedItems;
    }

    public Sheft totaldeletedItems(BigDecimal totaldeletedItems) {
        this.setTotaldeletedItems(totaldeletedItems);
        return this;
    }

    public void setTotaldeletedItems(BigDecimal totaldeletedItems) {
        this.totaldeletedItems = totaldeletedItems;
    }

    public BigDecimal getTotaldeletedItemsPrice() {
        return this.totaldeletedItemsPrice;
    }

    public Sheft totaldeletedItemsPrice(BigDecimal totaldeletedItemsPrice) {
        this.setTotaldeletedItemsPrice(totaldeletedItemsPrice);
        return this;
    }

    public void setTotaldeletedItemsPrice(BigDecimal totaldeletedItemsPrice) {
        this.totaldeletedItemsPrice = totaldeletedItemsPrice;
    }

    public String getNotes() {
        return this.notes;
    }

    public Sheft notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sheft)) {
            return false;
        }
        return getId() != null && getId().equals(((Sheft) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sheft{" +
            "id=" + getId() +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", active='" + getActive() + "'" +
            ", assignedEmployee='" + getAssignedEmployee() + "'" +
            ", duration='" + getDuration() + "'" +
            ", totalprice=" + getTotalprice() +
            ", totalCost=" + getTotalCost() +
            ", netPrice=" + getNetPrice() +
            ", netCost=" + getNetCost() +
            ", netUserPrice=" + getNetUserPrice() +
            ", totalItemsOut=" + getTotalItemsOut() +
            ", discount=" + getDiscount() +
            ", invoicesAdditions=" + getInvoicesAdditions() +
            ", additions=" + getAdditions() +
            ", additionsNotes=" + getAdditionsNotes() +
            ", invoicesExpenses=" + getInvoicesExpenses() +
            ", sheftExpenses=" + getSheftExpenses() +
            ", totalinvoices=" + getTotalinvoices() +
            ", totaldeletedItems=" + getTotaldeletedItems() +
            ", totaldeletedItemsPrice=" + getTotaldeletedItemsPrice() +
            ", notes='" + getNotes() + "'" +
            "}";
    }

    public @NotNull User getAssignedEmployeeUser() {
        return assignedEmployeeUser;
    }

    public void setAssignedEmployeeUser(@NotNull User assignedEmployeeUser) {
        this.assignedEmployeeUser = assignedEmployeeUser;
    }

    public List<PlayStationSession> getSessions() {
        return sessions;
    }

    public void setSessions(List<PlayStationSession> sessions) {
        this.sessions = sessions;
    }
}
