package com.konsol.core.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PriceOptionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PriceOptionDTO.class);
        PriceOptionDTO priceOptionDTO1 = new PriceOptionDTO();
        priceOptionDTO1.setId("id1");
        PriceOptionDTO priceOptionDTO2 = new PriceOptionDTO();
        assertThat(priceOptionDTO1).isNotEqualTo(priceOptionDTO2);
        priceOptionDTO2.setId(priceOptionDTO1.getId());
        assertThat(priceOptionDTO1).isEqualTo(priceOptionDTO2);
        priceOptionDTO2.setId("id2");
        assertThat(priceOptionDTO1).isNotEqualTo(priceOptionDTO2);
        priceOptionDTO1.setId(null);
        assertThat(priceOptionDTO1).isNotEqualTo(priceOptionDTO2);
    }
}
