package com.konsol.core.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StoreItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StoreItemDTO.class);
        StoreItemDTO storeItemDTO1 = new StoreItemDTO();
        storeItemDTO1.setId("id1");
        StoreItemDTO storeItemDTO2 = new StoreItemDTO();
        assertThat(storeItemDTO1).isNotEqualTo(storeItemDTO2);
        storeItemDTO2.setId(storeItemDTO1.getId());
        assertThat(storeItemDTO1).isEqualTo(storeItemDTO2);
        storeItemDTO2.setId("id2");
        assertThat(storeItemDTO1).isNotEqualTo(storeItemDTO2);
        storeItemDTO1.setId(null);
        assertThat(storeItemDTO1).isNotEqualTo(storeItemDTO2);
    }
}
