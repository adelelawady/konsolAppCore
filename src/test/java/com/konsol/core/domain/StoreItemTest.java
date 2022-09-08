package com.konsol.core.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StoreItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StoreItem.class);
        StoreItem storeItem1 = new StoreItem();
        storeItem1.setId("id1");
        StoreItem storeItem2 = new StoreItem();
        storeItem2.setId(storeItem1.getId());
        assertThat(storeItem1).isEqualTo(storeItem2);
        storeItem2.setId("id2");
        assertThat(storeItem1).isNotEqualTo(storeItem2);
        storeItem1.setId(null);
        assertThat(storeItem1).isNotEqualTo(storeItem2);
    }
}
