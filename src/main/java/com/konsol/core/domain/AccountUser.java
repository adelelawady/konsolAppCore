package com.konsol.core.domain;

import com.konsol.core.domain.enumeration.AccountKind;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A AccountUser.
 */
@Document(collection = "accounts")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccountUser extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("name")
    private String name;

    @Field("kind")
    private AccountKind kind;

    @DecimalMin(value = "0")
    @Field("balance_in")
    private BigDecimal balanceIn;

    @DecimalMin(value = "0")
    @Field("balance_out")
    private BigDecimal balanceOut;

    @Field("phone")
    private String phone;

    @Field("address")
    private String address;

    @Field("address_2")
    private String address2;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public AccountUser id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public AccountUser name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountKind getKind() {
        return this.kind;
    }

    public AccountUser kind(AccountKind kind) {
        this.setKind(kind);
        return this;
    }

    public void setKind(AccountKind kind) {
        this.kind = kind;
    }

    public BigDecimal getBalanceIn() {
        return this.balanceIn;
    }

    public AccountUser balanceIn(BigDecimal balanceIn) {
        this.setBalanceIn(balanceIn);
        return this;
    }

    public void setBalanceIn(BigDecimal balanceIn) {
        this.balanceIn = balanceIn;
    }

    public BigDecimal getBalanceOut() {
        return this.balanceOut;
    }

    public AccountUser balanceOut(BigDecimal balanceOut) {
        this.setBalanceOut(balanceOut);
        return this;
    }

    public void setBalanceOut(BigDecimal balanceOut) {
        this.balanceOut = balanceOut;
    }

    public String getPhone() {
        return this.phone;
    }

    public AccountUser phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return this.address;
    }

    public AccountUser address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return this.address2;
    }

    public AccountUser address2(String address2) {
        this.setAddress2(address2);
        return this;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountUser)) {
            return false;
        }
        return id != null && id.equals(((AccountUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountUser{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", kind='" + getKind() + "'" +
            ", balanceIn=" + getBalanceIn() +
            ", balanceOut=" + getBalanceOut() +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            ", address2='" + getAddress2() + "'" +
            "}";
    }
}
