package com.konsol.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.konsol.core.domain.enumeration.InvoiceKind;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Invoice.
 */
@Document(collection = "invoices")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Field("pk")
    private String pk;

    @Id
    private String id;

    @Field("kind")
    private InvoiceKind kind;

    @DecimalMin(value = "0")
    @Field("total_cost")
    private BigDecimal totalCost;

    @DecimalMin(value = "0")
    @Field("total_price")
    private BigDecimal totalPrice;

    @Min(value = 0)
    @Max(value = 100)
    @Field("discount_per")
    private Integer discountPer;

    @DecimalMin(value = "0")
    @Field("discount")
    private BigDecimal discount;

    @DecimalMin(value = "0")
    @Field("additions")
    private BigDecimal additions;

    @Field("additions_type")
    private String additionsType;

    @DecimalMin(value = "0")
    @Field("net_cost")
    private BigDecimal netCost;

    @DecimalMin(value = "0")
    @Field("net_price")
    private BigDecimal netPrice;

    @DecimalMin(value = "0")
    @Field("expenses")
    private BigDecimal expenses;

    @Field("expenses_type")
    private String expensesType;

    @DBRef
    @Field("bank")
    private Bank bank;

    @DBRef
    @Field("item")
    private Item item;

    @DBRef
    @Field("account")
    private AccountUser account;

    @DBRef
    @Field("invoiceItems")
    @JsonIgnoreProperties(value = { "invoices" }, allowSetters = true)
    private Set<InvoiceItem> invoiceItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getPk() {
        return this.pk;
    }

    public Invoice pk(String pk) {
        this.setPk(pk);
        return this;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getId() {
        return this.id;
    }

    public Invoice id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public InvoiceKind getKind() {
        return this.kind;
    }

    public Invoice kind(InvoiceKind kind) {
        this.setKind(kind);
        return this;
    }

    public void setKind(InvoiceKind kind) {
        this.kind = kind;
    }

    public BigDecimal getTotalCost() {
        return this.totalCost;
    }

    public Invoice totalCost(BigDecimal totalCost) {
        this.setTotalCost(totalCost);
        return this;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public Invoice totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getDiscountPer() {
        return this.discountPer;
    }

    public Invoice discountPer(Integer discountPer) {
        this.setDiscountPer(discountPer);
        return this;
    }

    public void setDiscountPer(Integer discountPer) {
        this.discountPer = discountPer;
    }

    public BigDecimal getDiscount() {
        return this.discount;
    }

    public Invoice discount(BigDecimal discount) {
        this.setDiscount(discount);
        return this;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getAdditions() {
        return this.additions;
    }

    public Invoice additions(BigDecimal additions) {
        this.setAdditions(additions);
        return this;
    }

    public void setAdditions(BigDecimal additions) {
        this.additions = additions;
    }

    public String getAdditionsType() {
        return this.additionsType;
    }

    public Invoice additionsType(String additionsType) {
        this.setAdditionsType(additionsType);
        return this;
    }

    public void setAdditionsType(String additionsType) {
        this.additionsType = additionsType;
    }

    public BigDecimal getNetCost() {
        return this.netCost;
    }

    public Invoice netCost(BigDecimal netCost) {
        this.setNetCost(netCost);
        return this;
    }

    public void setNetCost(BigDecimal netCost) {
        this.netCost = netCost;
    }

    public BigDecimal getNetPrice() {
        return this.netPrice;
    }

    public Invoice netPrice(BigDecimal netPrice) {
        this.setNetPrice(netPrice);
        return this;
    }

    public void setNetPrice(BigDecimal netPrice) {
        this.netPrice = netPrice;
    }

    public BigDecimal getExpenses() {
        return this.expenses;
    }

    public Invoice expenses(BigDecimal expenses) {
        this.setExpenses(expenses);
        return this;
    }

    public void setExpenses(BigDecimal expenses) {
        this.expenses = expenses;
    }

    public String getExpensesType() {
        return this.expensesType;
    }

    public Invoice expensesType(String expensesType) {
        this.setExpensesType(expensesType);
        return this;
    }

    public void setExpensesType(String expensesType) {
        this.expensesType = expensesType;
    }

    public Bank getBank() {
        return this.bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Invoice bank(Bank bank) {
        this.setBank(bank);
        return this;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Invoice item(Item item) {
        this.setItem(item);
        return this;
    }

    public AccountUser getAccount() {
        return this.account;
    }

    public void setAccount(AccountUser accountUser) {
        this.account = accountUser;
    }

    public Invoice account(AccountUser accountUser) {
        this.setAccount(accountUser);
        return this;
    }

    public Set<InvoiceItem> getInvoiceItems() {
        return this.invoiceItems;
    }

    public void setInvoiceItems(Set<InvoiceItem> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    public Invoice invoiceItems(Set<InvoiceItem> invoiceItems) {
        this.setInvoiceItems(invoiceItems);
        return this;
    }

    public Invoice addInvoiceItems(InvoiceItem invoiceItem) {
        this.invoiceItems.add(invoiceItem);
        invoiceItem.getInvoices().add(this);
        return this;
    }

    public Invoice removeInvoiceItems(InvoiceItem invoiceItem) {
        this.invoiceItems.remove(invoiceItem);
        invoiceItem.getInvoices().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return id != null && id.equals(((Invoice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", pk='" + getPk() + "'" +
            ", kind='" + getKind() + "'" +
            ", totalCost=" + getTotalCost() +
            ", totalPrice=" + getTotalPrice() +
            ", discountPer=" + getDiscountPer() +
            ", discount=" + getDiscount() +
            ", additions=" + getAdditions() +
            ", additionsType='" + getAdditionsType() + "'" +
            ", netCost=" + getNetCost() +
            ", netPrice=" + getNetPrice() +
            ", expenses=" + getExpenses() +
            ", expensesType='" + getExpensesType() + "'" +
            "}";
    }
}
