package com.konsol.core.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemUnitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemUnit.class);
        ItemUnit itemUnit1 = new ItemUnit();
        itemUnit1.setId("id1");
        ItemUnit itemUnit2 = new ItemUnit();
        itemUnit2.setId(itemUnit1.getId());
        assertThat(itemUnit1).isEqualTo(itemUnit2);
        itemUnit2.setId("id2");
        assertThat(itemUnit1).isNotEqualTo(itemUnit2);
        itemUnit1.setId(null);
        assertThat(itemUnit1).isNotEqualTo(itemUnit2);
    }
}
