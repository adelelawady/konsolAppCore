package com.konsol.core.service.dto;

import com.konsol.core.domain.enumeration.AccountKind;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.konsol.core.domain.AccountUser} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccountUserDTO implements Serializable {

    private String id;

    @NotNull
    private String name;

    private AccountKind kind;

    @DecimalMin(value = "0")
    private BigDecimal balanceIn;

    @DecimalMin(value = "0")
    private BigDecimal balanceOut;

    private String phone;

    private String address;

    private String address2;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountKind getKind() {
        return kind;
    }

    public void setKind(AccountKind kind) {
        this.kind = kind;
    }

    public BigDecimal getBalanceIn() {
        return balanceIn;
    }

    public void setBalanceIn(BigDecimal balanceIn) {
        this.balanceIn = balanceIn;
    }

    public BigDecimal getBalanceOut() {
        return balanceOut;
    }

    public void setBalanceOut(BigDecimal balanceOut) {
        this.balanceOut = balanceOut;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountUserDTO)) {
            return false;
        }

        AccountUserDTO accountUserDTO = (AccountUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, accountUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountUserDTO{" +
            "id='" + getId() + "'" +
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
