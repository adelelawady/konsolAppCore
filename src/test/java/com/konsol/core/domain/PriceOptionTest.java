package com.konsol.core.domain;

import static com.konsol.core.domain.PriceOptionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PriceOptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PriceOption.class);
        PriceOption priceOption1 = getPriceOptionSample1();
        PriceOption priceOption2 = new PriceOption();
        assertThat(priceOption1).isNotEqualTo(priceOption2);

        priceOption2.setId(priceOption1.getId());
        assertThat(priceOption1).isEqualTo(priceOption2);

        priceOption2 = getPriceOptionSample2();
        assertThat(priceOption1).isNotEqualTo(priceOption2);
    }
}
