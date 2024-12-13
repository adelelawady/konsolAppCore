package com.konsol.core.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CafeTableDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CafeTableDTO.class);
        CafeTableDTO cafeTableDTO1 = new CafeTableDTO();
        cafeTableDTO1.setId("id1");
        CafeTableDTO cafeTableDTO2 = new CafeTableDTO();
        assertThat(cafeTableDTO1).isNotEqualTo(cafeTableDTO2);
        cafeTableDTO2.setId(cafeTableDTO1.getId());
        assertThat(cafeTableDTO1).isEqualTo(cafeTableDTO2);
        cafeTableDTO2.setId("id2");
        assertThat(cafeTableDTO1).isNotEqualTo(cafeTableDTO2);
        cafeTableDTO1.setId(null);
        assertThat(cafeTableDTO1).isNotEqualTo(cafeTableDTO2);
    }
}
