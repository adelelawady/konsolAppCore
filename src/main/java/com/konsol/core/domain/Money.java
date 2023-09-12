package com.konsol.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.konsol.core.domain.enumeration.MoneyKind;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.*;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Money.
 */
@Document(collection = "monies")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Money extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Field("pk")
    private String pk;

    @Id
    private String id;

    @Field("kind")
    private MoneyKind kind;

    @DecimalMin(value = "0")
    @Field("money_in")
    private BigDecimal moneyIn;

    @DecimalMin(value = "0")
    @Field("money_out")
    private BigDecimal moneyOut;

    @DBRef
    @Field("bank")
    private Bank bank;

    @DBRef
    @Field("item")
    private Item item;

    @DBRef
    @Field("account")
    private AccountUser account;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getPk() {
        return this.pk;
    }

    public Money pk(String pk) {
        this.setPk(pk);
        return this;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getId() {
        return this.id;
    }

    public Money id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MoneyKind getKind() {
        return this.kind;
    }

    public Money kind(MoneyKind kind) {
        this.setKind(kind);
        return this;
    }

    public void setKind(MoneyKind kind) {
        this.kind = kind;
    }

    public BigDecimal getMoneyIn() {
        return this.moneyIn;
    }

    public Money moneyIn(BigDecimal moneyIn) {
        this.setMoneyIn(moneyIn);
        return this;
    }

    public void setMoneyIn(BigDecimal moneyIn) {
        this.moneyIn = moneyIn;
    }

    public BigDecimal getMoneyOut() {
        return this.moneyOut;
    }

    public Money moneyOut(BigDecimal moneyOut) {
        this.setMoneyOut(moneyOut);
        return this;
    }

    public void setMoneyOut(BigDecimal moneyOut) {
        this.moneyOut = moneyOut;
    }

    public Bank getBank() {
        return this.bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Money bank(Bank bank) {
        this.setBank(bank);
        return this;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Money item(Item item) {
        this.setItem(item);
        return this;
    }

    public AccountUser getAccount() {
        return this.account;
    }

    public void setAccount(AccountUser accountUser) {
        this.account = accountUser;
    }

    public Money account(AccountUser accountUser) {
        this.setAccount(accountUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Money)) {
            return false;
        }
        return id != null && id.equals(((Money) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Money{" +
            "id=" + getId() +
            ", pk='" + getPk() + "'" +
            ", kind='" + getKind() + "'" +
            ", moneyIn=" + getMoneyIn() +
            ", moneyOut=" + getMoneyOut() +
            "}";
    }
}
