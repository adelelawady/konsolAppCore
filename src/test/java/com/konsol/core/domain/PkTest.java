package com.konsol.core.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pk.class);
        Pk pk1 = new Pk();
        pk1.setId("id1");
        Pk pk2 = new Pk();
        pk2.setId(pk1.getId());
        assertThat(pk1).isEqualTo(pk2);
        pk2.setId("id2");
        assertThat(pk1).isNotEqualTo(pk2);
        pk1.setId(null);
        assertThat(pk1).isNotEqualTo(pk2);
    }
}
