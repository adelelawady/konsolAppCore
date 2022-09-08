package com.konsol.core.service.dto;

import com.konsol.core.domain.enumeration.MoneyKind;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.konsol.core.domain.Money} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MoneyDTO implements Serializable {

    private String pk;

    private String id;

    private MoneyKind kind;

    @DecimalMin(value = "0")
    private BigDecimal moneyIn;

    @DecimalMin(value = "0")
    private BigDecimal moneyOut;

    private BankDTO bank;

    private ItemDTO item;

    private AccountUserDTO account;

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

    public MoneyKind getKind() {
        return kind;
    }

    public void setKind(MoneyKind kind) {
        this.kind = kind;
    }

    public BigDecimal getMoneyIn() {
        return moneyIn;
    }

    public void setMoneyIn(BigDecimal moneyIn) {
        this.moneyIn = moneyIn;
    }

    public BigDecimal getMoneyOut() {
        return moneyOut;
    }

    public void setMoneyOut(BigDecimal moneyOut) {
        this.moneyOut = moneyOut;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MoneyDTO)) {
            return false;
        }

        MoneyDTO moneyDTO = (MoneyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, moneyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MoneyDTO{" +
            "pk='" + getPk() + "'" +
            ", id='" + getId() + "'" +
            ", kind='" + getKind() + "'" +
            ", moneyIn=" + getMoneyIn() +
            ", moneyOut=" + getMoneyOut() +
            ", bank=" + getBank() +
            ", item=" + getItem() +
            ", account=" + getAccount() +
            "}";
    }
}
