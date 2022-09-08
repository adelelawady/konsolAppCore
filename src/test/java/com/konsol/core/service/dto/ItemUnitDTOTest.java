package com.konsol.core.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemUnitDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemUnitDTO.class);
        ItemUnitDTO itemUnitDTO1 = new ItemUnitDTO();
        itemUnitDTO1.setId("id1");
        ItemUnitDTO itemUnitDTO2 = new ItemUnitDTO();
        assertThat(itemUnitDTO1).isNotEqualTo(itemUnitDTO2);
        itemUnitDTO2.setId(itemUnitDTO1.getId());
        assertThat(itemUnitDTO1).isEqualTo(itemUnitDTO2);
        itemUnitDTO2.setId("id2");
        assertThat(itemUnitDTO1).isNotEqualTo(itemUnitDTO2);
        itemUnitDTO1.setId(null);
        assertThat(itemUnitDTO1).isNotEqualTo(itemUnitDTO2);
    }
}
