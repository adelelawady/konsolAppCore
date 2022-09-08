package com.konsol.core.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BankDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BankDTO.class);
        BankDTO bankDTO1 = new BankDTO();
        bankDTO1.setId("id1");
        BankDTO bankDTO2 = new BankDTO();
        assertThat(bankDTO1).isNotEqualTo(bankDTO2);
        bankDTO2.setId(bankDTO1.getId());
        assertThat(bankDTO1).isEqualTo(bankDTO2);
        bankDTO2.setId("id2");
        assertThat(bankDTO1).isNotEqualTo(bankDTO2);
        bankDTO1.setId(null);
        assertThat(bankDTO1).isNotEqualTo(bankDTO2);
    }
}
