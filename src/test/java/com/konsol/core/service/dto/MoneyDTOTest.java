package com.konsol.core.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MoneyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MoneyDTO.class);
        MoneyDTO moneyDTO1 = new MoneyDTO();
        moneyDTO1.setId("id1");
        MoneyDTO moneyDTO2 = new MoneyDTO();
        assertThat(moneyDTO1).isNotEqualTo(moneyDTO2);
        moneyDTO2.setId(moneyDTO1.getId());
        assertThat(moneyDTO1).isEqualTo(moneyDTO2);
        moneyDTO2.setId("id2");
        assertThat(moneyDTO1).isNotEqualTo(moneyDTO2);
        moneyDTO1.setId(null);
        assertThat(moneyDTO1).isNotEqualTo(moneyDTO2);
    }
}
