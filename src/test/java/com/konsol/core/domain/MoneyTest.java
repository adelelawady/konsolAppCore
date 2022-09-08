package com.konsol.core.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MoneyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Money.class);
        Money money1 = new Money();
        money1.setId("id1");
        Money money2 = new Money();
        money2.setId(money1.getId());
        assertThat(money1).isEqualTo(money2);
        money2.setId("id2");
        assertThat(money1).isNotEqualTo(money2);
        money1.setId(null);
        assertThat(money1).isNotEqualTo(money2);
    }
}
