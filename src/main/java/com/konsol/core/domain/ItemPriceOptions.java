package com.konsol.core.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A Item.
 */

public class ItemPriceOptions implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private BigDecimal value;

    private Set<String> refId = new LinkedHashSet<>();

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getRefId() {
        return refId;
    }

    public void setRefId(Set<String> refId) {
        this.refId = refId;
    }
}
