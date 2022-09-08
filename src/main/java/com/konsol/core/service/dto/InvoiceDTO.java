package com.konsol.core.service.dto;

import com.konsol.core.domain.enumeration.InvoiceKind;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.konsol.core.domain.Invoice} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceDTO implements Serializable {

    private String pk;

    private String id;

    private InvoiceKind kind;

    @DecimalMin(value = "0")
    private BigDecimal totalCost;

    @DecimalMin(value = "0")
    private BigDecimal totalPrice;

    @Min(value = 0)
    @Max(value = 100)
    private Integer discountPer;

    @DecimalMin(value = "0")
    private BigDecimal discount;

    @DecimalMin(value = "0")
    private BigDecimal additions;

    private String additionsType;

    @DecimalMin(value = "0")
    private BigDecimal netCost;

    @DecimalMin(value = "0")
    private BigDecimal netPrice;

    @DecimalMin(value = "0")
    private BigDecimal expenses;

    private String expensesType;

    private BankDTO bank;

    private ItemDTO item;

    private AccountUserDTO account;

    private Set<InvoiceItemDTO> invoiceItems = new HashSet<>();

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public InvoiceKind getKind() {
        return kind;
    }

    public void setKind(InvoiceKind kind) {
        this.kind = kind;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(Integer discountPer) {
        this.discountPer = discountPer;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getAdditions() {
        return additions;
    }

    public void setAdditions(BigDecimal additions) {
        this.additions = additions;
    }

    public String getAdditionsType() {
        return additionsType;
    }

    public void setAdditionsType(String additionsType) {
        this.additionsType = additionsType;
    }

    public BigDecimal getNetCost() {
        return netCost;
    }

    public void setNetCost(BigDecimal netCost) {
        this.netCost = netCost;
    }

    public BigDecimal getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(BigDecimal netPrice) {
        this.netPrice = netPrice;
    }

    public BigDecimal getExpenses() {
        return expenses;
    }

    public void setExpenses(BigDecimal expenses) {
        this.expenses = expenses;
    }

    public String getExpensesType() {
        return expensesType;
    }

    public void setExpensesType(String expensesType) {
        this.expensesType = expensesType;
    }

    public BankDTO getBank() {
        return bank;
    }

    public void setBank(BankDTO bank) {
        this.bank = bank;
    }

    public ItemDTO getItem() {
        return item;
    }

    public void setItem(ItemDTO item) {
        this.item = item;
    }

    public AccountUserDTO getAccount() {
        return account;
    }

    public void setAccount(AccountUserDTO account) {
        this.account = account;
    }

    public Set<InvoiceItemDTO> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(Set<InvoiceItemDTO> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceDTO)) {
            return false;
        }

        InvoiceDTO invoiceDTO = (InvoiceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, invoiceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceDTO{" +
            "pk='" + getPk() + "'" +
            ", id='" + getId() + "'" +
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
            ", bank=" + getBank() +
            ", item=" + getItem() +
            ", account=" + getAccount() +
            ", invoiceItems=" + getInvoiceItems() +
            "}";
    }
}
